package com.example.login;

import com.example.model.UserBean;
import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class TWOActivity extends ActionBarActivity  {
	private  TextView tv2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv2=(TextView)findViewById(R.id.tv2);
		showInfo();
	}
	public void showInfo() {
		// TODO Auto-generated method stub
		tv2.setText(UserBean.getUsername());
	}
}
