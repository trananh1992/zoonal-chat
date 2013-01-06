package com.zoonal.settings;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.zoonal.R;

public class SettingMenuActivity extends FragmentActivity {
	
	MainSettingFragment fragment;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        fragment = new MainSettingFragment();
        getSupportFragmentManager().beginTransaction()
        	.add(R.id.setting_fragement_container, fragment).commit();
	}
	
	public void onItemClick(View v) {
		fragment.onItemClick(v);
	}
}
