package com.zoonal.chatting.core;

import java.io.Serializable;


public class Message implements Serializable {
	
	private static final long	serialVersionUID	= -9039112709003398306L;
	private User sender;
	private String message;
	
	public Message(User sender, String message) {
		this.sender = sender;
		this.message = message;
	}
	
	public User getSender() {
		return sender;
	}
	
	public String getMessage() {
		return message;
	}
	
}
