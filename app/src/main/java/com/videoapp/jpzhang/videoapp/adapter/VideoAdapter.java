package com.videoapp.jpzhang.videoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dueeeke.videocontroller.component.PrepareView;
import com.squareup.picasso.Picasso;
import com.videoapp.jpzhang.videoapp.R;
import com.videoapp.jpzhang.videoapp.entity.VideoEntity;
import com.videoapp.jpzhang.videoapp.listener.OnItemChildClickListener;
import com.videoapp.jpzhang.videoapp.listener.OnItemClickListener;
import com.videoapp.jpzhang.videoapp.view.CircleTransform;

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
            vh.tvDz.setText(String.valueOf(videoEntity.getLikeNum()));
        vh.tvComment.setText(String.valueOf(videoEntity.getCommentNum()));
        vh.tvCollect.setText(String.valueOf(videoEntity.getCollectNum()));

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
//        private ImageView imgDizan;
        public ImageView mThumb;//视频缩略图
        public PrepareView mPrepareView;//视频
        public FrameLayout mPlayerContainer;//视频容器
        public int mPosition;
//        private boolean flagCollect;
//        private boolean flagLike;

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
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
            //通过tag将ViewHolder和itemView绑定
            itemView.setTag(this);
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
