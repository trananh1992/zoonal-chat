package com.zoonal.chatting;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zoonal.chatting.core.ChatEventObject;
import com.zoonal.chatting.core.ChatSession;
import com.zoonal.chatting.core.ListChatMessageAdapter;
import com.zoonal.chatting.core.Message;
import com.zoonal.chatting.core.OnChatEventListener;

public class ChatFragment extends Fragment implements OnChatEventListener {
	
	private LinearLayout linearLayout;
	private ListView listMessage;
	private TextView txtMessage;
	private Button bttSend;
	
	private ChatSession session;
	private ListChatMessageAdapter adapter;
	private ChatActivity activity;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (ChatActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_chat, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Bundle argument = getArguments();
		if (argument != null) {
			session = (ChatSession) argument.getSerializable("session");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		adapter = new ListChatMessageAdapter(getActivity(), R.layout.list_message);
        adapter.setNotifyOnChange(false);
        
        listMessage = (ListView) activity.findViewById(R.id.list_message);
        listMessage.setAdapter(adapter);
        
        linearLayout = (LinearLayout) activity.findViewById(R.id.linearAvatar);
        txtMessage = (TextView) activity.findViewById(R.id.text_message);
        bttSend = (Button) activity.findViewById(R.id.button_send);
        bttSend.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				try {
					if (session.getType() == ChatSession.TYPE_SINGLE) {
						Message message = new Message(Constants.currentUser, txtMessage.getText().toString());
						adapter.add(message);
						adapter.notifyDataSetChanged();
						
						JSONObject obj = new JSONObject();
						obj.put("receiverName", session.getUsers().get(0));
						obj.put("message", txtMessage.getText().toString());
					}
					else if (session.getType() == ChatSession.TYPE_ROOM) {
						
					}
				}
				catch (JSONException ex) {}
			}
		});
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
	
	@Override
	public void onPause() {
		super.onPause();
		activity.getChatManager().removeChatEventListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		activity.getChatManager().addOnChatEventListener(this);
		adapter.rebuildMessage(session.getMessages());
	}

	public void onChatEvent(ChatEventObject obj) {
		if (obj.getEvent().equalsIgnoreCase("user-message")) {
			Message message = (Message) obj.getData()[0];
			if (message.getSender().getUsername().equals(session.getUsers().get(0).getUsername())) {
				adapter.add(message);
				adapter.notifyDataSetChanged();
			} 
		}
		else if (obj.getEvent().equalsIgnoreCase("user-group-message")) {
			
		}
	}

}
