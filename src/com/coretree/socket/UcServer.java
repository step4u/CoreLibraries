package com.coretree.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;

import com.coretree.event.Event;
import com.coretree.event.HaveGotUcMessageEventArgs;
import com.coretree.models.GroupWareData;
import com.coretree.models.SmsData;
import com.coretree.models.UcMessage;
import com.coretree.util.Const4pbx;

public class UcServer {
	public Event<HaveGotUcMessageEventArgs> HaveGotUcMessageEventHandler = new Event<HaveGotUcMessageEventArgs>();
	
	private ByteOrder byteorder = ByteOrder.BIG_ENDIAN;
	
	private DatagramSocket serverSocket;
	private InetSocketAddress remoteep;
	private Thread[] threads;
	private Timer timer;
	private int timerInterval = 30000;
	private int localport = 31002;
	// private Options _option;
	
	public ByteOrder GetByteorder() { return this.byteorder; }
	
	public UcServer() {
		this("127.0.0.1", 31001, 1, ByteOrder.BIG_ENDIAN);
	}
	
	public UcServer(String addr, ByteOrder order) {
		this(addr, 31001, 1, order);
	}
	
	public UcServer(String addr, int port, int threadcount, ByteOrder order) {
		try {
			byteorder = order;
			remoteep = new InetSocketAddress(addr, port);
			
			// InetSocketAddress address = new InetSocketAddress("localhost", 21003);
			serverSocket = new DatagramSocket(localport);
			serverSocket.connect(remoteep);
			
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					InitiateSocket();
				}
			};
			
			threads = new Thread[threadcount];
			
	        for (int i = 0; i < threads.length; i++) {
	        	threads[i] = new Thread(runnable);
	        	threads[i].start();
	        }
	        
	        Timer_Elapsed timer_elapsed = new Timer_Elapsed();
			timer = new Timer();
			timer.schedule(timer_elapsed, timerInterval, timerInterval);
			
			this.regist();
		} catch (Exception e) {
			System.err.println("An error has broken out in constructor.\n--->>" + e.getMessage());
		}
	}
	
//	public void start() {
//        for (int i = 0; i < threads.length; i++) {
//        	threads[i] = new Thread(this);
//        	threads[i].start();
//        }
//    }
	
	private void InitiateSocket() {
		try {
			// byte[] sendData = null;
			// byte[] receiveData = new byte[1024];
			// DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			byte[] receiveData = null;
			DatagramPacket receivePacket = null;

			while (true) {
				receiveData = new byte[1024];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				
				serverSocket.receive(receivePacket);
				
				// byte[] rcvbytes = receivePacket.getData();
				this.DataHasRecievedHandle(serverSocket, receivePacket.getData());
				
				receiveData = null;
				receivePacket = null;
			}
		} catch (PortUnreachableException e) {
			System.err.println("An error has broken out in InitiateSocket method2 .\n--->>" + e.getMessage());
			this.InitiateSocket();
			// return;
		} catch (Exception e) {
			System.err.println("An error has broken out in InitiateSocket method.\n--->>" + e.getMessage());
			// e.printStackTrace();
		}
	}
	
	public void Send(UcMessage msg) {
		try {
			GroupWareData data = this.GetData(msg);
			byte[] sendData = data.toBytes();

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
			// DatagramPacket sendPacket = new DatagramPacket(sendData, 0, sendData.length, remoteep);
			
			serverSocket.send(sendPacket);
			System.out.println("");
			System.err.println(String.format("Has sent %s", data.toString()));
		} catch (IOException e) {
			System.err.println("An error has broken out in Send method UcMessage.\n--->>" + e.getMessage());
		}
	}
	
	public void Send(SmsData msg) {
		try {
			byte[] sendData = msg.toBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
			// DatagramPacket sendPacket = new DatagramPacket(sendData, 0, sendData.length, remoteep);
			
			serverSocket.send(sendPacket);
			System.out.println("");
			System.err.println(String.format("SMS Has sent %s", msg.toString()));
		} catch (Exception e) {
			System.err.println("An error has broken out in Send method SmsData.\n--->>" + e.getMessage());
		}
	}
	
	public void Send(GroupWareData msg) {
		try {
			byte[] sendData = msg.toBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
			// DatagramPacket sendPacket = new DatagramPacket(sendData, 0, sendData.length, remoteep);
			
			serverSocket.send(sendPacket);
			System.out.println("");
			System.err.println(String.format("GroupWare has sent %s", msg.toString()));
		} catch (Exception e) {
			System.err.println("An error has broken out in Send method GroupWareData.\n--->>" + e.getMessage());
		}
	}
	
	public void Send(DatagramSocket sock, DatagramPacket packet, GroupWareData data) {
		try {
			byte[] sendData = data.toBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
			// DatagramPacket sendPacket = new DatagramPacket(sendData, 0, sendData.length, remoteep);
			
			sock.send(sendPacket);
			System.err.println("Normal has sent.");
		} catch (Exception e) {
			System.err.println("An error has broken out in Send method normal.\n--->>" + e.getMessage());
		}
	}
	
	private void DataHasRecievedHandle(DatagramSocket sock, byte[] bytes) {
		// GroupWareData data = new GroupWareData(bytes, byteorder);
		
		if (HaveGotUcMessageEventHandler != null)
			HaveGotUcMessageEventHandler.raiseEvent(this, new HaveGotUcMessageEventArgs(bytes));
	}
	
	public void regist() {
		UcMessage msg = new UcMessage();
		msg.cmd = Const4pbx.UC_REGISTER_REQ;
		this.Send(msg);
	}
	
	private GroupWareData GetData(UcMessage msg) {
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
				// data.setDirect(Const4pbx.UC_DIRECT_OUTGOING);
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
			case Const4pbx.WS_REQ_CHANGE_EXTENSION_STATE:
				switch (Integer.valueOf(msg.status)) {
					case Const4pbx.WS_VALUE_EXTENSION_STATE_AFTER:
					case Const4pbx.WS_VALUE_EXTENSION_STATE_LEFT:
					case Const4pbx.WS_VALUE_EXTENSION_STATE_REST:
					case Const4pbx.WS_VALUE_EXTENSION_STATE_EDU:
					case Const4pbx.WS_VALUE_EXTENSION_STATE_LOGEDON:
					case Const4pbx.WS_VALUE_EXTENSION_STATE_LOGEDOUT:
						data.setCmd(Const4pbx.UC_SET_SRV_REQ);
						data.setExtension(msg.extension);
						data.setResponseCode(msg.responseCode);
						// data.setUnconditional(msg.unconditional);
						break;
					case Const4pbx.WS_VALUE_EXTENSION_STATE_READY:
					default:
						data.setCmd(Const4pbx.UC_CLEAR_SRV_REQ);
						data.setExtension(msg.extension);
						data.setResponseCode(msg.responseCode);
						break;
				}
				break;
		}
		
		return data;
	}
	
	class Timer_Elapsed extends TimerTask {
		@Override
		public void run() {
			regist();
		}
	}
}
