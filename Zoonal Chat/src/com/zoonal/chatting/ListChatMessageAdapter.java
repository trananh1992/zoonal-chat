package com.zoonal.chatting;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoonal.chatting.R;
import com.zoonal.chatting.core.Message;

public class ListChatMessageAdapter extends ArrayAdapter<Message> {

	public ListChatMessageAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public void addMessage(Message message) {
		if (message != null) {
			this.add(message);
		}
	}
	
	public void rebuildMessage(List<Message> newItems) {
		this.clear();
		for (Message message : newItems) {
			this.add(message);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View messageView = convertView;
		if (messageView == null) {
			messageView = new ChatMessageCustomView(getContext());
		}
		
		final Message item = getItem(position);
		if (item != null) {
			final ImageView imageView = ((ImageView) messageView.findViewById(R.id.imageView));
			final TextView txtUserName = ((TextView) messageView.findViewById(R.id.txtUserName));
			final TextView txtMessage = ((TextView) messageView.findViewById(R.id.txtMessage));
			
			imageView.setImageBitmap(item.getSender().getAvatarBitmap());
			txtUserName.setText(item.getSender().getUsername());
			txtMessage.setText(item.getMessage());
		}
		return  messageView;
	}
}
