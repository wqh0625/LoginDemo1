package com.dingtao.logindemo.presenter;

import com.dingtao.logindemo.bean.Result;
import com.dingtao.logindemo.core.BasePresenter;
import com.dingtao.logindemo.core.DataCall;
import com.dingtao.logindemo.model.AvatarModel;
import com.dingtao.logindemo.model.RegisterModel;

import java.io.File;


/**
 * @author dingtao
 * @date 2018/12/6 14:41
 * qq:1940870847
 */
public class UserInfoPresenter extends BasePresenter {

    public UserInfoPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Result getData(String... args) {

        File file = new File(args[1]);
        Result result = AvatarModel.avatar(args[0],file);//调用网络请求获取数据
        return result;
    }
}
