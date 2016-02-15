package com.coretree.models;

import java.nio.ByteOrder;
import java.sql.Timestamp;

import com.coretree.core.SetGetBytes;

public class CDRData extends SetGetBytes<Object>
{
	private int seq;
	private byte[] office_name = new byte[40];
	private int start_yyyy;
	private int start_month;
	private int start_day;
	private int start_hour;
	private int start_min;
	private int start_sec;
	private int end_yyyy;
	private int end_month;
	private int end_day;
	private int end_hour;
	private int end_min;
	private int end_sec;
	private int caller_type;
	private int callee_type;
	private byte[] caller = new byte[20];
	private byte[] callee = new byte[20];
	private byte[] caller_ipn_number = new byte[20];
	private byte[] caller_group_code = new byte[5];
	private byte[] caller_group_name = new byte[31];
	private byte[] caller_human_name = new byte[20];
	private byte[] callee_ipn_number = new byte[20];
	private byte[] callee_group_code = new byte[5];
	private byte[] callee_group_name = new byte[31];
	private byte[] callee_human_name = new byte[20];
	private int result;
	private int next;
    
    public int getSeq(){ return this.seq; }
    public void setSeq(int seq) { this.seq = seq; }
    
    public String getOffice_name(){
/*    	String o = "";
    	try {
			o = new String(this.office_name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return o;
    	*/
    	return new String(this.office_name).trim();
    	}
    public void setOffice_name(byte[] office_name) { this.office_name = office_name; }
    
    public int getStart_yyyy(){ return this.start_yyyy; }
    public void setStart_yyyy(int start_yyyy) { this.start_yyyy = start_yyyy; }
    
    public int getStart_month(){ return this.start_month; }
    public void setStart_month(int start_month) { this.start_month = start_month; }
    
    public int getStart_day(){ return this.start_day; }
    public void setStart_day(int start_day) { this.start_day = start_day; }
    
    public int getStart_hour(){ return this.start_hour; }
    public void setStart_hour(int start_hour) { this.start_day = start_hour; }
    
    public int getStart_min(){ return this.start_min; }
    public void setStart_min(int start_min) { this.start_min = start_min; }
    
    public int getStart_sec(){ return this.start_sec; }
    public void setStart_sec(int start_sec) { this.start_sec = start_sec; }
    
    public int getEnd_yyyy(){ return this.end_yyyy; }
    public void setEnd_yyyy(int end_yyyy) { this.end_yyyy = end_yyyy; }
    
    public int getEnd_month(){ return this.end_month; }
    public void setEnd_month(int end_month) { this.end_month = end_month; }
    
    public int getEnd_day(){ return this.end_day; }
    public void setEnd_day(int end_day) { this.end_day = end_day; }
    
    public int getEnd_hour(){ return this.end_hour; }
    public void setEnd_hour(int end_hour) { this.end_hour = end_hour; }
    
    public int getEnd_min(){ return this.end_min; }
    public void setEnd_min(int end_min) { this.end_min = end_min; }
    
    public int getEnd_sec(){ return this.end_sec; }
    public void setEnd_sec(int end_sec) { this.end_sec = end_sec; }
    
    public int getCaller_type(){ return this.caller_type; }
    public void setCaller_type(int caller_type) { this.caller_type = caller_type; }
    
    public int getCallee_type(){ return this.callee_type; }
    public void setCallee_type(int callee_type) { this.callee_type = callee_type; }
    
    public String getCaller(){ return new String(this.caller).trim(); }
    public void setCaller(byte[] caller) { this.caller = caller; }
    
    public String getCallee(){ return new String(this.callee).trim(); }
    public void setCallee(byte[] callee) { this.callee = callee; }
    
    public String getCaller_ipn_number(){ return new String(this.caller_ipn_number).trim(); }
    public void setCaller_ipn_number(byte[] caller_ipn_number) { this.caller_ipn_number = caller_ipn_number; }
    
    public String getCaller_group_code(){ return new String(this.caller_group_code).trim(); }
    public void setCaller_group_code(byte[] caller_group_code) { this.caller_group_code = caller_group_code; }
    
    public String getCaller_group_name(){ return new String(this.caller_group_name).trim(); }
    public void setCaller_group_name(byte[] caller_group_name) { this.caller_group_name = caller_group_name; }
    
    public String getCaller_human_name(){ return new String(this.caller_human_name).trim(); }
    public void setCaller_human_name(byte[] caller_human_name) { this.caller_human_name = caller_human_name; }
    
    public String getCallee_ipn_number(){ return new String(this.callee_ipn_number).trim(); }
    public void setCallee_ipn_number(byte[] callee_ipn_number) { this.callee_ipn_number = callee_ipn_number; }
    
    public String getCallee_group_code(){ return new String(this.callee_group_code).trim(); }
    public void setCallee_group_code(byte[] callee_group_code) { this.callee_group_code = callee_group_code; }
    
    public String getCallee_group_name(){ return new String(this.callee_group_name).trim(); }
    public void setCallee_group_name(byte[] callee_group_name) { this.callee_group_name = callee_group_name; }
    
    public String getCallee_human_name(){ return new String(this.callee_human_name).trim(); }
    public void setCallee_human_name(byte[] callee_human_name) { this.callee_human_name = callee_human_name; }
    
    public int getResult(){ return this.result; }
    public void setResult(int result) { this.result = result; }
    
    public int getNext(){ return this.next; }
    public void setNext(int next) { this.next = next; }
    
    public Timestamp getStartdate() {
    	return Timestamp.valueOf(String.format("%d-%d-%d %d:%d:%d", getStart_yyyy(), getStart_month(), getStart_day(), getStart_hour(), getStart_min(), getStart_sec()));
    }
    
