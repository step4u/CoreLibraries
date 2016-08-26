package com.coretree.event;

import java.util.ArrayList;

public final class Event<TEventArgs extends EventArgs>
{
	private ArrayList<IEventHandler<TEventArgs>> observerList = new ArrayList<IEventHandler<TEventArgs>>();
	private ArrayList<IEventHandler2<TEventArgs>> observerList2 = new ArrayList<IEventHandler2<TEventArgs>>();
	
	// Raise Event
	public void raiseEvent(Object sender, TEventArgs e) {
		for(IEventHandler<TEventArgs> handler : this.observerList) {
			handler.eventReceived(sender, e);
		}
	}
	
	public void raiseEvent2(Object sender, TEventArgs e) {
		for(IEventHandler2<TEventArgs> handler : this.observerList2) {
			handler.EventReceived2(sender, e);
		}
	}
	
	// Add Event Handler
	public void addEventHandler(IEventHandler<TEventArgs> handler) {
		this.observerList.add(handler);			
	}
	
	public void addEventHandler(IEventHandler2<TEventArgs> handler) {
		this.observerList2.add(handler);
	}
	
	// Remove Event Handler
	public void removeEventHandler(IEventHandler<TEventArgs> handler) {
		this.observerList.remove(handler);
	}
	
	public void removeEventHandler2(IEventHandler2<TEventArgs> handler) {
		this.observerList2.remove(handler);
	}
}
