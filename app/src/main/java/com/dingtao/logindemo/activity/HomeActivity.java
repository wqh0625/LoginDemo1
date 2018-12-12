package com.dingtao.logindemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dingtao.logindemo.bean.Result;
import com.dingtao.logindemo.core.DataCall;
import com.dingtao.logindemo.presenter.UserInfoPresenter;
import com.dingtao.logindemo.util.StringUtil;
import com.dingtao.logindemo1.R;
import com.dingtao.logindemo.bean.User;
import com.dingtao.logindemo.core.DTApplication;
import com.google.gson.Gson;

import java.io.FileNotFoundException;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * @author dingtao
 * @date 2018/12/6 20:00
 * qq:1940870847
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences share = DTApplication.getInstance().getShare();

    ImageView imageView;

    ImageView mAvatar;

    private User user;

    private final static  int PHOTO_REQUEST_GALLERY = 100;
    UserInfoPresenter userInfoPresenter;

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
        user = new Gson().fromJson(userStr,User.class);

        findViewById(R.id.logout_btn).setOnClickListener(this);

        imageView= findViewById(R.id.zxing_image);
        mAvatar = findViewById(R.id.avatar);
        mAvatar.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(user.getUsername(),400);
                Message message = handler.obtainMessage();
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }).start();

        userInfoPresenter= new UserInfoPresenter(new UserInfoCall());
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.logout_btn) {
            DTApplication.getInstance().getShare().edit().remove("user").commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else if(v.getId()==R.id.avatar){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
             // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
           startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY&& resultCode==Activity.RESULT_OK) {
            Uri uri =data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(uri));
                mAvatar.setImageBitmap(bitmap);

                String path = StringUtil.getRealPathFromUri(this,uri);
                Log.i("dt",path);

                userInfoPresenter.requestData(user.getUid()+"",path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private class UserInfoCall implements DataCall{

        @Override
        public void success(Object data) {

        }

        @Override
        public void fail(Result result) {
            Toast.makeText(getBaseContext(),result.getCode()+"  "+result.getMsg(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userInfoPresenter.unBindCall();
    }
}
