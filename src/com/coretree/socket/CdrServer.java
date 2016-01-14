package com.coretree.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteOrder;
import com.coretree.models.CDRData;
import com.coretree.models.CDRRequest;
import com.coretree.models.CDRResponse;
// import com.coretree.models.Options;

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
