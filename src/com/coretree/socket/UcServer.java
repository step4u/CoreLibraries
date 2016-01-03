package com.coretree.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;

import com.coretree.event.Event;
import com.coretree.event.HaveGotUcMessageEventArgs;
import com.coretree.models.GroupWareData;
import com.coretree.models.UcMessage;
import com.coretree.util.Const4pbx;

public class UcServer implements Runnable
{
	public Event<HaveGotUcMessageEventArgs> HaveGotUcMessageEventHandler = new Event<HaveGotUcMessageEventArgs>();
	
	private ByteOrder byteorder = ByteOrder.BIG_ENDIAN;
	
	private DatagramSocket serverSocket;
	private InetSocketAddress remoteep;
	private Thread[] threads;
	private Timer timer;
	private int timerInterval = 30000;
	// private Options _option;
	
	public UcServer()
	{
		this("127.0.0.1", 31001, 1, ByteOrder.BIG_ENDIAN);
	}
	
	public UcServer(String addr, ByteOrder order)
	{
		this(addr, 31001, 1, order);
	}
	
	public UcServer(String addr, int port, int threadcount, ByteOrder order)
	{
		try
		{
			byteorder = order;
			remoteep = new InetSocketAddress(addr, port);
			
			// InetSocketAddress address = new InetSocketAddress("localhost", 21003);
			serverSocket = new DatagramSocket(31001);
			serverSocket.connect(remoteep);
			
			threads = new Thread[threadcount];
			
	        for (int i = 0; i < threads.length; i++) {
	        	threads[i] = new Thread(this);
	        	threads[i].start();
	        }
	        
	        Timer_Elapsed timer_elapsed = new Timer_Elapsed();
			timer = new Timer();
			timer.schedule(timer_elapsed, timerInterval, timerInterval);
		}
		catch (SocketException e)
		{
			
		}
	}
	
	public void start() {
        for (int i = 0; i < threads.length; i++) {
        	threads[i] = new Thread(this);
        	threads[i].start();
        }
    }
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		this.InitiateSocket();
	}
	
	private void InitiateSocket()
	{
		try
		{
			byte[] receiveData = new byte[1024];
			// byte[] sendData = null;

			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				
				// byte[] rcvbytes = receivePacket.getData();
				this.DataHasRecievedHandle(serverSocket, receivePacket.getData());
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void Send(UcMessage msg) throws UnknownHostException
	{
		GroupWareData data = this.GetData(msg);
		byte[] sendData = data.toBytes();
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
		
		try {
			serverSocket.send(sendPacket);
			System.out.println("");
			System.err.println(String.format("Has sent %s", data.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("an error has broken out.");
			e.printStackTrace();
		}
	}
	
	public void Send(DatagramSocket sock, DatagramPacket packet, GroupWareData data) throws IOException
	{
		byte[] sendData = data.toBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
		sock.send(sendPacket);
		System.err.println("has sent.");
	}
	
	private void DataHasRecievedHandle(DatagramSocket sock, byte[] bytes)
	{
		GroupWareData data = new GroupWareData(bytes, byteorder);
		
		if (HaveGotUcMessageEventHandler != null)
			HaveGotUcMessageEventHandler.raiseEvent(this, new HaveGotUcMessageEventArgs(data));
	}
	
	public void regist()
	{
		UcMessage msg = new UcMessage();
		msg.cmd = Const4pbx.UC_REGISTER_REQ;
		try {
			this.Send(msg);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private GroupWareData GetData(UcMessage msg) throws UnknownHostException
	{
		GroupWareData data = new GroupWareData(byteorder);
		data.cmd = msg.cmd;
		
		switch (msg.cmd)
		{
			case Const4pbx.UC_REGISTER_REQ:
				InetAddress bar = null;
				try {
					bar = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//System.err.println(String.format("InetAddress.getLocalHost() : %s, %s", InetAddress.getLocalHost(), InetAddress.getLocalHost().getHostAddress()));
				//System.err.println(String.format("bar.getHostAddress() : %s", bar.getHostAddress()));
				
			    int value = ByteBuffer.wrap(bar.getAddress()).getInt();
				//System.err.println(String.format("UC_REGISTER_REQ inetaddr.hashCode=%d", inetaddr.hashCode()));
				//System.err.println(String.format("UC_REGISTER_REQ inetaddr to int=%d", value));
				
				data.type = Const4pbx.UC_TYPE_GROUPWARE;
				data.ip = value;
				data.port = 31001;
				break;
			case Const4pbx.UC_UNREGISTER_REQ:
				data.type = Const4pbx.UC_TYPE_GROUPWARE;
				break;
			case Const4pbx.UC_BUSY_EXT_REQ:
				break;
			case Const4pbx.UC_INFO_SRV_REQ:
				data.extension = msg.extension;
				break;
			case Const4pbx.UC_MAKE_CALL_REQ:
				data.type = Const4pbx.UC_TYPE_GROUPWARE;
				data.extension = msg.extension;
				data.caller = msg.extension;
				data.callee = msg.peer;
				break;
			case Const4pbx.UC_DROP_CALL_REQ:
				data.type = Const4pbx.UC_TYPE_GROUPWARE;
				data.extension = msg.extension;
				data.caller = msg.extension;
				data.callee = msg.peer;
				break;
			case Const4pbx.UC_HOLD_CALL_REQ:
				data.type = Const4pbx.UC_TYPE_GROUPWARE;
				data.extension = msg.extension;
				data.caller = msg.extension;
				data.callee = msg.peer;
				break;
			case Const4pbx.UC_TRANSFER_CALL_REQ:
				data.type = Const4pbx.UC_TYPE_GROUPWARE;
				data.extension = msg.extension;
				data.caller = msg.extension;
				data.callee = msg.peer;
				break;
			case Const4pbx.UC_PICKUP_CALL_REQ:
				data.type = Const4pbx.UC_TYPE_GROUPWARE;
				data.extension = msg.extension;
				data.callee = msg.peer;
				break;
		}
		
		return data;
	}
	
	class Timer_Elapsed extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			regist();
		}
	}
}
