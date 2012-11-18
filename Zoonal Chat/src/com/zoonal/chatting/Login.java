package com.zoonal.chatting;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		final Button btnlogin = (Button) findViewById(R.id.btnLogin);
		final EditText textUser = (EditText) findViewById(R.id.text_userName);
		final EditText textPass = (EditText) findViewById(R.id.text_pass);
		btnlogin.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(Login.this, Contact.class);
//				intent.putExtra("name", textUser.getText().toString());
//				startActivity(intent);
				// name = admin pass = 123 thi dang nhap thanh cong
				 if (textUser.getText().toString().equals("admin")
				 && textPass.getText().toString().equals("123") ) {
				 Intent intent = new Intent(Login.this, Contact.class);
				 intent.putExtra("name", textUser.getText().toString());
				 startActivity(intent);
				 }
				 else{
				 Toast.makeText(Login.this, "Login faile",
				 Toast.LENGTH_SHORT).show();
				
				 }
			}
		});

	}

}
