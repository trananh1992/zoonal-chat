package com.zoonal.chatting.core;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatSession implements Serializable {
	
	private static final long serialVersionUID	= 21640740174850394L;
	public static final int TYPE_SINGLE = 1;
	public static final int TYPE_ROOM = 2;
	
	private int type;
	private String groupId;
	private ArrayList<Message> messages;
	private ArrayList<User> users;
	
	public ChatSession(int type) {
		this.type = type;
		messages = new ArrayList<Message>();
		users = new ArrayList<User>();
	}
	
	public String getGroupId() {
		return groupId;
	}
	
	public int getType() {
		return type;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}

}
