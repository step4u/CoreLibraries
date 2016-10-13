package com.coretree.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.plaf.ComponentInputMapUIResource;

import com.coretree.consts.ErrorMessages;
import com.coretree.models.CDRData;
import com.coretree.models.CDRRequest;
import com.coretree.models.CDRResponse;
// import com.coretree.models.Options;
import com.coretree.sql.DBConnection;
import com.coretree.util.Const4pbx;
import com.coretree.util.Finalvars;
import com.coretree.util.Util;

public class CdrServer {
	private DatagramSocket serverSocket;
	private InetSocketAddress remoteep;
	private int localport = 21003;
	private String pbxip = "127.0.0.1";
	private int pbxport = 21005;
	private Thread[] threads;
	private int threadcount = 1;
	private ByteOrder defaultbyteorder = ByteOrder.BIG_ENDIAN;
	// private Options _option;
	
	public CdrServer() {
		this(21003, "127.0.0.1", 21005, ByteOrder.BIG_ENDIAN);
	}
	
	public CdrServer(String pbxip, ByteOrder byteorder) {
		this(21003, pbxip, 21005, byteorder);
	}
	
	public CdrServer(ByteOrder byteorder) {
		this(21003, "127.0.0.1", 21005, byteorder);
	}
	
	public CdrServer(int localport, String pbxip, int pbxport, ByteOrder byteorder) {
		this.pbxip = pbxip;
		this.pbxport = pbxport;
		this.localport = localport;
		this.defaultbyteorder = byteorder;
		
		try {
			InetSocketAddress localep = new InetSocketAddress("localhost", this.localport);
			// serverSocket = new DatagramSocket(localep);
			serverSocket = new DatagramSocket(21003);
			this.remoteep = new InetSocketAddress(this.pbxip, this.pbxport);
			serverSocket.connect(this.remoteep);
			
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					byte[] receiveData = new byte[1024];
					byte[] sendData = null;

					while (true) {
						
						try {
							DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
							serverSocket.receive(receivePacket);
							System.err.println("has received.");
							
							byte[] rcvbytes = receivePacket.getData();
							CDRRequest rcvObj = new CDRRequest(rcvbytes, defaultbyteorder);
							
							if (rcvObj.cmd == Const4pbx.CDR_GET_LIST_RES)
								continue;
							
							CDRData rcvData = new CDRData(rcvObj.data, defaultbyteorder);
							if (rcvData.getSeq() < 1)
								continue;
							
							// DB
							// System.err.println(rcvData.toString());
							//
							
							System.err.println(rcvData.toString());
							
							String sql = "insert into cdr "
									+ " ( idx, office_name, startdate, enddate"
									+ ", caller_type, caller, caller_ipn_number, caller_group_code, caller_group_name, caller_human_name"
									+ ", callee_type, callee, callee_ipn_number, callee_group_code, callee_group_name, callee_human_name"
									+ ", result, seq )"
									+ " values "
									+ " ( gen_id(GEN_CDR_IDX, 1), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
							
							Connection con = null;

							try {
								con = DBConnection.getConnection();
								con.setAutoCommit(false);
								
								PreparedStatement stmt = con.prepareStatement(sql);
								
								stmt.setString(1, rcvData.getOffice_name());
								stmt.setTimestamp(2, rcvData.getStartdate());
								stmt.setTimestamp(3, rcvData.getEnddate());
								stmt.setInt(4, rcvData.getCaller_type());
								stmt.setString(5, rcvData.getCaller());
								stmt.setString(6, rcvData.getCaller_ipn_number());
								stmt.setString(7, rcvData.getCaller_group_code());
								stmt.setString(8, rcvData.getCaller_group_name());
								stmt.setString(9, rcvData.getCaller_human_name());
								stmt.setInt(10, rcvData.getCallee_type());
								stmt.setString(11, rcvData.getCallee());
								stmt.setString(12, rcvData.getCallee_ipn_number());
								stmt.setString(13, rcvData.getCallee_group_code());
								stmt.setString(14, rcvData.getCallee_group_name());
								stmt.setString(15, rcvData.getCallee_human_name());
								stmt.setInt(16, rcvData.getResult());
								stmt.setInt(17, rcvData.getSeq());
								
								stmt.executeUpdate();
								con.commit();
								
								// Response
								CDRResponse res = new CDRResponse(defaultbyteorder);
								res.cmd = 2;
								res.pCdr = rcvObj.pCdr;
								res.status = 0;
								sendData = res.toBytes();
								
								DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
								serverSocket.send(sendPacket);
								System.err.println("has answered.");
								
								// System.err.println(rcvData.toString());
								// System.out.println(String.format("stream end event insert db : sql: %s", sql));
							} catch (SQLException ex) {
								// ex.printStackTrace();
								try {
									Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_SQL, ex.getMessage()), 1);
									con.rollback();
								} catch (SQLException e) {
									Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_SQL, e.getMessage()), 1);
								}
							}  catch (IllegalArgumentException iee) {
								Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_PARSING, iee.getMessage()), 1);
							} finally {
								try {
									// Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_SQL, e.getMessage()), 1);
									con.close();
								} catch (SQLException e) {
									Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_SQL, e.getMessage()), 1);
								}
							}
							
							/* finally {
								try {
									System.err.println(rcvData.toString());
									System.out.println(String.format("stream end event insert db : sql: %s", sql));
								} catch (IllegalArgumentException iee) {
									continue;
								}
							}*/
						} catch (SocketException se) {
							Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_SOCKET, se.getMessage()), 1);
						} catch (IOException ie) {
							// ie.printStackTrace();
							Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_IO, ie.getMessage()), 1);
						}
					}
				}
			};
			
			threads = new Thread[threadcount];
			
	        for (int i = 0; i < threads.length; i++) {
	        	threads[i] = new Thread(runnable);
	        	threads[i].start();
	        }
			
			this.Register();
		} catch (SocketException e) {
			// e.printStackTrace();
			Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_SOCKET, e.getMessage()), 1);
		}
	}
	
	/*
	public void InitiateSocket() {
		byte[] receiveData = new byte[1024];
		byte[] sendData = null;

		while (true) {
			
			try {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				System.err.println("has received.");
				
				byte[] rcvbytes = receivePacket.getData();
				CDRRequest rcvObj = new CDRRequest(rcvbytes, defaultbyteorder);
				CDRData rcvData = new CDRData(rcvObj.data, defaultbyteorder);
				
				// DB
				// System.err.println(rcvData.toString());
				//
				
				// Response
				CDRResponse res = new CDRResponse(defaultbyteorder);
				res.cmd = 2;
				res.pCdr = rcvObj.pCdr;
				res.status = 0;
				sendData = res.toBytes();
				
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
				serverSocket.send(sendPacket);
				System.err.println("Has answered.");
				
				String sql = "insert into cdr "
						+ " ( idx, office_name, startdate, enddate"
						+ ", caller_type, caller, caller_ipn_number, caller_group_code, caller_group_name, caller_human_name"
						+ ", callee_type, callee, callee_ipn_number, callee_group_code, callee_group_name, callee_human_name"
						+ ", result, seq )"
						+ " values "
						+ " ( gen_id(GEN_CDR_IDX, 1), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
				try(Connection con = DBConnection.getConnection();
						PreparedStatement stmt = con.prepareStatement(sql)) {
					con.setAutoCommit(true);
					
					stmt.setString(1, rcvData.getOffice_name());
					stmt.setTimestamp(2, rcvData.getStartdate());
					stmt.setTimestamp(3, rcvData.getEnddate());
					stmt.setInt(4, rcvData.getCaller_type());
					stmt.setString(5, rcvData.getCaller());
					stmt.setString(6, rcvData.getCaller_ipn_number());
					stmt.setString(7, rcvData.getCaller_group_code());
					stmt.setString(8, rcvData.getCaller_group_name());
					stmt.setString(9, rcvData.getCaller_human_name());
					stmt.setInt(10, rcvData.getCallee_type());
					stmt.setString(11, rcvData.getCallee());
					stmt.setString(12, rcvData.getCallee_ipn_number());
					stmt.setString(13, rcvData.getCallee_group_code());
					stmt.setString(14, rcvData.getCallee_group_name());
					stmt.setString(15, rcvData.getCallee_human_name());
					stmt.setInt(16, rcvData.getResult());
					stmt.setInt(17, rcvData.getSeq());
					
					stmt.executeUpdate();
					
					// con.commit();
				} catch (SQLException ex) {
					// ex.printStackTrace();
					Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_SQL, ex.getMessage()), 1);
				} finally {
					System.err.println(rcvData.toString());
					System.out.println(String.format("stream end event insert db : sql: %s", sql));
				}
			} catch (SocketException se) {
				Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_SOCKET, se.getMessage()), 1);
			} catch (IOException ie) {
				// ie.printStackTrace();
				Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_IO, ie.getMessage()), 1);
			}
		}
	}
	*/
	
	private void Register() {
		CDRResponse res = new CDRResponse();
		res.cmd = Const4pbx.CDR_GET_LIST_REQ;
		byte[] sendData = res.toBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
		
		try {
			this.serverSocket.send(sendPacket);
		} catch (IOException e) {
			Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CDR_IO, e.getMessage()), 1);
		}
	}

/*	@Override
	public void run() {
		this.InitiateSocket();
	}*/
}
