package com.zoonal.chatting;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.zoonal.chatting.core.ChatEventObject;
import com.zoonal.chatting.core.ChatSession;
import com.zoonal.chatting.core.Message;
import com.zoonal.chatting.core.OnChatEventListener;
import com.zoonal.chatting.core.User;

public class ChatManagerService extends Service {
	
	private LocalBinder binder = new LocalBinder();
	private ChatManager chatManager;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		chatManager = new ChatManager();
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	private static void log(Object message) {
    	Log.e("CHAT MANAGER", message.toString());
    }
	
	public class LocalBinder extends Binder {
		public ChatManager getChatManager() {
			return ChatManagerService.this.chatManager;
		}
	}
	
	public class ChatManager implements IOCallback {
		
		public static final String SERVER_ADDRESS = "http://127.0.0.1:8000";
		private SocketIO socket;
		private ArrayList<OnChatEventListener> listeners;
		private HashMap<String, User> users;
		private HashMap<String, ChatSession> sessions;
		
		public ChatManager() {
			try {
				URL url = new URL(SERVER_ADDRESS);
				socket = new SocketIO();
				socket.connect(url, chatManager);
				
				listeners = new ArrayList<OnChatEventListener>();
				users = new HashMap<String, User>();
				sessions = new HashMap<String, ChatSession>();
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
				ChatManagerService.this.stopSelf();
			}
		}
		
		public void onMessage(JSONObject json, IOAcknowledge ack) {
	    	log("Server send json data: " + json.toString());
	    }

	    public void onMessage(String data, IOAcknowledge ack) {
	        log("Server said: " + data);
	    }

	    public void onError(SocketIOException socketIOException) {
	    	log("error. socket exception");
	    	log(socketIOException);
	    	broadcastToListener("error", new Object[] { socketIOException });
	    }

	    public void onDisconnect() {
	        log("Connection terminated.");
	        broadcastToListener("disconnected", (Object[]) null);
	    }

	    public void onConnect() {
	        log("Connection established");
	        broadcastToListener("connected", (Object[]) null);
	    }

	    public void on(String event, IOAcknowledge ack, Object... args) {
	    	if (event.equalsIgnoreCase("user-message")) {
	    		String username = (String) args[0];
	    		String url = (String) args[2];
	    		
	    		ChatSession targetSession = sessions.get(username);
	    		User user = users.get(username);
	    		if (targetSession == null || user == null) {
	    			targetSession = new ChatSession(ChatSession.TYPE_SINGLE);
	    			Bitmap avatar = loadAvatar(url);
	    			user = new User(username, url, avatar);
	    			
	    			sessions.put(username, targetSession);
	    			users.put(username, user);
	    		}
	    		Message message = new Message(user, (String) args[1]);
	    		targetSession.getMessages().add(message);
	    		this.broadcastToListener(event, message);
	    	}
	    	else if (event.equalsIgnoreCase("user-group-message")) {
	    		
	    	}
	    }
	    
	    public void addOnChatEventListener(OnChatEventListener listener) {
	    	listeners.add(listener);
	    }
	    
	    public void removeChatEventListener(OnChatEventListener listener) {
	    	listeners.remove(listener);
	    }
	    
	    public void emitEvent(String event, JSONObject obj) {
	    	try {
		    	if (event.equalsIgnoreCase("user-message")) {
		    		String receiver = (String) obj.get("receiverName");
	    			ChatSession session = sessions.get(receiver);
	    			if (session != null) {
	    				User currentUser = new User(Constants.currentUser.getUsername()
	    						, Constants.currentUser.getAvatarUrl());
	    				Message message = new Message(currentUser, obj.getString("message"));
	    				session.getMessages().add(message);
	    			}
		    	}
		    	else if (event.equalsIgnoreCase("user-group-message")) {
		    		
		    	}
	    	} catch (JSONException ex) {
	    		log(ex);
	    	}
	    	finally {
	    		socket.emit(event, obj);
	    	}
	    }
	    
	    private void broadcastToListener(String event, Object... objects) {
	    	for (OnChatEventListener listener : listeners) {
	    		ChatEventObject obj = new ChatEventObject(event, objects);
	    		listener.onChatEvent(obj);
	    	}
	    }
	    
	    private Bitmap loadAvatar(String urlStr) {
			int response = -1;
			InputStream in = null;
			Bitmap bitmap = null;
			try {
				URL url = new URL(urlStr);
				URLConnection con = url.openConnection();
				if (!(con instanceof HttpURLConnection)) throw new IOException("Not an HTTP connection");
				
				HttpURLConnection httpConn = (HttpURLConnection) con;
				httpConn.setAllowUserInteraction(false);
				httpConn.setInstanceFollowRedirects(true);
				httpConn.setRequestMethod("GET");
				httpConn.connect();

				response = httpConn.getResponseCode();
				try {
					if (response == HttpURLConnection.HTTP_OK) {
						in = httpConn.getInputStream();
						bitmap = BitmapFactory.decodeStream(in);
					}
					httpConn.disconnect();
				}
				catch (Exception ex) {
					if (in != null) {
						in.close();
					}
				}
			} catch (Exception ex) {
				log(ex.getMessage());
			}
			return bitmap;
		}
	}
}
