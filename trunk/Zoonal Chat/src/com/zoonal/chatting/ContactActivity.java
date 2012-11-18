package com.zoonal.chatting;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.zoonal.chatting.core.AvatarLoader;
import com.zoonal.chatting.core.ChatEventObject;
import com.zoonal.chatting.core.OnChatEventListener;
import com.zoonal.chatting.core.User;

public class ContactActivity extends Activity implements OnItemClickListener,
		LoaderCallbacks<Bitmap>, OnChatEventListener {
	
	private ChatManagerService.ChatManager chatManager;
	private LoaderManager loaderManager;
	private ContactListAdapter adapter;
	private GridView gridView;
	
	private int loaderIndex = 0;
	private ArrayList<User> users = new ArrayList<User>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		
		gridView = (GridView) findViewById(R.id.myGrid);
		Bundle extra = getIntent().getExtras();
		final TextView textUsername = (TextView) findViewById(R.id.nameLogin);
		if (extra != null) {
			textUsername.setText(extra.getString("name"));
		}
		gridView.setAdapter(new ContactListAdapter(this));
		
		Intent intent = new Intent(this, ChatManagerService.class);
		this.bindService(intent, connection, Service.BIND_AUTO_CREATE);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, ChatActivity.class);
	}
	
	private ServiceConnection connection = new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			ChatManagerService.LocalBinder binder = (ChatManagerService.LocalBinder) service;
			chatManager = binder.getChatManager();
			chatManager.addOnChatEventListener(ContactActivity.this);
		}
	};

	public Loader<Bitmap> onCreateLoader(int arg0, Bundle arg1) {
		return new AvatarLoader(this, users.get(loaderIndex).getAvatarUrl());
	}

	public void onLoadFinished(Loader<Bitmap> arg0, Bitmap arg1) {
		User newItem = users.get(loaderIndex++);
		newItem.setAvatarBitmap(arg1);
		adapter.addItem(newItem);
		adapter.notifyDataSetChanged();
		if (loaderIndex < users.size() - 1) {
			loaderManager.restartLoader(0, null, this);
		}
	}

	public void onLoaderReset(Loader<Bitmap> arg0) {
		// TODO Auto-generated method stub
	}

	public void onChatEvent(ChatEventObject obj) {
		if (obj.getEvent().equalsIgnoreCase("get-list-user")) {
			
		}
	}
	
	private synchronized void setChannelList(List<User> users) {
		this.users = (ArrayList<User>) users;
	}
}
