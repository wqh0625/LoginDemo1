package com.dingtao.logindemo.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.dingtao.logindemo.activity.LoginActivity;
import com.dingtao.logindemo.bean.Result;
import com.dingtao.logindemo.bean.User;
import com.dingtao.logindemo.core.BasePresenter;
import com.dingtao.logindemo.core.DataCall;
import com.dingtao.logindemo.http.Utils;
import com.dingtao.logindemo.model.LoginModel;
import com.google.gson.Gson;


/**
 * @author dingtao
 * @date 2018/12/6 14:41
 * qq:1940870847
 */
public class LoginPresenter extends BasePresenter {


    public LoginPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Result getData(String... args) {
        Result result = LoginModel.login(args[0],args[1]);//调用网络请求获取数据
        return result;
    }
}
