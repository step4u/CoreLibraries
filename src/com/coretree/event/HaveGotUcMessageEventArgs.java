package com.coretree.event;

import com.coretree.models.GroupWareData;

public class HaveGotUcMessageEventArgs extends EventArgs
{
	private GroupWareData item = null;
	
	public HaveGotUcMessageEventArgs(GroupWareData item) {
		this.item = item;
	}
	
	public HaveGotUcMessageEventArgs(String msg)
	{
		// TODO Auto-generated constructor stub
	}

	public GroupWareData getEnfOfCallInstance()
	{
		return this.item;
	}
}
