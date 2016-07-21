package com.coretree.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.coretree.models.CDRData;
import com.coretree.models.CDRRequest;
import com.coretree.models.CDRResponse;
// import com.coretree.models.Options;
import com.coretree.sql.DBConnection;
import com.coretree.util.Const4pbx;

public class CdrServer implements Runnable {
	private DatagramSocket serverSocket;
	private Thread[] threads;
	private int threadcount = 1;
	private long sleeptimer = 2000;
	private ByteOrder defaultbyteorder = ByteOrder.BIG_ENDIAN;
	
	private InetAddress PbxCdrAddress;
	private int PbxCdrPort = 21003;
	// private Options _option;
	
	public CdrServer() {
		this(ByteOrder.BIG_ENDIAN, "localhost", 21003);
	}
	
	public CdrServer(ByteOrder byteorder) {
		this(byteorder, "localhost", 21003);
	}
	
	public CdrServer(ByteOrder byteorder, String address) {
		this(byteorder, address, 21003);
	}
	
	public CdrServer(ByteOrder byteorder, String address, int port) {
		defaultbyteorder = byteorder;
		
		try {
			this.PbxCdrAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		this.PbxCdrPort = port;
		
		try {
			// InetSocketAddress address = new InetSocketAddress("localhost", 21003);
			// serverSocket = new DatagramSocket(address);
			serverSocket = new DatagramSocket(21003);
			
			threads = new Thread[this.threadcount];
			
	        for (int i = 0; i < threads.length; i++) {
	        	threads[i] = new Thread(this);
	        	threads[i].start();
	        }
	        
			this.RequestQueues();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void InitiateSocket() {
		try {
			byte[] receiveData = new byte[1024];
			byte[] sendData = null;

			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				System.err.println("ready to receive.");
				serverSocket.receive(receivePacket);
				System.err.println("has received at InitiateSocket().");
				
				byte[] rcvbytes = receivePacket.getData();
				CDRRequest rcvObj = new CDRRequest(rcvbytes, defaultbyteorder);
				
				switch (rcvObj.cmd) {
					case Const4pbx.CDR_SAVE_REQ:
						CDRData rcvData = new CDRData(rcvObj.data, defaultbyteorder);
						
						// DB
						// System.err.println(rcvData.toString());
						//
						
						// Response
						CDRResponse res = new CDRResponse(this.defaultbyteorder);
						res.cmd = Const4pbx.CDR_SAVE_RES;
						res.pCdr = rcvObj.pCdr;
						res.status = 0;
						sendData = res.toBytes();
						
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
						serverSocket.send(sendPacket);
						System.err.println("Has sent at InitiateSocket().");
						
						String sql = "insert into cdr "
								+ " ( office_name, startdate, enddate"
								+ ", caller_type, caller, caller_ipn_number, caller_group_code, caller_group_name, caller_human_name"
								+ ", callee_type, callee, callee_ipn_number, callee_group_code, callee_group_name, callee_human_name"
								+ ", result, seq )"
								+ " values "
								+ " ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						
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
							ex.printStackTrace();
						} finally {
							System.err.println(rcvData.toString());
							System.out.println(String.format("stream end event insert db : sql: %s", sql));
						}
						break;
					case Const4pbx.CDR_GET_LIST_REQ:
					case Const4pbx.CDR_GET_LIST_RES:
					case Const4pbx.CDR_SAVE_RES:						
					default:
						break;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	private void RequestQueues() {
		CDRResponse res = new CDRResponse(defaultbyteorder);
		res.cmd = Const4pbx.CDR_GET_LIST_REQ;
		byte[] sendData = res.toBytes();
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.PbxCdrAddress, this.PbxCdrPort);
		try {
			serverSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.err.println("Has sent at RequestQueues().");
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.InitiateSocket();
	}
}
