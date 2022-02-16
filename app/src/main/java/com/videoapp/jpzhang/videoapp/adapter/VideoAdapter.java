package com.videoapp.jpzhang.videoapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dueeeke.videocontroller.component.PrepareView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.videoapp.jpzhang.videoapp.R;
import com.videoapp.jpzhang.videoapp.api.Api;
import com.videoapp.jpzhang.videoapp.api.ApiConfig;
import com.videoapp.jpzhang.videoapp.api.TtitCallback;
import com.videoapp.jpzhang.videoapp.entity.BaseResponse;
import com.videoapp.jpzhang.videoapp.entity.VideoEntity;
import com.videoapp.jpzhang.videoapp.listener.OnItemChildClickListener;
import com.videoapp.jpzhang.videoapp.listener.OnItemClickListener;
import com.videoapp.jpzhang.videoapp.util.StringUtils;
import com.videoapp.jpzhang.videoapp.view.CircleTransform;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<VideoEntity> datas;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemClickListener mOnItemClickListener;

    public void setDatas(List<VideoEntity> datas) {
        this.datas = datas;
    }

    public VideoAdapter(Context context) {
        this.mContext = context;
    }

    public VideoAdapter(Context context, List<VideoEntity> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(mContext).inflate(R.layout.item_video_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        VideoEntity videoEntity = datas.get(position);
        vh.tvTitle.setText(videoEntity.getVtitle());
        vh.tvAuthor.setText(videoEntity.getAuthor());
//        vh.tvDz.setText(String.valueOf(videoEntity.getLikeNum()));
//        vh.tvComment.setText(String.valueOf(videoEntity.getCommentNum()));
//        vh.tvCollect.setText(String.valueOf(videoEntity.getCollectNum()));
            int likenum = 0;
            int commentnum = 0;
            int collectnum = 0;
            boolean flagLike = false;
            boolean flagCollect = false;
        if (videoEntity.getVideoSocialEntity() != null) {
            likenum = videoEntity.getVideoSocialEntity().getLikenum();
            commentnum = videoEntity.getVideoSocialEntity().getCommentnum();
            collectnum = videoEntity.getVideoSocialEntity().getCollectnum();
            flagLike = videoEntity.getVideoSocialEntity().isFlagLike();
            flagCollect = videoEntity.getVideoSocialEntity().isFlagCollect();
//        } else {
//            likenum = videoEntity.getLikeNum();
//            commentnum = videoEntity.getCommentNum();
//            collectnum = videoEntity.getCollectNum();
//        }
            if (flagLike) {
                vh.tvDz.setTextColor(Color.parseColor("#E21918"));
                vh.imgDizan.setImageResource(R.mipmap.dianzan_select);
            }
            if (flagCollect) {
                vh.tvCollect.setTextColor(Color.parseColor("#E21918"));
                vh.imgCollect.setImageResource(R.mipmap.collect_select);
            }
            vh.tvDz.setText(String.valueOf(likenum));
            vh.tvComment.setText(String.valueOf(commentnum));
            vh.tvCollect.setText(String.valueOf(collectnum));
            vh.flagCollect = flagCollect;
            vh.flagLike = flagLike;
        }
        Picasso.with(mContext)
                .load(videoEntity.getHeadurl())
                .transform(new CircleTransform())
                .into(vh.imgHeader);
        Picasso.with(mContext)
                .load(videoEntity.getCoverurl())
                .into(vh.mThumb);
        vh.mPosition = position;

    }

    @Override
    public int getItemCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvDz;
        private TextView tvComment;
        private TextView tvCollect;
        private ImageView imgHeader;
//        private ImageView imgCover;
        private ImageView imgCollect;
        private ImageView imgDizan;
        public ImageView mThumb;//视频缩略图
        public PrepareView mPrepareView;//视频
        public FrameLayout mPlayerContainer;//视频容器
        public int mPosition;
        private boolean flagCollect;
        private boolean flagLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title);
            tvAuthor = itemView.findViewById(R.id.author);
            tvDz = itemView.findViewById(R.id.dz);
            tvComment = itemView.findViewById(R.id.comment);
            tvCollect = itemView.findViewById(R.id.collect);
            imgHeader = itemView.findViewById(R.id.img_header);
            mPlayerContainer = itemView.findViewById(R.id.player_container);
            mPrepareView = itemView.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(R.id.thumb);
            imgCollect = itemView.findViewById(R.id.img_collect);
            imgDizan = itemView.findViewById(R.id.img_like);
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
            imgCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int collectNum = Integer.parseInt(tvCollect.getText().toString());
                    if (flagCollect) { //已收藏
                        if (collectNum > 0) {
                            tvCollect.setText(String.valueOf(--collectNum));
                            tvCollect.setTextColor(Color.parseColor("#161616"));
                            imgCollect.setImageResource(R.mipmap.collect);
                            updateCount(datas.get(mPosition).getVid(), 1, !flagCollect);
                        }
                    } else {//未收藏
                        tvCollect.setText(String.valueOf(++collectNum));
                        tvCollect.setTextColor(Color.parseColor("#E21918"));
                        imgCollect.setImageResource(R.mipmap.collect_select);
                        updateCount(datas.get(mPosition).getVid(), 1, !flagCollect);
                    }
                    flagCollect = !flagCollect;
                }
            });
            imgDizan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int likeNum = Integer.parseInt(tvDz.getText().toString());
                    if (flagLike) { //已点赞
                        if (likeNum > 0) {
                            tvDz.setText(String.valueOf(--likeNum));
                            tvDz.setTextColor(Color.parseColor("#161616"));
                            imgDizan.setImageResource(R.mipmap.dianzan);
                            updateCount(datas.get(mPosition).getVid(), 2, !flagLike);
                        }
                    } else {//未点赞
                        tvDz.setText(String.valueOf(++likeNum));
                        tvDz.setTextColor(Color.parseColor("#E21918"));
                        imgDizan.setImageResource(R.mipmap.dianzan_select);
                        updateCount(datas.get(mPosition).getVid(), 2, !flagLike);
                    }
                    flagLike = !flagLike;
                }
            });
            //通过tag将ViewHolder和itemView绑定
            itemView.setTag(this);
        }

        private void updateCount(int vid, int type, boolean flag) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("vid", vid);
            params.put("type", type);
            params.put("flag", flag);
            Api.config(ApiConfig.VIDEO_UPDATE_COUNT, params).postRequest(mContext, new TtitCallback() {
                @Override
                public void onSuccess(final String res) {
                    Log.e("onSuccess", res);
                    Gson gson = new Gson();
                    BaseResponse baseResponse = gson.fromJson(res, BaseResponse.class);
                    if (baseResponse.getCode() == 0) {

                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_container) {
                if (mOnItemChildClickListener != null) {
                    mOnItemChildClickListener.onItemChildClick(mPosition);
                }
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mPosition);
                }
            }
        }
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
