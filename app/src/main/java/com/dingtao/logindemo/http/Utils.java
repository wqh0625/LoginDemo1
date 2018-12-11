package com.dingtao.logindemo.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utils {
    public static String get(String urlString){

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url(urlString).get().build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String postForm(String url,String[] name,String[] value){

        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody.Builder formBuild = new FormBody.Builder();
        for (int i = 0; i < name.length; i++) {
            formBuild.add(name[i],value[i]);
        }

        Request request = new Request.Builder().url(url).post(formBuild.build()).build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String postJson(String url,String jsonString){

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonString);

        Request request = new Request.Builder().url(url).post(requestBody).build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
