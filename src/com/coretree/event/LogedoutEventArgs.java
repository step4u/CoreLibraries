package com.coretree.event;

import com.coretree.models.Organization;

public class LogedoutEventArgs extends EventArgs
{
	private Organization item = null;
	
	public LogedoutEventArgs(Organization _item) {
		this.item = _item;
	}
	
	public LogedoutEventArgs(String msg)
	{
		// TODO Auto-generated constructor stub
	}

	public Organization getEnfOfCallInstance()
	{
		return this.item;
	}
}
