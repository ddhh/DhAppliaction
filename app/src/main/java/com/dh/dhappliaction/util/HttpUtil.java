package com.dh.dhappliaction.util;

import com.dh.dhappliaction.bean.BaseBean;
import com.dh.dhappliaction.bean.LoginBean;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 端辉 on 2016/5/25.
 */
public class HttpUtil  {

    public static String SERVER_URL = "http://182.61.48.169:8080/DhServer/";
    public static String ACTION_LOGIN = "Login";
    public static String ACTION_REGISTER = "Register";
    public static String ACTION_SYNC = "Sync";
    public static String ACTION_RELOAD = "Reload";

    public static BaseBean register(String user,String password){
        BaseBean bb = new BaseBean();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(SERVER_URL + ACTION_REGISTER);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user", user));
        params.add(new BasicNameValuePair("password", password));
        try {
            request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String json = EntityUtils.toString(response.getEntity());
                Gson gson = new Gson();
                bb = gson.fromJson(json, BaseBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bb;
    }


    public static LoginBean login(String user, String password) {

        LoginBean lb = new LoginBean();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(SERVER_URL + ACTION_LOGIN);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user", user));
        params.add(new BasicNameValuePair("password", password));
        try {
            request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String json = EntityUtils.toString(response.getEntity());
                Gson gson = new Gson();
                lb = gson.fromJson(json, LoginBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lb;
    }

    public static BaseBean sync(String user_id, String s){

        BaseBean bb = new BaseBean();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(SERVER_URL + ACTION_SYNC);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("syncStr", s));
        params.add(new BasicNameValuePair("user_id", user_id));
        try {
            request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String json = EntityUtils.toString(response.getEntity());
                Gson gson = new Gson();
                bb = gson.fromJson(json,BaseBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bb;
    }


    public static String reload(String user_id){

        String result = "";

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(SERVER_URL + ACTION_RELOAD);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id", user_id));
        try {
            request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
            }else{
                result = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    

}
