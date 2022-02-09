package com.videoapp.jpzhang.videoapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.videoapp.jpzhang.videoapp.R;
import com.videoapp.jpzhang.videoapp.api.Api;
import com.videoapp.jpzhang.videoapp.api.ApiConfig;
import com.videoapp.jpzhang.videoapp.api.TtitCallback;
import com.videoapp.jpzhang.videoapp.util.StringUtils;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText account;
    private EditText pwd;
    private Button register;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        account = findViewById(R.id.et_account);
        pwd = findViewById(R.id.et_pwd);
        register = findViewById(R.id.btn_register);
        register.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                String saccount = account.getText().toString().trim();
                String spwd = pwd.getText().toString().trim();
                doregister(saccount,spwd);
//                Intent intent1 = new Intent(LoginActivity.this,HomeActivity.class);
//                startActivity(intent1);
                navigateTo(HomeActivity.class);
        }
    }

    public void doregister(String account,String pwd){
        if (StringUtils.isEmpty(account)){
            showToast("用户名不能为空！");
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("密码不能为空！");
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("mobile",account);
        map.put("password",pwd);
        //封装后的请求方法
        Api.config(ApiConfig.REGISTER,map).postRequest(this, new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                final String s = res;
                Log.e("===============请求成功",res);
                //异步请求相当于开辟一个子线程，要对主线程ui进行操作就需要再回到主线程，也可以用handle处理
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(s);
                    }
                });
                navigateTo(HomeActivity.class);
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("===============请求失败",e.getMessage());
                showToast("用户名或密码错误");
            }
        });

    }
}
