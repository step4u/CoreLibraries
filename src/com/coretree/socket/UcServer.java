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
import com.coretree.models.SmsMsg;
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
	private int localport = 31002;
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
			serverSocket = new DatagramSocket(localport);
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
	
	public void Send(SmsMsg msg) throws UnknownHostException
	{
		byte[] sendData = msg.toBytes();
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
		
		try {
			serverSocket.send(sendPacket);
			System.out.println("");
			System.err.println(String.format("SMS Has sent %s", msg.toString()));
		} catch (IOException e) {
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
		data.setCmd((byte)msg.cmd);
		
		switch (msg.cmd)
		{
			case Const4pbx.UC_REGISTER_REQ:
				data.setType(Const4pbx.UC_TYPE_GROUPWARE);
				data.setIp();
				data.setPort(localport);
				break;
			case Const4pbx.UC_UNREGISTER_REQ:
				data.setType(Const4pbx.UC_TYPE_GROUPWARE);
				break;
			case Const4pbx.UC_BUSY_EXT_REQ:
				break;
			case Const4pbx.UC_INFO_SRV_REQ:
				data.setExtension(msg.extension);
				break;
			case Const4pbx.UC_MAKE_CALL_REQ:
				data.setType(Const4pbx.UC_TYPE_GROUPWARE);
				// data.setDirect(Const4pbx.UC_DIRECT_OUTGOING);
				data.setExtension(msg.extension);
				data.setCaller(msg.caller);
				data.setCallee(msg.callee);
				break;
			case Const4pbx.UC_DROP_CALL_REQ:
				data.setType(Const4pbx.UC_TYPE_GROUPWARE);
				data.setExtension(msg.extension);
				data.setCaller(msg.caller);
				data.setCallee(msg.callee);
				break;
			case Const4pbx.UC_HOLD_CALL_REQ:
			case Const4pbx.UC_ACTIVE_CALL_REQ:
				data.setType(Const4pbx.UC_TYPE_GROUPWARE);
				data.setExtension(msg.extension);
				data.setCaller(msg.caller);
				data.setCallee(msg.callee);
				break;
			case Const4pbx.UC_TRANSFER_CALL_REQ:
				data.setType(Const4pbx.UC_TYPE_GROUPWARE);
				data.setExtension(msg.extension);
				data.setCaller(msg.caller);
				data.setCallee(msg.callee);
				data.setUnconditional(msg.unconditional);
				break;
			case Const4pbx.UC_PICKUP_CALL_REQ:
				data.setType(Const4pbx.UC_TYPE_GROUPWARE);
				data.setExtension(msg.extension);
				data.setCallee(msg.caller);
				data.setCallee(msg.callee);
				break;
			case Const4pbx.UC_SET_SRV_REQ:
				data.setExtension(msg.extension);
				data.setResponseCode(msg.responseCode);
				if (data.getResponseCode() == Const4pbx.UC_SRV_UNCONDITIONAL) {
					data.setUnconditional(msg.unconditional);
				} else if (data.getResponseCode() == Const4pbx.UC_SRV_NOANSWER) {
					data.setNoanswer(msg.unconditional);
				} else if (data.getResponseCode() == Const4pbx.UC_SRV_BUSY) {
					data.setBusy(msg.unconditional);
				} else if (data.getResponseCode() == Const4pbx.UC_SRV_DND) {
					data.setDnD(Const4pbx.UC_DND_SET);
				}
				break;
			case Const4pbx.UC_CLEAR_SRV_REQ:
				data.setExtension(msg.extension);
				data.setResponseCode(msg.responseCode);
				break;
			case Const4pbx.UC_ANSWER_CALL_REQ:
				data.setExtension(msg.extension);
				break;
			case Const4pbx.WS_VALUE_EXTENSION_STATE_ONLINE:
				data.setCmd(Const4pbx.UC_CLEAR_SRV_REQ);
				data.setExtension(msg.extension);
				data.setResponseCode(msg.responseCode);
				break;
			case Const4pbx.WS_VALUE_EXTENSION_STATE_LEFT:
			case Const4pbx.WS_VALUE_EXTENSION_STATE_DND:
			case Const4pbx.WS_VALUE_EXTENSION_STATE_REDIRECTED:
				data.setCmd(Const4pbx.UC_SET_SRV_REQ);
				data.setExtension(msg.extension);
				data.setResponseCode(msg.responseCode);
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
