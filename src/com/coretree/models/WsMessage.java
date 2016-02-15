package com.coretree.models;

public class WsMessage {
	public int cmd;
	public int value;
	
	@Override
	public String toString() {
		return "WsMessage [cmd=" + cmd + ", value=" + value + "]";
	}
}
