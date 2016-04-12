package com.example.login;

import com.example.model.UserBean;
import com.login.presenter.UserPresenter;
import com.login.view.IUserView;

import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class MainActivity extends ActionBarActivity implements IUserView{
	private EditText	user;
	private TextView    login;
	private EditText	pass;
	private TextView ttt;
	private String tt,tttt;
	private UserPresenter userPresenter;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
	private void initView() {
		// TODO Auto-generated method stub
			login=(TextView)findViewById(R.id.login);
			ttt=(TextView)findViewById(R.id.tt);
			user=(EditText)findViewById(R.id.username);
			pass=(EditText)findViewById(R.id.password);
			userPresenter=new UserPresenter(this);
	}
	//登录操作
		public void login(View view) 
	{
			// TODO Auto-generated method stub
			 userPresenter.judge(getuserName(),getpassword());
	}
	public String getuserName() {
		// TODO Auto-generated method stub
		return user.getText().toString();
	}
	public String getpassword() {
		// TODO Auto-generated method stub
		return pass.getText().toString();
	}
	public void toTwoActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this,TWOActivity.class);
		startActivity(intent);
	}
	@Override
	public void ToastjudgeError() {
		// TODO Auto-generated method stub
		Toast.makeText(MainActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void ToastrequestError() {
		// TODO Auto-generated method stub
		Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void ToastrequestSuccess() {
		// TODO Auto-generated method stub
		showInfo();
		Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void showInfo() {
		// TODO Auto-generated method stub
		ttt.setText(UserBean.getUsername());
	}
	public void dogreendao(){
		userPresenter.dodatebase(this);
	}
 }

