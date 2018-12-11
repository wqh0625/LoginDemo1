package com.dingtao.logindemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.dingtao.logindemo1.R;
import com.dingtao.logindemo.bean.User;
import com.dingtao.logindemo.core.DTApplication;
import com.google.gson.Gson;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * @author dingtao
 * @date 2018/12/6 20:00
 * qq:1940870847
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences share = DTApplication.getInstance().getShare();

    ImageView imageView;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            imageView.setImageBitmap(bitmap);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        String userStr = share.getString("user","");
        final User user = new Gson().fromJson(userStr,User.class);

        findViewById(R.id.logout_btn).setOnClickListener(this);

        imageView= findViewById(R.id.zxing_image);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(user.getUsername(),400);
                Message message = handler.obtainMessage();
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        DTApplication.getInstance().getShare().edit().remove("user").commit();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
