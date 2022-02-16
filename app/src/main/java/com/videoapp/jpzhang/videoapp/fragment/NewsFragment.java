package com.videoapp.jpzhang.videoapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.videoapp.jpzhang.videoapp.R;
import com.videoapp.jpzhang.videoapp.activity.LoginActivity;
import com.videoapp.jpzhang.videoapp.activity.WebActivity;
import com.videoapp.jpzhang.videoapp.adapter.NewsAdapter;
import com.videoapp.jpzhang.videoapp.adapter.VideoAdapter;
import com.videoapp.jpzhang.videoapp.api.Api;
import com.videoapp.jpzhang.videoapp.api.ApiConfig;
import com.videoapp.jpzhang.videoapp.api.TtitCallback;
import com.videoapp.jpzhang.videoapp.entity.NewsEntity;
import com.videoapp.jpzhang.videoapp.entity.NewsListResponse;
import com.videoapp.jpzhang.videoapp.entity.VideoEntity;
import com.videoapp.jpzhang.videoapp.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewsFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private NewsAdapter newsAdapter;
    private List<NewsEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private int pageNum = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    newsAdapter.setDatas(datas);
                    newsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.recyclerView);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsAdapter(getActivity());
//        for (int i=0;i<15;i++) {
//            int type = i % 3 + 1;
//            NewsEntity newsEntity = new NewsEntity();
//            newsEntity.setType(type);
//            datas.add(newsEntity);
//        }
//        newsAdapter.setDatas(datas);
        recyclerView.setAdapter(newsAdapter);
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Serializable obj) {
//                showToast("点击");
                NewsEntity newsEntity = (NewsEntity) obj;
                String url = "http://192.168.31.32:8089/newsDetail?title=" + newsEntity.getAuthorName();
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                navigateToWithBundle(WebActivity.class, bundle);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getVideoList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pageNum++;
                getVideoList(false);
            }
        });
        getVideoList(true);
    }
    public void getVideoList(Boolean isRefresh){
        String s = findByKey("token");
            HashMap<String, Object> params = new HashMap<>();
            params.put("token",s);
            params.put("page", pageNum);
            params.put("limit", ApiConfig.PAGE_SIZE);
            Api.config(ApiConfig.NEWS_LIST,params).getRequest(getActivity(), new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    if(isRefresh){
                        refreshLayout.finishRefresh(true);
                    } else {
                        refreshLayout.finishLoadMore(true);
                    }
                    //把json转换为实体
                    NewsListResponse newsListResponse = new Gson().fromJson(res,NewsListResponse.class);
                    if(newsListResponse != null && newsListResponse.getCode() == 0) {
                        List<NewsEntity> list = newsListResponse.getPage().getList();
                        if(list != null && list.size() > 0){
                            if(isRefresh){
                                datas = list;
                            } else {
                                datas.addAll(list);
                            }
                            mHandler.sendEmptyMessage(0);
                        } else {
                            if (isRefresh) {
                                showToastSync("暂时无数据");
                            } else {
                                showToastSync("没有更多数据");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh(true);
                    } else {
                        refreshLayout.finishLoadMore(true);
                    }
                }
            });
    }
}
