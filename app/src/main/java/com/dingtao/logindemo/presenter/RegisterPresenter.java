package com.dingtao.logindemo.presenter;

import com.dingtao.logindemo.bean.Result;
import com.dingtao.logindemo.core.BasePresenter;
import com.dingtao.logindemo.core.DataCall;
import com.dingtao.logindemo.model.LoginModel;
import com.dingtao.logindemo.model.RegisterModel;


/**
 * @author dingtao
 * @date 2018/12/6 14:41
 * qq:1940870847
 */
public class RegisterPresenter extends BasePresenter {


    public RegisterPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Result getData(String... args) {
        Result result = RegisterModel.reg(args[0],args[1]);//调用网络请求获取数据
        return result;
    }
}
