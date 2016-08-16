package com.coretree.models;

public class UcMessage {
	public int cmd;
	public byte direct;
	public long call_idx;
	public String extension;
	public String cust_no;
	public String caller;
	public String callername;
	public String callee;
	public String calleename;
	public int responseCode;
	public String unconditional;
	public String status;
/*
	public void setCmd(byte cmd) { this.cmd = cmd; }
	public byte getCmd() { return this.cmd; }
	
	public void setDirect(byte direct) { this.direct = direct; }
	public byte getDirect() { return this.direct; }
	
	public void setCall_idx(long call_idx) { this.call_idx = call_idx; }
	public long getCall_idx() { return this.call_idx; }
	
	public void setExtension(String extension) { this.extension = extension; }
	public String getExtension() { return this.extension; }
	
	public void setCust_idx(long cust_idx) { this.cust_idx = cust_idx; }
	public long getCust_idx() { return this.cust_idx; }
	
	public void setCaller(String caller) { this.caller = caller; }
	public String getCaller() { return this.caller; }
	
	public void setCallername(String callername) { this.callername = callername; }
	public String getCallername() { return this.callername; }
	
	public void setCallee(String callee) { this.callee = callee; }
	public String getCallee() { return this.callee; }
	
	public void setCalleename(String calleename) { this.calleename = calleename; }
	public String getCalleename() { return this.calleename; }
	
	public void setResponseCode(int responseCode) { this.responseCode = responseCode; }
	public int getResponseCode() { return this.responseCode; }
	
	public void setUnconditional(String unconditional) { this.unconditional = unconditional; }
	public String getUnconditional() { return this.unconditional; }
	
	public void setNoanswer(String noanswer) { this.noanswer = noanswer; }
	public String getNoanswer() { return this.noanswer; }
	
	public void setBusy(String busy) { this.busy = busy; }
	public String getBusy() { return this.busy; }
	
	public void setStatus(byte status) { this.status = status; }
	public byte getStatus() { return this.status; }
*/
	
	@Override
	public String toString() {
		return "UcMessage [cmd=" + cmd + ", direct=" + direct + ", extension=" + extension + ", call_idx=" + call_idx + ", cust_no=" + cust_no
		+ ", caller=" + caller + ", callername=" + callername + ", callee=" + callee + ", calleename=" + calleename
		+ ", responseCode=" + responseCode + ", unconditional=" + unconditional + ", status=" + status + "]";
	}
}
