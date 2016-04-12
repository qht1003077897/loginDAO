/**
 * 
 */
/**
 * @author QHT
 *
 */
package com.login.presenter;
import android.app.Activity;
import android.content.Context;

import com.example.login.MainActivity;
import com.example.model.UserBean;
import com.example.model.Util;
import com.login.view.IUserView;
import com.logindao.model.UserModelIml;
 public class UserPresenter implements Ipresenter{
	 //同时持有Model和View的引用
	UserModelIml userModelIml;
	UserBean userBean;
	MainActivity mainActivity;
	IUserView iUserView;
public UserPresenter(IUserView view){
	this.userModelIml=new UserModelIml(this);
	this.userBean =new UserBean();
	iUserView=view;
}
public void judge(String userName, String password){
	  userModelIml.judgmentNameANDPass(userName, password);
	  if(Util.judgeNullMsg==1){
		  userModelIml.httprequest(userName, password);
	  }else{
		judgeerror();  
	  }
	  return;
}
public void dodatebase(Context context){
	// TODO Auto-generated method stub
	userModelIml.doGreenDAO(context);
}
public void toactivityANDshow(int i){
	iUserView.toTwoActivity();
 }

public void judgeerror(){
	iUserView.ToastjudgeError();	 
}

public void requestError(){
	 if(Util.requestMsg==0)
	   {
		 iUserView.ToastrequestError();
	   }
}

public void requestSuccess(){
	     iUserView.dogreendao();
		 iUserView.ToastrequestSuccess();
	 toactivityANDshow(Util.requestMsg);
 }
}