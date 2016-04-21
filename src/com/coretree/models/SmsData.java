package com.coretree.models;

import java.nio.ByteOrder;
import com.coretree.core.SetGetBytes;

public class SmsData extends SetGetBytes<Object>
{
	private byte cmd;
	private byte direct;
	private byte type;
	private byte status;
	private char[] from_ext = new char[16]; 
	private char[] to_ext = new char[16];
	private char[] userid = new char[16];
	private char[] senderphone = new char[20];
	private char[] receiverphones = new char[256];
	private char[] message = new char[84];
	private char[] reservetime = new char[32];
	
	private String[] rphones;

	private int len = 444;
	
	public void setCmd(byte cmd) { this.cmd = cmd; }
	public byte getCmd() { return this.cmd; }

	public void setDirect(byte direct) { this.direct = direct; }
	public byte getDirect() { return this.direct; }

	public void setType(byte type) { this.type = type; }
	public byte getType() { return this.type; }

	public void setStatus(byte status) { this.type = status; }
	public byte getStatus() { return this.status; }

	public void setFrom_ext(String from_ext) {
		for (int i = 0 ; i < from_ext.length() ; i++) {
			this.from_ext[i] = from_ext.charAt(i);
		}
	}
	public String getFrom_ext() { return new String(this.from_ext).trim(); }
	
	public void setTo_ext(String to_ext) {
		for (int i = 0 ; i < to_ext.length() ; i++) {
			this.to_ext[i] = to_ext.charAt(i);
		}
	}
	public String getTo_ext() { return new String(this.to_ext).trim(); }
	
	public void setUserid(String userid) {
		for (int i = 0 ; i < userid.length() ; i++) {
			this.userid[i] = userid.charAt(i);
		}
	}
	public String getUserid() { return new String(this.userid).trim(); }
	
	public void setSenderphone(String senderphone) {
		for (int i = 0 ; i < senderphone.length() ; i++) {
			this.senderphone[i] = senderphone.charAt(i);
		}
	}
	public String getSenderphone() { return new String(this.senderphone).trim(); }
	
	public void setReceiverphones(String receiverphones) {
//		for (int j = 0 ; j < this.receiverphones.length ; j++) {
//			this.receiverphones[j] = '\0';
//		}
		
		for (int i = 0 ; i < receiverphones.length() ; i++) {
			this.receiverphones[i] = receiverphones.charAt(i);
		}
	}
	public String getReceiverphones() { return new String(this.receiverphones).trim(); }
	
	public void setMessage(String message) {
		for (int i = 0 ; i < message.length() ; i++) {
			this.message[i] = message.charAt(i);
		}
	}
	public String getMessage() { return new String(this.message).trim(); }
	
	public void setReservetime(String reservetime) {
		for (int i = 0 ; i < reservetime.length() ; i++) {
			this.reservetime[i] = reservetime.charAt(i);
		}
	}
	public String getReservetime() { return new String(this.reservetime).trim(); }
	
	public void setRphones() {
		this.rphones = new String(this.receiverphones).trim().split(",");
	}
	public String[] getRphones() {
		if (this.rphones == null) {
			this.rphones = new String(this.receiverphones).trim().split(",");
		}
		return this.rphones;
	}
	
	public SmsData(){}
    
    public SmsData(ByteOrder byteorder)
    {
    	this.SetByteOrder(byteorder);
    }
    
	public SmsData(byte[] buff, ByteOrder byteorder)
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
		
		bytes = object2Bytes(from_ext);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(to_ext);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(userid);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(senderphone);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(receiverphones);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(message);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(reservetime);
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
		this.from_ext = (char[])bytes2Object(this.from_ext, rcv, tlength, this.from_ext.length);
		tlength += from_ext.length;
		this.to_ext = (char[])bytes2Object(this.to_ext, rcv, tlength, this.to_ext.length);
		tlength += to_ext.length;
		this.userid = (char[])bytes2Object(this.userid, rcv, tlength, this.userid.length);
		tlength += userid.length;
		this.senderphone = (char[])bytes2Object(this.senderphone, rcv, tlength, this.senderphone.length);
		tlength += senderphone.length;
		this.receiverphones = (char[])bytes2Object(this.receiverphones, rcv, tlength, this.receiverphones.length);
		tlength += receiverphones.length;
		this.message = (char[])bytes2Object(this.message, rcv, tlength, this.message.length);
		tlength += message.length;		
		this.reservetime = (char[])bytes2Object(this.reservetime, rcv, tlength, this.reservetime.length);
		tlength += reservetime.length;		
	}
	
	@Override
	public String toString() {
		return "SmsMsg [cmd=" + getCmd() + ", direct=" + getDirect() + ", type=" + getType() + ", status=" + getStatus()
		+ ", from_ext=" + getFrom_ext() + ", to_ext=" + getTo_ext() + ", userid=" + getUserid() + ", senderphone=" + getSenderphone()
		+ ", receiverphones=" + getReceiverphones() + ", message=" + getMessage() + ", reservetime=" + getReservetime() + ", rphones.length=" + getRphones().length + "]";
	}
}
