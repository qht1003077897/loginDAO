package com.example.model;

import com.google.gson.Gson;
import okhttp3.Response;
import com.zhy.http.okhttp.callback.Callback;
import java.io.IOException;
/**
 * Created by QHT on 16/04/01.
 */
public abstract class UserCallback extends Callback<UserBean>
{
    @Override
    public UserBean parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
//      JsonObject jsonObject=new JsonObject();
        UserBean user = new Gson().fromJson(string, UserBean.class);
//      UserBean users=jsonObject.getObject(string, UserBean.class);
        return user;
    }
}