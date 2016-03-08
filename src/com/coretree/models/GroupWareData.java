package com.coretree.models;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.coretree.core.SetGetBytes;

public class GroupWareData extends SetGetBytes<Object>
{
	private byte cmd;
	private byte direct;
	private byte type;
	private byte status;
	private char[] caller = new char[16]; 
	private char[] callee = new char[16];
	private char[] extension = new char[5];
	private byte[] dummy0 = new byte[3];
    private int responseCode;
    private int ip;
    private int port;
    private char[] unconditional = new char[16];
    private char[] noanswer = new char[16];
    private char[] busy = new char[16];
    private byte DnD;
    private char[] UserAgent = new char[10];
    private char[] dummy = new char[50];
    private byte[] dummy1 = new byte[3];
    
	private int len = 168;
	
	
	public void setCmd(byte cmd) { this.cmd = cmd; }
	public byte getCmd() { return this.cmd; }

	public void setDirect(byte direct) { this.direct = direct; }
	public byte getDirect() { return this.direct; }

	public void setType(byte type) { this.type = type; }
	public byte getType() { return this.type; }

	public void setStatus(byte status) { this.type = status; }
	public byte getStatus() { return this.status; }

	public void setCaller(String caller) {
		for (int i = 0 ; i < caller.length() ; i++) {
			this.caller[i] = caller.charAt(i);
		}
	}
	public String getCaller() { return new String(this.caller).trim(); }

	public void setCallee(String callee) {
		for (int i = 0 ; i < callee.length() ; i++) {
			this.callee[i] = callee.charAt(i);
		}
	}
	public String getCallee() { return new String(this.callee).trim(); }
	
	public void setExtension(String extension) {
		for (int i = 0 ; i < extension.length() ; i++) {
			this.extension[i] = extension.charAt(i);
		}
	}
	public String getExtension() { return new String(this.extension).trim(); }

	public void setDummy0(byte[] dummy0) { this.dummy0 = dummy0; }
	public char[] getDummy0() { return this.extension; }
	
	public void setResponseCode(int responseCode) { this.responseCode = responseCode; }
	public int getResponseCode() { return this.responseCode; }
	
