package com.videoapp.jpzhang.videoapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.videoapp.jpzhang.videoapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {

    @BindView(R.id.img_header)
    ImageView imgHeader;

    public MyFragment() {
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

//    @Override
//    protected int initLayout() {
//        return R.layout.fragment_my;
//    }
//
//    @Override
//    protected void initView() {
//
//    }
//
//    @Override
//    protected void initData() {
//
//    }

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
