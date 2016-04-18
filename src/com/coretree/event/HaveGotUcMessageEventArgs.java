package com.coretree.event;

public class HaveGotUcMessageEventArgs extends EventArgs
{
	private byte[] item = null;
	
	public HaveGotUcMessageEventArgs(byte[] item) {
		this.item = item;
	}
	
	public HaveGotUcMessageEventArgs(String msg)
	{
		// TODO Auto-generated constructor stub
	}

	public byte[] getItem()
	{
		return this.item;
	}
}
