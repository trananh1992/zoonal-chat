package com.zoonal.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoonal.R;

public class MainSettingFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_setting_main, container, false);
	}
	
	public void onItemClick(View view) {
		Fragment newFragment = null;
		switch (view.getId()) {
		case R.id.settingGeneralBtt:
			newFragment = new GeneralSettingFragment();
			break;
			
		case R.id.settingPersonalBtt:
			newFragment = new PersonalSettingFragment();
			break;
			
		case R.id.settingEmailBtt:
			newFragment = new EmailSettingFragment();
			break;
		}
		if (newFragment != null) {
			replaceFragment(newFragment);
		}
	}
	
	private void replaceFragment(Fragment newFragment) {
		FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
		ft.replace(R.id.setting_fragement_container, newFragment).addToBackStack(null).commit();
	}
}
