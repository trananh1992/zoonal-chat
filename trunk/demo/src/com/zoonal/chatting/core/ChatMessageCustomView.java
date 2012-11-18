package com.zoonal.chatting.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoonal.chatting.R;

public class ChatMessageCustomView extends LinearLayout {
	
	ImageView imageAvatar;
	TextView txtUserName;
	TextView txtMessage;
	
	public ChatMessageCustomView(Context context) {
		super(context);
		LayoutInflater li = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.list_message, this, true);

		imageAvatar = (ImageView) findViewById(R.id.imageView);	
		txtUserName = (TextView) findViewById(R.id.txtUserName);
		txtMessage = (TextView) findViewById(R.id.txtMessage);
	}

}
