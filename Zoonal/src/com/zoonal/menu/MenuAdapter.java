package com.zoonal.menu;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoonal.R;

public class MenuAdapter extends ArrayAdapter<MenuItem> {

	public MenuAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		MenuItem[] fakeItems = {
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings"),
    			new MenuItem(R.drawable.img_menu_item, "Settings")
    	};
		addData(fakeItems);
	}

	public void addData(MenuItem item) {
		if (item != null) {
			add(item);
		}
	}
	
	public void addData(List<MenuItem> items) {
		for (MenuItem item : items) {
			addData(item);
		}
	}
	
	public void addData(MenuItem... items) {
		for (MenuItem item : items) {
			addData(item);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View channelView = convertView;
		if (channelView == null) {
			channelView = new CustomViewMenuItem(getContext());
		}
		
		final MenuItem item = getItem(position);
		if (item != null) {
			final ImageView imageView = ((ImageView) channelView.findViewById(R.id.menu_image));
			final TextView titleView = ((TextView) channelView.findViewById(R.id.menu_title));
			imageView.setImageResource(item.imageId);
			titleView.setText(item.title);
		}
		
		return channelView;
	}
}
