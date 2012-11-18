package com.zoonal.chatting.core;

import java.io.Serializable;

import android.graphics.Bitmap;

public class User implements Serializable {
	
	private static final long	serialVersionUID	= -7282868455397496496L;
	private String username;
	private String avatarUrl;
	private Bitmap avatar;
	
	public User(String username, String avatarUrl) {
		this.username  = username;
		this.avatarUrl = avatarUrl;
	}
	
	public User(String username, String avatarUrl, Bitmap avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}
	
	public Bitmap getAvatarBitmap() {
		return avatar;
	}
	
	public void setAvatarBitmap(Bitmap avatar) {
		this.avatar = avatar;
	}

}
