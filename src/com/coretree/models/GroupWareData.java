package com.coretree.models;

import java.io.UnsupportedEncodingException;
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
	private byte[] caller = new byte[16];
	private byte[] callee = new byte[16];
	private byte[] extension = new byte[5];
	private byte[] dummy0 = new byte[3];
    private int responseCode;
    private int ip;
    private int port;
    private byte[] unconditional = new byte[16];
    private byte[] noanswer = new byte[16];
    private byte[] busy = new byte[16];
    private byte DnD;
    private byte[] UserAgent = new byte[10];
    private byte[] InputData = new byte[20];
    private byte InputDataType;
    private int pList;
    private int	plvrKey;
    private byte InputDataResult;
    private byte[] dummy1 = new byte[3];
    private int StartCallSec;
    private int StartCallUSec;
    private byte[] dummy = new byte[12];
    
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
		try {
			byte[] strbuff = caller.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.caller.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.caller.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.caller[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
/*		
		for (int i = 0 ; i < caller.length() ; i++) {
			this.caller[i] = caller.charAt(i);
		}
*/		
	}
	public String getCaller() { return new String(this.caller).trim(); }

	public void setCallee(String callee) {
		try {
			byte[] strbuff = callee.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.callee.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.callee.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.callee[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
/*		
		for (int i = 0 ; i < callee.length() ; i++) {
			this.callee[i] = callee.charAt(i);
		}
*/		
	}
	public String getCallee() { return new String(this.callee).trim(); }
	
	public void setExtension(String extension) {
		try {
			byte[] strbuff = extension.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.extension.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.extension.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.extension[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
/*		
		for (int i = 0 ; i < extension.length() ; i++) {
			this.extension[i] = extension.charAt(i);
		}
*/		
	}
	public String getExtension() { return new String(this.extension).trim(); }

	public void setDummy0(byte[] dummy0) { this.dummy0 = dummy0; }
	public byte[] getDummy0() { return this.dummy0; }
	
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
		try {
			byte[] strbuff = unconditional.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.unconditional.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.unconditional.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.unconditional[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

/*		
		for (int i = 0 ; i < unconditional.length() ; i++) {
			this.unconditional[i] = unconditional.charAt(i);
		}
*/		
	}
	public String getUnconditional() { return new String(this.unconditional).trim(); }
	
	public void setNoanswer(String noanswer) {
		try {
			byte[] strbuff = noanswer.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.noanswer.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.noanswer.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.noanswer[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

/*
		for (int i = 0 ; i < noanswer.length() ; i++) {
			this.noanswer[i] = noanswer.charAt(i);
		}
*/
	}
	public String getNoanswer() { return new String(this.noanswer).trim(); }

	public void setBusy(String busy) {
		try {
			byte[] strbuff = busy.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.busy.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.busy.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.busy[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

/*
		for (int i = 0 ; i < busy.length() ; i++) {
			this.busy[i] = busy.charAt(i);
		}
*/
	}
	public String getBusy() { return new String(this.busy).trim(); }

	public void setDnD(byte DnD) { this.DnD = DnD; }
	public byte getDnD() { return this.DnD; }
	
	public void setUserAgent(String UserAgent) {
		try {
			byte[] strbuff = UserAgent.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.UserAgent.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.UserAgent.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.UserAgent[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
/*
		for (int i = 0 ; i < UserAgent.length() ; i++) {
			this.UserAgent[i] = UserAgent.charAt(i);
		}
*/
	}
	public String getUserAgent() { return new String(this.UserAgent).trim(); }
	
	public void setInputData(String InputData) {
		try {
			byte[] strbuff = InputData.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.InputData.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.InputData.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.InputData[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getInputData() { return new String(this.InputData).trim(); }
	
	public void setInputDataType(byte InputDataType) { this.InputDataType = InputDataType; }
	public byte getInputDataType() { return this.InputDataType; }
	
	public void setpList(byte pList) { this.pList = pList; }
	public int getpList() { return this.pList; }
	
	public void setplvrKey(byte plvrKey) { this.plvrKey = plvrKey; }
	public int getplvrKey() { return this.plvrKey; }
	
	public void setInputDataResult(byte InputDataResult) { this.InputDataResult = InputDataResult; }
	public byte getInputDataResult() { return this.InputDataResult; }
	
	public void setDummy1(String dummy1) {
		try {
			byte[] strbuff = dummy1.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.dummy1.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.dummy1.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.dummy1[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getDummy1() { return new String(this.dummy1).trim(); }
	
	public void setStartCallSec(byte StartCallSec) { this.StartCallSec = StartCallSec; }
	public int getStartCallSec() { return this.StartCallSec; }
	
	public void setStartCallUSec(byte StartCallUSec) { this.StartCallUSec = StartCallUSec; }
	public int getStartCallUSec() { return this.StartCallUSec; }
	
	public void setDummy(String dummy) {
		try {
			byte[] strbuff = dummy.getBytes("EUC-KR");
			
			int lcount = 0;
			if (strbuff.length < this.dummy.length) {
				lcount = strbuff.length;
			} else {
				lcount = this.dummy.length;
			}
			
			for (int i = 0 ; i < lcount ; i++) {
				this.dummy[i] = strbuff[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getDummy() { return new String(this.dummy).trim(); }
	

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
		byte[] bytes = null;
		
		out[tlength] = this.cmd;
		tlength += 1;
		
		out[tlength] = this.direct;
		tlength += 1;
		
		out[tlength] = this.type;
		tlength += 1;
		
		out[tlength] = this.status;
		tlength += 1;
		
		System.arraycopy(caller, 0, out, tlength, caller.length);
		tlength += caller.length;
		
		System.arraycopy(callee, 0, out, tlength, callee.length);
		tlength += callee.length;
		
		System.arraycopy(extension, 0, out, tlength, extension.length);
		tlength += extension.length;
		
		// dummy space
		System.arraycopy(dummy0, 0, out, tlength, dummy0.length);
		tlength += dummy0.length;
		
		bytes = object2Bytes(responseCode);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(ip);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(port);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		System.arraycopy(this.unconditional, 0, out, tlength, this.unconditional.length);
		tlength += this.unconditional.length;
		
		System.arraycopy(this.noanswer, 0, out, tlength, this.noanswer.length);
		tlength += this.noanswer.length;
		
		System.arraycopy(this.busy, 0, out, tlength, this.busy.length);
		tlength += this.busy.length;
		
		out[tlength] = this.DnD;
		tlength += 1;
		
		System.arraycopy(this.UserAgent, 0, out, tlength, this.UserAgent.length);
		tlength += this.UserAgent.length;
		
		System.arraycopy(this.InputData, 0, out, tlength, this.InputData.length);
		tlength += this.InputData.length;
		
		out[tlength] = this.InputDataType;
		tlength += 1;
		
		bytes = object2Bytes(this.pList);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(this.plvrKey);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;

		out[tlength] = this.InputDataResult;
		tlength += 1;
		
		// dummy space
		System.arraycopy(dummy1, 0, out, tlength, dummy1.length);
		tlength += dummy1.length;
		
		bytes = object2Bytes(this.StartCallSec);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(this.StartCallUSec);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		System.arraycopy(dummy, 0, out, tlength, dummy.length);
		tlength += this.dummy.length;
		
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
		
		System.arraycopy(rcv, tlength, this.caller, 0, this.caller.length);
		tlength += this.caller.length;

		System.arraycopy(rcv, tlength, this.callee, 0, this.callee.length);
		tlength += this.callee.length;

		System.arraycopy(rcv, tlength, this.extension, 0, this.extension.length);
		tlength += this.extension.length;

		System.arraycopy(rcv, tlength, this.dummy0, 0, this.dummy0.length);
		tlength += this.dummy0.length;

		this.responseCode = (int)bytes2Object(this.responseCode, rcv, tlength, 4);
		tlength += 4;
		
		this.ip = (int)bytes2Object(this.ip, rcv, tlength, 4);
		tlength += 4;
		
		this.port = (int)bytes2Object(this.port, rcv, tlength, 4);
		tlength += 4;
		
		System.arraycopy(rcv, tlength, this.unconditional, 0, this.unconditional.length);
		tlength += this.unconditional.length;

		System.arraycopy(rcv, tlength, this.noanswer, 0, this.noanswer.length);
		tlength += this.noanswer.length;

		System.arraycopy(rcv, tlength, this.busy, 0, this.busy.length);
		tlength += this.busy.length;
		
		this.DnD = rcv[tlength];
		tlength += 1;
		
		System.arraycopy(rcv, tlength, this.UserAgent, 0, this.UserAgent.length);
		tlength += this.UserAgent.length;
		
		System.arraycopy(rcv, tlength, this.InputData, 0, this.InputData.length);
		tlength += this.InputData.length;
		
		this.InputDataType = rcv[tlength];
		tlength += 1;
		
		this.pList = (int)bytes2Object(this.pList, rcv, tlength, 4);
		tlength += 4;
		
		this.plvrKey = (int)bytes2Object(this.plvrKey, rcv, tlength, 4);
		tlength += 4;
		
		this.InputDataResult = rcv[tlength];
		tlength += 1;
		
		System.arraycopy(rcv, tlength, this.dummy1, 0, this.dummy1.length);
		tlength += this.dummy1.length;
		
		this.StartCallSec = (int)bytes2Object(this.StartCallSec, rcv, tlength, 4);
		tlength += 4;
		
		this.StartCallUSec = (int)bytes2Object(this.StartCallUSec, rcv, tlength, 4);
		tlength += 4;

		System.arraycopy(rcv, tlength, this.dummy, 0, this.dummy.length);
		tlength += this.dummy.length;
	}
	
	@Override
	public String toString() {
		return "GroupWareData [cmd=" + getCmd() + ", direct=" + getDirect() + ", type=" + getType() + ", status=" + getStatus()
		+ ", caller=" + getCaller() + ", callee=" + getCallee() + ", extension=" + getExtension() + ", responseCode=" + getResponseCode()
		+ ", ip=" + getIp() + ", port=" + getPort() + ", unconditional=" + getUnconditional() + ", noanswer=" + getNoanswer()
		+ ", busy=" + getBusy() + ", DnD=" + getDnD() + ", UserAgent=" + getUserAgent() + ", InputData=" + getInputData() + ", InputDataType=" + getInputDataType()
		+ ", pList=" + getpList() + ", plvrKey=" + getplvrKey() + ", InputDataResult=" + getInputDataResult()
		+ ", StartCallSec=" + getStartCallSec() + ", StartCallUSec=" + getStartCallUSec() + "]";
	}
	
	private String intToIp(int i) {
        return 	( i        & 0xFF) + "." +
        		((i >>  8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ((i >> 24 ) & 0xFF);
    }
}
