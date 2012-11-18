package com.zoonal.chatting.core;


public class ChatEventObject {
	
	private String event;
	private Object[] data;
	
	public ChatEventObject(String event, Object[] data) {
		this.event = event;
		this.data = data;
	}
	
	public String getEvent() {
		return event;
	}
	
	public Object[] getData() {
		return data;
	}
	
}
