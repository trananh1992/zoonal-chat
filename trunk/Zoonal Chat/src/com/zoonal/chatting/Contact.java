/**
 * 
 */
package com.zoonal.chatting;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

/**
 * @author Hiepnn
 * 
 */
public class Contact extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		
		final GridView gridView = (GridView) findViewById(R.id.myGrid);
		Bundle extra = getIntent().getExtras();
		final TextView textUsername = (TextView) findViewById(R.id.nameLogin);
		if (extra != null) {
			textUsername.setText(extra.getString("name"));
		}

		gridView.setAdapter(new ImageAdapter(this));
		
	}

}
