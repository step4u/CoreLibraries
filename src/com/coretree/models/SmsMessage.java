package com.coretree.models;

public class SmsMessage {
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
}
