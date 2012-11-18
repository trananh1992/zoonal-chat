package com.zoonal.chatting;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;

import com.zoonal.chatting.core.ChatSession;

public class ChatActivity extends FragmentActivity {
	
	private ChatManagerService.ChatManager chatManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = new Intent(this, ChatManagerService.class);
        this.bindService(intent, connection, Service.BIND_AUTO_CREATE);

        ChatSession session = (ChatSession) getIntent().getExtras()
        		.getSerializable("session");
        String tags = session.getType() == ChatSession.TYPE_ROOM
        		? session.getGroupId() : session.getUsers().get(0).getUsername();
        
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(getIntent().getExtras());
    	getSupportFragmentManager().beginTransaction()
    		.add(R.id.fragment_container, fragment, tags).commit();
    }
    
    @Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	public ChatManagerService.ChatManager getChatManager() {
		return chatManager;
	}
	
	private ServiceConnection connection = new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			ChatManagerService.LocalBinder binder = (ChatManagerService.LocalBinder) service;
			chatManager = binder.getChatManager();
		}
	};
}
