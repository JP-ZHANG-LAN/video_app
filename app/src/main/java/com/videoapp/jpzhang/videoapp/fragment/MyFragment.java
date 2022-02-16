package com.videoapp.jpzhang.videoapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.videoapp.jpzhang.videoapp.R;
import com.videoapp.jpzhang.videoapp.activity.LoginActivity;

public class MyFragment extends BaseFragment implements View.OnClickListener {

    public  ImageView imgHeader;
    public  RelativeLayout  rlCollect;
    public  RelativeLayout  rlSkin;
    public  RelativeLayout  rlLogout;

    public MyFragment() {
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        imgHeader = mRootView.findViewById(R.id.img_header);
        rlCollect = mRootView.findViewById(R.id.rl_collect);
        rlSkin = mRootView.findViewById(R.id.rl_skin);
        rlLogout = mRootView.findViewById(R.id.rl_logout);
    }

    @Override
    protected void initData() {
        imgHeader.setOnClickListener(this);
        rlCollect.setOnClickListener(this);
        rlSkin.setOnClickListener(this);
        rlLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header:

                break;
            case R.id.rl_collect:
//                navigateTo(MyCollectActivity.class);
                break;
            case R.id.rl_skin:
                String skin = findByKey("skin");
//                if (skin.equals("night")) {
//                    // 恢复应用默认皮肤
//                    SkinCompatManager.getInstance().restoreDefaultTheme();
//                    insertVal("skin", "default");
//                } else {
//                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
//                    insertVal("skin", "night");
//                }
                break;
            case R.id.rl_logout:
                removeByKey("token");
                navigateToWithFlag(LoginActivity.class,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
        }
    }

//    @OnClick({R.id.img_header, R.id.rl_collect, R.id.rl_skin, R.id.rl_logout})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.img_header:
//
//                break;
//            case R.id.rl_collect:
//                navigateTo(MyCollectActivity.class);
//                break;
//            case R.id.rl_skin:
//                String skin = findByKey("skin");
//                if (skin.equals("night")) {
//                    // 恢复应用默认皮肤
//                    SkinCompatManager.getInstance().restoreDefaultTheme();
//                    insertVal("skin", "default");
//                } else {
//                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
//                    insertVal("skin", "night");
//                }
//                break;
//            case R.id.rl_logout:
//                removeByKey("token");
//                navigateToWithFlag(LoginActivity.class,
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                break;
//        }
//    }
}
