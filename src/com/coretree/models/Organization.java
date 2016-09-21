package com.coretree.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

import com.coretree.event.Event;
import com.coretree.event.LogedoutEventArgs;
import com.coretree.util.Const4pbx;

public class Organization {
	public Event<LogedoutEventArgs> LogedoutEventHandler = new Event<LogedoutEventArgs>();
	
	private Timer timer;
	private int timerInterval = 1000 * 60 * 2;
	private int limited_time = 1000 * 60 * 30;
	
	public Organization() {
		Timer_Elapsed timer_elapsed = new Timer_Elapsed();
		timer = new Timer();
		timer.schedule(timer_elapsed, timerInterval, timerInterval);
		
		this.setStartdate(LocalDateTime.now());
	}
	
	class Timer_Elapsed extends TimerTask
	{
		@Override
		public void run()
		{
//			if (agentStatCd.equals(String.valueOf(Const4pbx.WS_VALUE_EXTENSION_STATE_BUSY))
//					|| agentStatCd.equals(String.valueOf(Const4pbx.WS_VALUE_EXTENSION_STATE_EDU))
//					|| agentStatCd.equals(String.valueOf(Const4pbx.WS_VALUE_EXTENSION_STATE_LEFT))
//					) {
//				
//			}
			
			LocalDateTime rightnow = LocalDateTime.now();
			
			long diffseconds = getStartdate().until(rightnow, ChronoUnit.SECONDS);
			
			if (diffseconds >= limited_time) {
				setAgentStatCd(String.valueOf(Const4pbx.WS_VALUE_EXTENSION_STATE_LOGEDOUT));
				LogedoutEventHandler.raiseEvent(this, new LogedoutEventArgs(""));
			}
		}
	}
	
	private String empNo;
	private String empNm;
	private String password;
	private String grpCd;
	private String enterDate;
	private String authCd;
	private String mobilePhoneNo;
	private String emailId;
	private String extensionNo;
	private String note;
	private String agentStatCd;
	private String prevAgentStatCd;
	private String newPwd;
	private int existCount;

	private int tempval = -1;
	private String tempstr;
	private LocalDateTime startdate;
	private LocalDateTime prevstartdate;
	private String sdate;
	private String shms;
	private String pdate;
	private String phms;
	
	
	public String getEmpNo() { return empNo; }
	public void setEmpNo(String empNo) { this.empNo = empNo; }

	public String getEmpNm() { return empNm; }
	public void setEmpNm(String empNm) { this.empNm = empNm; }
	
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
	public String getGrpCd() { return grpCd; }
	public void setGrpCd(String grpCd) { this.grpCd = grpCd; }
	
	public String getEnterDate() { return enterDate; }
	public void setEnterDate(String enterDate) { this.enterDate = enterDate; }
	
	public String getAuthCd() { return authCd; }
	public void setAuthCd(String authCd) { this.authCd = authCd; }
	
	public String getMobilePhoneNo() { return mobilePhoneNo; }
	public void setMobilePhoneNo(String mobilePhoneNo) { this.mobilePhoneNo = mobilePhoneNo; }
	
	public String getEmailId() { return emailId; }
	public void setEmailId(String emailId) { this.emailId = emailId; }
	
	public String getExtensionNo() { return extensionNo; }
	public void setExtensionNo(String extensionNo) { this.extensionNo = extensionNo; }
	
	public String getNote() { return note; }
	public void setNote(String note) { this.note = note; }
	
	public String getAgentStatCd() { return this.agentStatCd; }
	public void setAgentStatCd(String agentStatCd) { this.agentStatCd = agentStatCd; }
	
	public String getPrevAgentStatCd() { return this.prevAgentStatCd; }
	public void setPrevAgentStatCd(String prevAgentStatCd) { this.prevAgentStatCd = prevAgentStatCd; }
	
	public String getNewPwd() { return newPwd; }
	public void setNewPwd(String newPwd) { this.newPwd = newPwd; }
	
	public int getExistCount() { return existCount; }
	public void setExistCount(int existCount) { this.existCount = existCount; }
	
	public int getTempval() { return tempval; }
	public void setTempval(int tempval) { this.tempval = tempval; }
	
	public String getTempstr() { return tempstr; }
	public void setTempstr(String tempstr) { this.tempstr = tempstr; }
	
	public LocalDateTime getStartdate() { return startdate; }
	public void setStartdate(LocalDateTime startdate) {
		this.startdate = startdate;
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		this.setSdate(this.startdate.format(df));
		df = DateTimeFormatter.ofPattern("HHmmss");
		this.setShms(this.startdate.format(df));
	}
	
	public String getSdate() { return this.sdate; }
	public void setSdate(String sdate) { this.sdate = sdate; }
	
	public String getShms() { return this.shms; }
	public void setShms(String shms) { this.shms = shms; }
	
	public LocalDateTime getPrevStartdate() { return prevstartdate; }
	public void setPrevStartdate(LocalDateTime prevstartdate) {
		this.prevstartdate = prevstartdate;
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		this.setPdate(this.prevstartdate.format(df));
		df = DateTimeFormatter.ofPattern("HHmmss");
		this.setPhms(this.prevstartdate.format(df));
	}
	
	public String getPdate() { return this.pdate; }
	public void setPdate(String pdate) { this.pdate = pdate; }
	
	public String getPhms() { return this.phms; }
	public void setPhms(String phms) { this.phms = phms; }
	
	@Override
	public String toString() {
		return "Organization [empNo=" + empNo + ", empNm=" + empNm + ", password=" + password
				+ ", authCd=" + authCd + ", emailId=" + emailId + ", extensionNo=" + extensionNo
				+ ", note=" + note + ", agentStatCd=" + agentStatCd + ", prevAgentStatCd=" + prevAgentStatCd + ", newPwd=" + newPwd
				+ ", existCount=" + existCount+ ", sdate=" + sdate + ", shms=" + shms + ", pdate=" + pdate + ", phms=" + phms + "]";
	}
}