	public void setIp() {
		InetAddress bar = null;
		try {
			// bar = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
			bar = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ByteBuffer b = ByteBuffer.wrap(bar.getAddress());
		if (byteorder == ByteOrder.BIG_ENDIAN)
			b.order(ByteOrder.LITTLE_ENDIAN);
		else
			b.order(ByteOrder.BIG_ENDIAN);
		
		this.ip = b.getInt();
		// System.out.println("GroupWare->setIp(): " + this.ip);
	}
	public String getIp() {
		return intToIp(this.ip);
	}
	
	public void setPort(int port) { this.port = port; }
	public int getPort() { return this.port; }
	
	public void setUnconditional(String unconditional) {
		for (int i = 0 ; i < unconditional.length() ; i++) {
			this.unconditional[i] = unconditional.charAt(i);
		}
	}
	public String getUnconditional() { return new String(this.unconditional).trim(); }
	
	public void setNoanswer(String noanswer) {
		for (int i = 0 ; i < noanswer.length() ; i++) {
			this.noanswer[i] = noanswer.charAt(i);
		}
	}
	public String getNoanswer() { return new String(this.noanswer).trim(); }

	public void setBusy(String busy) {
		for (int i = 0 ; i < busy.length() ; i++) {
			this.busy[i] = busy.charAt(i);
		}
	}
	public String getBusy() { return new String(this.busy).trim(); }

	public void setDnD(byte DnD) { this.DnD = DnD; }
	public byte getDnD() { return this.DnD; }
	
	
	public void setUserAgent(String UserAgent) {
		for (int i = 0 ; i < UserAgent.length() ; i++) {
			this.UserAgent[i] = UserAgent.charAt(i);
		}
	}
	public String getUserAgent() { return new String(this.UserAgent).trim(); }
	
	public void setDummy(String dummy) {
		for (int i = 0 ; i < dummy.length() ; i++) {
			this.dummy[i] = dummy.charAt(i);
		}
	}
	public String getDummy() { return new String(this.dummy).trim(); }
	
	public void setDummy1(byte[] dummy1) { this.dummy1 = dummy1; }
	public char[] getDummy1() { return this.extension; }
	

	public GroupWareData(){}
    
    public GroupWareData(ByteOrder byteorder)
    {
    	this.SetByteOrder(byteorder);
    }
    
	public GroupWareData(byte[] buff, ByteOrder byteorder)
	{
		this.SetByteOrder(byteorder);
		this.toObject(buff);
	}

	@Override
	public byte[] toBytes()
	{
		byte[] out = new byte[len];

		int tlength = 0;
		
		byte[] bytes = object2Bytes(cmd);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(direct);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(type);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(status);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(caller);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(callee);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(extension);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		// dummy space
		bytes = new byte[3];
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(responseCode);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(ip);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(port);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(unconditional);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(noanswer);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(busy);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(DnD);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(UserAgent);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(dummy);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		// dummy space
		bytes = new byte[3];
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		return out;
	}

	@Override
	public void toObject(byte[] rcv)
	{
		int tlength = 0;
		this.cmd = (byte)bytes2Object(this.cmd, rcv, tlength, 1);
		tlength += 1;
		this.direct = (byte)bytes2Object(this.direct, rcv, tlength, 1);
		tlength += 1;
		this.type = (byte)bytes2Object(this.type, rcv, tlength, 1);
		tlength += 1;
		this.status = (byte)bytes2Object(this.status, rcv, tlength, 1);
		tlength += 1;
		this.caller = (char[])bytes2Object(this.caller, rcv, tlength, this.caller.length);
		tlength += caller.length;
		this.callee = (char[])bytes2Object(this.callee, rcv, tlength, this.callee.length);
		tlength += callee.length;
		this.extension = (char[])bytes2Object(this.extension, rcv, tlength, this.extension.length);
		tlength += extension.length;
		this.dummy0 = (byte[])bytes2Object(this.dummy0, rcv, tlength, this.dummy0.length);
		tlength += dummy0.length;
		this.responseCode = (int)bytes2Object(this.responseCode, rcv, tlength, 4);
		tlength += 4;
		this.ip = (int)bytes2Object(this.ip, rcv, tlength, 4);
		tlength += 4;
		this.port = (int)bytes2Object(this.port, rcv, tlength, 4);
		tlength += 4;
		this.unconditional = (char[])bytes2Object(this.unconditional, rcv, tlength, this.unconditional.length);
		tlength += unconditional.length;
		this.noanswer = (char[])bytes2Object(this.noanswer, rcv, tlength, this.noanswer.length);
		tlength += noanswer.length;
		this.busy = (char[])bytes2Object(this.busy, rcv, tlength, this.busy.length);
		tlength += busy.length;
		this.DnD = (byte)bytes2Object(this.DnD, rcv, tlength, 1);
		tlength += 1;
		this.UserAgent = (char[])bytes2Object(this.UserAgent, rcv, tlength, this.UserAgent.length);
		tlength += UserAgent.length;
		this.dummy = (char[])bytes2Object(this.dummy, rcv, tlength, this.dummy.length);
		tlength += dummy.length;
	}
	
	@Override
	public String toString() {
		return "GroupWareData [cmd=" + getCmd() + ", direct=" + getDirect() + ", type=" + getType() + ", status=" + getStatus()
		+ ", caller=" + getCaller() + ", callee=" + getCallee() + ", extension=" + getExtension() + ", responseCode=" + getResponseCode()
		+ ", ip=" + getIp() + ", port=" + getPort() + ", unconditional=" + getUnconditional() + ", noanswer=" + getNoanswer()
		+ ", busy=" + getBusy() + ", DnD=" + getDnD() + "]";
	}
	
	private String intToIp(int i) {
        return 	( i        & 0xFF) + "." +
        		((i >>  8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ((i >> 24 ) & 0xFF);
    }
}
