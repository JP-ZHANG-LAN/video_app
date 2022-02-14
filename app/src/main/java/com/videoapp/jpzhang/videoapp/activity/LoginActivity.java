package com.videoapp.jpzhang.videoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.videoapp.jpzhang.videoapp.MainActivity;
import com.videoapp.jpzhang.videoapp.R;
import com.videoapp.jpzhang.videoapp.api.Api;
import com.videoapp.jpzhang.videoapp.api.ApiConfig;
import com.videoapp.jpzhang.videoapp.api.TtitCallback;
import com.videoapp.jpzhang.videoapp.entity.LoginResponse;
import com.videoapp.jpzhang.videoapp.util.AppConfig;
import com.videoapp.jpzhang.videoapp.util.StringUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText account;
    private EditText pwd;
    private Button login;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        account = findViewById(R.id.et_account);
        pwd = findViewById(R.id.et_pwd);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String saccount = account.getText().toString().trim();
                String spwd = pwd.getText().toString().trim();
                dologin(saccount,spwd);
//                Intent intent1 = new Intent(LoginActivity.this,HomeActivity.class);
//                startActivity(intent1);
//                navigateTo(HomeActivity.class);
        }
    }

    public void dologin(String account,String pwd) {
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
        Api.config(ApiConfig.LOGIN,map).postRequest(this, new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                final String s = res;
                Log.e("===============请求成功",res);
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 0) {
                    String token = loginResponse.getToken();
                    insertVal("token", token);
                }
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
        //封装前的请求方式
//        // 将map转换为json再转换为string放到请求体中
//        JSONObject jsonObject = new JSONObject(map);
//        String jsonmap = jsonObject.toString();
//        //1、获取okhttpclient对象
//        OkHttpClient client = new OkHttpClient
//                .Builder()
//                .build();
//        //2、设置异步post请求的请求体
//        RequestBody requestBody = RequestBody
//                .create(MediaType.parse("application/json;charset=utf-8"),jsonmap);
//        //3、构建request对象
//        Request request = new Request.Builder()
//                .url(AppConfig.BASE_URl + "/app/login")
//                .addHeader("contentType","application/json;charset=utf-8")
//                .post(requestBody)
//                .build();
//        //4、构建call对象
//        final Call call = client.newCall(request);
//        //5、发送异步post请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("===============请求失败",e.getMessage());
//                showToast("用户名或密码错误");
//            }
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                Log.e("===============请求成功",response.toString());
//                //异步请求相当于开辟一个子线程，要对主线程ui进行操作就需要再回到主线程，也可以用handle处理
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showToast(response.toString());
//                    }
//                });
//                navigateTo(HomeActivity.class);
//            }
//        });
    }
}
