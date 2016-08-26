package com.coretree.event;

public interface IEventHandler2<TEventArgs extends EventArgs>
{
	public void EventReceived2(Object sender, TEventArgs e);
}
