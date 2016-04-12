package com.login.presenter;

public interface Ipresenter {
	//用户名和密码判断
  void judgeerror();
  //登录错误和成功
  void requestError();
  void requestSuccess();
}
