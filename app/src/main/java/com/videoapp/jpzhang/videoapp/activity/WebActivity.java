package com.videoapp.jpzhang.videoapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;

import com.videoapp.jpzhang.videoapp.R;
import com.videoapp.jpzhang.videoapp.jsbridge.BridgeHandler;
import com.videoapp.jpzhang.videoapp.jsbridge.BridgeWebView;
import com.videoapp.jpzhang.videoapp.jsbridge.CallBackFunction;

/**
 * @author: wei
 * @date: 2020-08-17
 **/
@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity {
    private BridgeWebView bridgeWebView;
    private String url;

    @Override
    protected int initLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        bridgeWebView = findViewById(R.id.bridgeWebView);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }
//        registJavaHandler();
        initWebView();
    }

    private void initWebView() {
        WebSettings settings = bridgeWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        bridgeWebView.loadUrl("http://www.news.cn/2022-02/15/c_1211573157.html");
    }

//    private void registJavaHandler() {
//        bridgeWebView.registerHandler("goback", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                finish();
//            }
//        });
//    }
}
