package com.zoonal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	private EditText txtUsername, txtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        txtUsername = (EditText) findViewById(R.id.text_userName);
        txtPassword = (EditText) findViewById(R.id.text_pass);
        Button bttLogin = (Button) findViewById(R.id.btnLogin);
        
        bttLogin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, WebActivity.class);
				intent.putExtra("username", txtUsername.getText().toString());
				intent.putExtra("password", txtPassword.getText().toString());
				LoginActivity.this.startActivity(intent);
			}
		});
    }
}
