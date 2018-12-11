package com.dingtao.logindemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.dingtao.logindemo1.R;
import com.dingtao.logindemo.bean.Result;
import com.dingtao.logindemo.bean.User;
import com.dingtao.logindemo.core.DTApplication;
import com.dingtao.logindemo.core.DataCall;
import com.dingtao.logindemo.presenter.LoginPresenter;
import com.dingtao.logindemo.util.StringUtil;
import com.google.gson.Gson;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        DataCall<User>,CompoundButton.OnCheckedChangeListener {

    private EditText mMobile,mPassword;

    LoginPresenter presenter = new LoginPresenter(this);
    SharedPreferences share = DTApplication.getInstance().getShare();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (share.getString("user",null)!=null){//有登陆用户，直接进入
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();//关闭页面
            return;
        }
        setContentView(R.layout.activity_login);

        mMobile = findViewById(R.id.login_mobile);
        mPassword = findViewById(R.id.login_pas);

        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.login_reg).setOnClickListener(this);
        findViewById(R.id.login_qq_btn).setOnClickListener(this);

        CheckBox remPasCheck = findViewById(R.id.login_rem_pas);//记住密码
        remPasCheck.setOnCheckedChangeListener(this);
        boolean remPas = share.getBoolean("login_rem_pas",false);//读取sp中记住密码状态
        remPasCheck.setChecked(remPas);//还原记住密码状态
        if (remPas){//如果选择了记住密码，则还原数据
            mMobile.setText(share.getString("login_mobile",""));
            mPassword.setText(share.getString("login_password",""));
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.login_btn) {//点击登录按钮
            final String mobile = mMobile.getText().toString();
            final String password = mPassword.getText().toString();

            if (!StringUtil.isPhone(mobile)){
                Toast.makeText(this,"请输入正确手机号",Toast.LENGTH_LONG).show();
                return;
            }
            if (password.length()<6){
                Toast.makeText(this,"密码最少6位",Toast.LENGTH_LONG).show();
                return;
            }

            boolean remPas = share.getBoolean("login_rem_pas",false);//获取sp中保存的值
            if (remPas){//选择了记住密码
                SharedPreferences.Editor editor = share.edit();
                editor.putString("login_mobile",mobile);
                editor.putString("login_password",password);
                editor.commit();
            }

            presenter.requestData(mobile, password);//presenter请求数据，方法内部自动调用useModel()进行数据梳理
        }else if (v.getId()==R.id.login_reg){//点击注册按钮

            Intent intent = new Intent(this,RegActivity.class);
            startActivity(intent);
        }else if(v.getId()==R.id.login_qq_btn){

            if(Build.VERSION.SDK_INT>=23){//QQ需要申请写入权限
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(this,mPermissionList,123);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==123){//权限获取结果回调
        }
    }

    @Override
    public void success(User data) {
        Toast.makeText(this,data.getUsername(),Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = share.edit();
        editor.putString("user",new Gson().toJson(data));
        editor.commit();
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void fail(Result result) {
        Toast.makeText(this,result.getCode()+"   "+result.getMsg(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId()==R.id.login_rem_pas){
            SharedPreferences.Editor editor = share.edit();
            if (!isChecked){//取消记录密码
                editor.remove("login_mobile");//移除手机号
                editor.remove("login_password");//移除密码
            }
            editor.putBoolean("login_rem_pas",isChecked);//保存记住密码状态
            editor.commit();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unBindCall();//解除本页面的接口绑定
    }
}
