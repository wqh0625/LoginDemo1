package com.dingtao.logindemo.model;

import com.dingtao.logindemo.bean.Result;
import com.dingtao.logindemo.bean.User;
import com.dingtao.logindemo.http.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;

/**
 * @author dingtao
 * @date 2018/12/6 14:55
 * qq:1940870847
 */
public class AvatarModel {

    public static Result avatar(final String uid, final File file){
        String resultString = Utils.postFile("http://www.zhaoapi.cn/file/upload",
                new String[]{"uid"},new String[]{uid},
                "file",file);

        Gson gson = new Gson();

        Type type = new TypeToken<Result>() {}.getType();

        Result result= gson.fromJson(resultString,type);

        return result;
    }

}
