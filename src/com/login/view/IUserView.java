/**
 * 
 */
/**
 * @author QHT
 *
 */
package com.login.view;
public interface IUserView{
	//界面信息展示
	void showInfo();
	//操作数据库
	void dogreendao();
	//跳转
	void toTwoActivity();
	//用户名密码错误
	void ToastjudgeError();
	//登录错误
	void ToastrequestError();
	//登录成功
	void ToastrequestSuccess();
} 
