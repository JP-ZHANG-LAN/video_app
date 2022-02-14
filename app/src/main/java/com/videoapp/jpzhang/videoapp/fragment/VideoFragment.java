package com.videoapp.jpzhang.videoapp.fragment;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.videoapp.jpzhang.videoapp.R;
import com.videoapp.jpzhang.videoapp.activity.LoginActivity;
import com.videoapp.jpzhang.videoapp.adapter.VideoAdapter;
import com.videoapp.jpzhang.videoapp.api.Api;
import com.videoapp.jpzhang.videoapp.api.ApiConfig;
import com.videoapp.jpzhang.videoapp.api.TtitCallback;
import com.videoapp.jpzhang.videoapp.entity.VideoEntity;
import com.videoapp.jpzhang.videoapp.entity.VideoListResponse;
import com.videoapp.jpzhang.videoapp.listener.OnItemChildClickListener;
import com.videoapp.jpzhang.videoapp.util.StringUtils;
import com.videoapp.jpzhang.videoapp.util.Tag;
import com.videoapp.jpzhang.videoapp.util.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoFragment extends BaseFragment implements OnItemChildClickListener{

//
//    @Override
//    public void onItemChildClick(int position) {
//
//    }

    private String tittle;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private int pageNum = 1;
    private VideoAdapter videoAdapter;
    private List<VideoEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;
    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;
//
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    videoAdapter.setDatas(datas);
//                    videoAdapter.notifyDataSetChanged();
//                    break;
//            }
//        }
//    };
//
//    public VideoFragment() {
//    }
//
    @Override
    protected int initLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        initVideoView();
        recyclerView = mRootView.findViewById(R.id.recyclerView);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        videoAdapter = new VideoAdapter(getActivity());
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.setOnItemChildClickListener(this);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
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
//        List<VideoEntity> datas = new ArrayList<>();
//        for (int i = 0;i<8;i++) {
//            VideoEntity ve = new VideoEntity();
//            ve.setVtitle("======"+i);
//            ve.setAuthor(String.valueOf(i));
//            ve.setCollectnum(i+6);
//            ve.setCommentnum(i*2);
//            ve.setLikenum(i*5);
//            datas.add(ve);
//        }
        getVideoList(true);
    }


    public static VideoFragment newInstance(String tittle) {
        VideoFragment fragment = new VideoFragment();
        fragment.tittle = tittle;
        return fragment;
    }

    public void getVideoList(Boolean isRefresh){
        String s = findByKey("token");
        if(!StringUtils.isEmpty(s)){
            HashMap<String, Object> params = new HashMap<>();
            params.put("token",s);
            params.put("page", pageNum);
            params.put("limit", ApiConfig.PAGE_SIZE);
            Api.config(ApiConfig.VIDEO_LIST,params).getRequest(getActivity(), new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(isRefresh){
                                refreshLayout.finishRefresh(true);
                            } else {
                                refreshLayout.finishLoadMore(true);
                            }
                            //把json转换为实体
                            VideoListResponse videoListResponse = new Gson().fromJson(res,VideoListResponse.class);
                            if(videoListResponse != null && videoListResponse.getCode() == 0) {
                                List<VideoEntity> list = videoListResponse.getPage().getList();
                                if(list != null && list.size() > 0){
                                    if(isRefresh){
                                        datas = list;
                                    } else {
                                        datas.addAll(list);
                                    }
                                    videoAdapter.setDatas(datas);
                                    videoAdapter.notifyDataSetChanged();
                                } else {
                                    if (isRefresh) {
                                        showToast("暂时无数据");
                                    } else {
                                        showToast("没有更多数据");
                                    }
                                }
                            }
                        }
                    });
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
        } else {
            navigateTo(LoginActivity.class);
        }

    }

//    public static VideoFragment newInstance(int categoryId) {
//        VideoFragment fragment = new VideoFragment();
//        fragment.categoryId = categoryId;
//        return fragment;
//    }
//

    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new com.dueeeke.videoplayer.player.VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == com.dueeeke.videoplayer.player.VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    protected void pause() {
        releaseVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    protected void resume() {
        if (mLastPos == -1)
            return;
        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    /**
     * PrepareView被点击
     */
    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    /**
     * 开始播放
     *
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoEntity videoEntity = datas.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(videoEntity.getPlayurl());
        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = linearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;

    }

    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }
}