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
//�ж��û���������ĺϷ���
	void judgmentNameANDPass(String username,String password);
//okhttp����
	void httprequest(String username,String password);
//greenDAO���ݿⱣ��
	void doGreenDAO(Context context);
}