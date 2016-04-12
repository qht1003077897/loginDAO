/**
 * 
 */
/**
 * @author QHT
 *
 */
package com.logindao.model;

import android.content.Context;

import com.example.model.UserBean;


public interface IUserModel {
//判断用户名和密码的合法性
	void judgmentNameANDPass(String username,String password);
//okhttp请求
	void httprequest(String username,String password);
//greenDAO数据库保存
	void doGreenDAO(Context context);
}