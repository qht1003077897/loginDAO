package com.logindao.model;

import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import com.example.model.DaoMaster;
import com.example.model.DaoSession;
import com.example.model.USERDao;
import com.example.model.UserBean;
import com.example.model.UserCallback;
import com.example.model.Util;
import com.login.presenter.Ipresenter;
import com.zhy.http.okhttp.OkHttpUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
@SuppressLint("NewApi") public class UserModelIml implements IUserModel {
	  private SQLiteDatabase db;
		private DaoMaster daoMaster;
		private DaoSession daoSession;
		private USERDao userDao; 
		private Ipresenter presenter;
		private List<UserBean> daoslist;
		 public int ifsuccess=0;
	public UserModelIml(Ipresenter view){
		this.daoslist=new ArrayList<UserBean>();
		presenter=view;
	}
	public void  judgmentNameANDPass(String username, String password) {
		// TODO Auto-generated method stub
		if(username==null||username.isEmpty()||password==null||password.isEmpty()){
		   
      }else{
    	  Util.judgeNullMsg=1;
    	  UserBean.setUsername(username);
    	  UserBean.setPassword(password);
      }
		return ;
	}
	@Override
	public void  httprequest(String username, String password) {
		// TODO Auto-generated method stub
		  OkHttpUtils
	      .get()//
	      .url(Util.getUrl())
	      .addParams("username", username)//
	      .addParams("password", password)//
	      .build()//
	      .execute(new UserCallback()
	      {
	          @Override
	          public void onError(Call call, Exception e)
	          {
	        	  presenter.requestError();  
	          }
	          public void onResponse(UserBean response)
	          {
	        	String responsestring=response.getmsg();
	        	
	          	if(responsestring.equals("login success")){
	          		Util.requestMsg=1;
	          		presenter.requestSuccess();
	          }else{
	        	  presenter.requestError();
	          }
	         }
	      });
		  return;
	}
	@Override
	public void doGreenDAO(Context context) {
//	    USERDao userdao = SingleType.getInstance().getuserDao();
		SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(context,"user.db",null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        userDao = daoSession.getUSERDao();
        userDao.deleteAll();
	    UserBean user = new UserBean((long)2, UserBean.getUsername(), UserBean.getPassword());
	    daoslist.add(user);   
	    userDao.insertInTx(daoslist);	
	    Util.doGreenDaoMsg=1;
	}
}
