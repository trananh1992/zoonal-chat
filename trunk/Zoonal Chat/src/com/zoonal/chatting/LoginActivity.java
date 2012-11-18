package com.zoonal.chatting;

import org.json.JSONException;
import org.json.JSONObject;

import com.zoonal.chatting.ChatManagerService.ChatManager;
import com.zoonal.chatting.core.ChatEventObject;
import com.zoonal.chatting.core.OnChatEventListener;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnChatEventListener {
	
	private ChatManagerService.ChatManager chatManager;
	
	final Intent intent= new Intent(LoginActivity.this, Contact.class);
	
	Button btnlogin;
	EditText textUser;
	EditText textPass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		btnlogin = (Button) findViewById(R.id.btnLogin);
		textUser = (EditText) findViewById(R.id.text_userName);
		textPass = (EditText) findViewById(R.id.text_pass);
		
		this.bindService(intent, connection, Service.BIND_AUTO_CREATE);
		
		
		btnlogin.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				/*Intent intent = new Intent(LoginActivity.this, Contact.class);*/
				
				
				//create 
				JSONObject object = new JSONObject();
				try {
					object.put("name", textUser.getText().toString());
					object.put("pass", textPass.getText().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

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


	public void onChatEvent(ChatEventObject obj) {
		// TODO Auto-generated method stub
		if(obj.getEvent().equalsIgnoreCase("login")){
		String result = String.valueOf(obj.getData()[0]);
			if(result.equals("ok")){
				//login thanh cong
				
				intent.putExtra("name", textUser.getText().toString());
				startActivity(intent);
			}
		}
	}

}
