package com.coretree.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.coretree.models.CDRData;
import com.coretree.models.CDRRequest;
import com.coretree.models.CDRResponse;
// import com.coretree.models.Options;
import com.coretree.sql.DBConnection;

public class CdrServer implements Runnable
{
	private DatagramSocket serverSocket;
	private Thread[] threads;
	private int threadcount = 1;
	// private Options _option;
	
	public CdrServer()
	{
		try {
			// InetSocketAddress address = new InetSocketAddress("localhost", 21003);
			serverSocket = new DatagramSocket(21003);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		threads = new Thread[threadcount];
		
        for (int i = 0; i < threads.length; i++) {
        	threads[i] = new Thread(this);
        	threads[i].start();
        }
	}
	
	public void InitiateSocket()
	{
		try
		{
			byte[] receiveData = new byte[1024];
			byte[] sendData = null;

			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				System.err.println("has received.");
				
				byte[] rcvbytes = receivePacket.getData();
				CDRRequest rcvObj = new CDRRequest(rcvbytes, ByteOrder.BIG_ENDIAN);
				CDRData rcvData = new CDRData(rcvObj.data, ByteOrder.BIG_ENDIAN);
				
				// DB
				System.err.println(rcvData.toString());
				//
				
				// Response
				CDRResponse res = new CDRResponse(ByteOrder.BIG_ENDIAN);
				res.cmd = 2;
				res.pCdr = rcvObj.pCdr;
				res.status = 0;
				sendData = res.toBytes();
				
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
				serverSocket.send(sendPacket);
				System.err.println("Has answered.");
				
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
					System.out.println(String.format("stream end event insert db : sql: %s", sql));
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.InitiateSocket();
	}
}
