package com.zoonal.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoonal.R;

public class CustomViewMenuItem extends LinearLayout {
	
	ImageView image;
	TextView title;
	TextView author;
	TextView personWatching;
	
	public CustomViewMenuItem(Context context) {
		super(context);
		LayoutInflater li = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.menu_list, this, true);

		image = (ImageView) findViewById(R.id.menu_image);	
		title = (TextView) findViewById(R.id.menu_title);
	}

}