    public Timestamp getEnddate() {
    	return Timestamp.valueOf(String.format("%d-%d-%d %d:%d:%d", getEnd_yyyy(), getEnd_month(), getEnd_day(), getEnd_hour(), getEnd_min(), getEnd_sec()));
    }
    
    // private int len = 300;
    
    public CDRData(){}
    
    public CDRData(ByteOrder byteorder)
    {
    	this.SetByteOrder(byteorder);
    }
    
	public CDRData(byte[] buff, ByteOrder byteorder)
	{
		this.SetByteOrder(byteorder);
		this.toObject(buff);
	}

	@Override
	public byte[] toBytes()
	{
		return null;
	}

	@Override
	public void toObject(byte[] rcv)
	{
		int tlength = 0;
		this.seq = (int)bytes2Object(this.seq, rcv, tlength, 4);
		tlength += 4;
		//this.office_name = (byte[])bytes2Object(this.office_name, rcv, tlength, this.office_name.length);
		System.arraycopy(rcv, tlength, this.office_name, 0, this.office_name.length);
		tlength += this.office_name.length;
		this.start_yyyy = (int)bytes2Object(this.start_yyyy, rcv, tlength, 4);
		tlength += 4;
		this.start_month = (int)bytes2Object(this.start_month, rcv, tlength, 4);
		tlength += 4;
		this.start_day = (int)bytes2Object(this.start_day, rcv, tlength, 4);
		tlength += 4;
		this.start_hour = (int)bytes2Object(this.start_hour, rcv, tlength, 4);
		tlength += 4;
		this.start_min = (int)bytes2Object(this.start_min, rcv, tlength, 4);
		tlength += 4;
		this.start_sec = (int)bytes2Object(this.start_sec, rcv, tlength, 4);
		tlength += 4;
		this.end_yyyy = (int)bytes2Object(this.end_yyyy, rcv, tlength, 4);
		tlength += 4;
		this.end_month = (int)bytes2Object(this.end_month, rcv, tlength, 4);
		tlength += 4;
		this.end_day = (int)bytes2Object(this.end_day, rcv, tlength, 4);
		tlength += 4;
		this.end_hour = (int)bytes2Object(this.end_hour, rcv, tlength, 4);
		tlength += 4;
		this.end_min = (int)bytes2Object(this.end_min, rcv, tlength, 4);
		tlength += 4;
		this.end_sec = (int)bytes2Object(this.end_sec, rcv, tlength, 4);
		tlength += 4;
		this.caller_type = (int)bytes2Object(this.caller_type, rcv, tlength, 4);
		tlength += 4;
		this.callee_type = (int)bytes2Object(this.callee_type, rcv, tlength, 4);
		tlength += 4;
		System.arraycopy(rcv, tlength, this.caller, 0, this.caller.length);
		tlength += this.caller.length;
		System.arraycopy(rcv, tlength, this.callee, 0, this.callee.length);
		tlength += this.callee.length;
		System.arraycopy(rcv, tlength, this.caller_ipn_number, 0, this.caller_ipn_number.length);
		tlength += this.caller_ipn_number.length;
		System.arraycopy(rcv, tlength, this.caller_group_code, 0, this.caller_group_code.length);
		tlength += this.caller_group_code.length;
		System.arraycopy(rcv, tlength, this.caller_group_name, 0, this.caller_group_name.length);
		tlength += this.caller_group_name.length;
		System.arraycopy(rcv, tlength, this.caller_human_name, 0, this.caller_human_name.length);
		tlength += this.caller_human_name.length;
		System.arraycopy(rcv, tlength, this.callee_ipn_number, 0, this.callee_ipn_number.length);
		tlength += this.callee_ipn_number.length;
		System.arraycopy(rcv, tlength, this.callee_group_code, 0, this.callee_group_code.length);
		tlength += this.callee_group_code.length;
		System.arraycopy(rcv, tlength, this.callee_group_name, 0, this.callee_group_name.length);
		tlength += this.callee_group_name.length;
		System.arraycopy(rcv, tlength, this.callee_human_name, 0, this.callee_human_name.length);
		tlength += this.callee_human_name.length;
		this.result = (int)bytes2Object(this.result, rcv, tlength, 4);
		tlength += 4;
		this.next = (int)bytes2Object(this.next, rcv, tlength, 4);
		tlength += 4;
	}
	
	@Override
	public String toString()
	{
		return "CDRData [seq=" + seq + ", office_name=" + getOffice_name() + ", start_yyyy=" + getStart_yyyy() + ", start_month=" + getStart_month()
				+ ", start_day=" + getStart_day() + ", start_hour=" + getStart_hour() + ", start_min=" + getStart_min() + ", start_sec=" + getStart_sec()
				+ ", end_yyyy=" + getEnd_yyyy() + ", end_month=" + getEnd_month() + ", end_day=" + getEnd_day() + ", end_hour=" + getEnd_hour()
				+ ", end_min=" + getEnd_min() + ", end_sec=" + getEnd_sec() + ", caller_type=" + getCaller_type() + ", callee_type=" + getCallee_type()
				+ ", caller=" + getCaller() + ", callee=" + getCallee() + ", caller_ipn_number=" + getCaller_ipn_number() + ", caller_group_code=" + getCaller_group_code()
				+ ", caller_group_name=" + getCaller_group_name() + ", caller_human_name=" + getCaller_human_name() + ", callee_ipn_number=" + getCallee_ipn_number()
				+ ", callee_group_code=" + getCallee_group_code() + ", callee_group_name=" + getCallee_group_name() + ", callee_human_name=" + getCallee_human_name()
				+ ", result=" + getResult() + ", next=" + getNext() + "]";
	}
}
