package com.videoapp.jpzhang.videoapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.videoapp.jpzhang.videoapp.R;
import com.videoapp.jpzhang.videoapp.adapter.HomeAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "热门","IOS","Android","前端"
            ,"后端","设计","工具资源"
    };
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

//    @Override
//    protected int initLayout() {
//        return R.layout.fragment_home;
//    }
//
//    @Override
//    protected void initView() {
//
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        viewPager = v.findViewById(R.id.fixedViewPager);
        slidingTabLayout = v.findViewById(R.id.slidingTabLayout);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (String tittle : mTitles) {
            mFragments.add(VideoFragment.newInstance(tittle));
        }
        HomeAdapter homeAdapter = new HomeAdapter(getFragmentManager(),mTitles,mFragments);
        //更新后用的包不一样，但是不影响使用
        viewPager.setAdapter(homeAdapter);
        slidingTabLayout.setViewPager(viewPager);
    }
//
//    @Override
//    protected void initData() {
//       // getVideoCategoryList();
//    }

//    private void getVideoCategoryList() {
//        HashMap<String, Object> params = new HashMap<>();
//        Api.config(ApiConfig.VIDEO_CATEGORY_LIST, params).getRequest(getActivity(), new TtitCallback() {
//            @Override
//            public void onSuccess(final String res) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        VideoCategoryResponse response = new Gson().fromJson(res, VideoCategoryResponse.class);
//                        if (response != null && response.getCode() == 0) {
//                            List<CategoryEntity> list = response.getPage().getList();
//                            if (list != null && list.size() > 0) {
//                                mTitles = new String[list.size()];
//                                for (int i = 0; i < list.size(); i++) {
//                                    mTitles[i] = list.get(i).getCategoryName();
//                                    mFragments.add(VideoFragment.newInstance(list.get(i).getCategoryId()));
//                                }
//                                viewPager.setOffscreenPageLimit(mFragments.size());
//                                viewPager.setAdapter(new HomeAdapter(getFragmentManager(), mTitles, mFragments));
//                                slidingTabLayout.setViewPager(viewPager);
//                            }
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//            }
//        });
//    }
}
