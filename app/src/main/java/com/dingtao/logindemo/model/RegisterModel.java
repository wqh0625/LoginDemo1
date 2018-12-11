package com.dingtao.logindemo.model;

import com.dingtao.logindemo.bean.Result;
import com.dingtao.logindemo.bean.User;
import com.dingtao.logindemo.http.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @author dingtao
 * @date 2018/12/6 14:55
 * qq:1940870847
 */
public class RegisterModel {

    public static Result reg(final String mobile, final String pwd){
        String resultString = Utils.get("http://www.zhaoapi.cn/user/reg?mobile="
                +mobile+"&password="+pwd);

        Gson gson = new Gson();

        Type type = new TypeToken<Result<User>>() {}.getType();

        Result result= gson.fromJson(resultString,type);

        return result;
    }

}
