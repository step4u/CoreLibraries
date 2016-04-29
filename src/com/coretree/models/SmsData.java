package com.coretree.models;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import com.coretree.core.SetGetBytes;

public class SmsData extends SetGetBytes<Object>
{
	private byte cmd;
	private byte direct;
	private byte type;
	private byte status;
	private byte[] from_ext = new byte[16]; 
	private byte[] to_ext = new byte[16];
	private byte[] userid = new byte[16];
	private byte[] senderphone = new byte[20];
	private byte[] receiverphones = new byte[256];
	private byte[] message = new byte[84];
	private byte[] reservetime = new byte[32];
	
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
		try {
			byte[] strbuff = from_ext.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.from_ext.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.from_ext.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.from_ext[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getFrom_ext() { return new String(this.from_ext).trim(); }
	
	public void setTo_ext(String to_ext) {
		try {
			byte[] strbuff = to_ext.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.to_ext.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.to_ext.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.to_ext[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getTo_ext() { return new String(this.to_ext).trim(); }
	
	public void setUserid(String userid) {
		try {
			byte[] strbuff = userid.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.userid.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.userid.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.userid[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getUserid() { return new String(this.userid).trim(); }
	
	public void setSenderphone(String senderphone) {
		try {
			byte[] strbuff = senderphone.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.senderphone.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.senderphone.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.senderphone[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getSenderphone() { return new String(this.senderphone).trim(); }
	
	public void setReceiverphones(String receiverphones) {
		try {
			byte[] strbuff = receiverphones.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.receiverphones.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.receiverphones.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.receiverphones[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getReceiverphones() { return new String(this.receiverphones).trim(); }
	
	public void setMessage(String message) {
		try {
			byte[] strbuff = message.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.message.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.message.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.message[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getMessage() {
		String out = null;
		try {
			out = new String(this.message, "EUC-KR").trim();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out;
	}
	
	public void setReservetime(String reservetime) {
		try {
			byte[] strbuff = reservetime.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.reservetime.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.reservetime.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.reservetime[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getReservetime() { return new String(this.reservetime).trim(); }
	
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
		byte[] bytes = null;
		
		out[tlength] = this.cmd;
		tlength += 1;
		
		out[tlength] = this.direct;
		tlength += 1;
		
		out[tlength] = this.type;
		tlength += 1;
		
		out[tlength] = this.status;
		tlength += 1;
		
		System.arraycopy(this.from_ext, 0, out, tlength, this.from_ext.length);
		tlength += this.from_ext.length;
		
		System.arraycopy(this.to_ext, 0, out, tlength, this.to_ext.length);
		tlength += this.to_ext.length;
		
		System.arraycopy(this.userid, 0, out, tlength, this.userid.length);
		tlength += this.userid.length;
		
		System.arraycopy(this.senderphone, 0, out, tlength, this.senderphone.length);
		tlength += this.senderphone.length;
		
		System.arraycopy(this.receiverphones, 0, out, tlength, this.receiverphones.length);
		tlength += this.receiverphones.length;
		
		System.arraycopy(this.message, 0, out, tlength, this.message.length);
		tlength += this.message.length;
		
		System.arraycopy(this.reservetime, 0, out, tlength, this.reservetime.length);
		tlength += this.reservetime.length;
		
		return out;
	}

	@Override
	public void toObject(byte[] rcv)
	{
		int tlength = 0;
		
		this.cmd = rcv[tlength];
		tlength += 1;
		
		this.direct = rcv[tlength];
		tlength += 1;

		this.type = rcv[tlength];
		tlength += 1;
		
		this.status = rcv[tlength];
		tlength += 1;
		
		System.arraycopy(rcv, tlength, this.from_ext, 0, this.from_ext.length);
		tlength += from_ext.length;

		System.arraycopy(rcv, tlength, this.to_ext, 0, this.to_ext.length);
		tlength += to_ext.length;

		System.arraycopy(rcv, tlength, this.userid, 0, this.userid.length);
		tlength += userid.length;

		System.arraycopy(rcv, tlength, this.senderphone, 0, this.senderphone.length);
		tlength += senderphone.length;

		System.arraycopy(rcv, tlength, this.receiverphones, 0, this.receiverphones.length);
		tlength += receiverphones.length;

		System.arraycopy(rcv, tlength, this.message, 0, this.message.length);
		tlength += message.length;

		System.arraycopy(rcv, tlength, this.reservetime, 0, this.reservetime.length);
		tlength += reservetime.length;		
	}
	
	@Override
	public String toString() {
		return "SmsMsg [cmd=" + getCmd() + ", direct=" + getDirect() + ", type=" + getType() + ", status=" + getStatus()
		+ ", from_ext=" + getFrom_ext() + ", to_ext=" + getTo_ext() + ", userid=" + getUserid() + ", senderphone=" + getSenderphone()
		+ ", receiverphones=" + getReceiverphones() + ", message=" + getMessage() + ", reservetime=" + getReservetime() + "]";
	}
}
