package com.videoapp.jpzhang.videoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.videoapp.jpzhang.videoapp.activity.BaseActivity;
import com.videoapp.jpzhang.videoapp.activity.HomeActivity;
import com.videoapp.jpzhang.videoapp.activity.LoginActivity;
import com.videoapp.jpzhang.videoapp.activity.RegisterActivity;
import com.videoapp.jpzhang.videoapp.util.StringUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        Button login = findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        Button register = findViewById(R.id.btn_register);
        register.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        String token = findByKey("token");
        if (!StringUtils.isEmpty(token)) {
//            navigateToWithFlag(HomeActivity.class,
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            showToast("登录成功");
            navigateTo(HomeActivity.class);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_login:
                navigateTo(LoginActivity.class);
//                Intent intent1 = new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent1);
                break;
            case R.id.btn_register:
                navigateTo(RegisterActivity.class);
//                Intent intent2 = new Intent(MainActivity.this,RegisterActivity.class);
//                startActivity(intent2);
                break;
        }
    }
}
