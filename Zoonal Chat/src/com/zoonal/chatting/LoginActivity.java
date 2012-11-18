package com.zoonal.chatting;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.zoonal.chatting.core.ChatEventObject;
import com.zoonal.chatting.core.OnChatEventListener;

public class LoginActivity extends Activity implements OnChatEventListener {
	
	private ChatManagerService.ChatManager chatManager;
	
	private Button btnlogin;
	private EditText textUser;
	private EditText textPass;
	private Intent intent; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		btnlogin = (Button) findViewById(R.id.btnLogin);
		textUser = (EditText) findViewById(R.id.text_userName);
		textPass = (EditText) findViewById(R.id.text_pass);
		btnlogin.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				JSONObject object = new JSONObject();
				try {
					object.put("name", textUser.getText().toString());
					object.put("password", textPass.getText().toString());
					chatManager.emitEvent("login", object);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		intent = new Intent(this, ChatManagerService.class);
		this.startService(intent);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		this.bindService(intent, connection, Service.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.unbindService(connection);
	}
	
	public ChatManagerService.ChatManager getChatManager() {
		return chatManager;
	}
	
	private ServiceConnection connection = new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			ChatManagerService.LocalBinder binder = (ChatManagerService.LocalBinder) service;
			chatManager = binder.getChatManager();
			chatManager.addOnChatEventListener(LoginActivity.this);
		}
	};

	public void onChatEvent(ChatEventObject obj) {
		if (obj.getEvent().equalsIgnoreCase("login")) {
			String result = String.valueOf(obj.getData()[0]);
			if (result.equals("ok")) {
				Intent contactIntent = new Intent(this, ContactActivity.class);
				contactIntent.putExtra("name", textUser.getText().toString());
				startActivity(contactIntent);
			} else {
				// hien thi mot cai alert thong bao login khong thanh cong
				
			}
		}
		else if (obj.getEvent().equalsIgnoreCase("error")) {
			
		}
		else if (obj.getEvent().equalsIgnoreCase("disconnected")) {
			
		}
	}

}
