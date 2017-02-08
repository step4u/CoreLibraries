package com.coretree.models;

public class HappyTalkResponse {
	public String msgid;
	public String result;
	public String code;
	public String error;
	public String kind;
	public String sendtime;
	
	@Override
	public String toString() {
		return "HappyTalkResponse [msgid=" + msgid + ", result=" + result + ", code=" + code + ", error=" + error + ", kind=" + kind + ", sendtime=" + sendtime + "]";
	}
}
