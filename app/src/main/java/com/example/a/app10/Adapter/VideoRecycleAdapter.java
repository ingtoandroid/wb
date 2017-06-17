package com.example.a.app10.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a.app10.R;
import com.example.a.app10.bean.CommentItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 12917 on 2017/6/10.
 */

public class VideoRecycleAdapter extends RecyclerView.Adapter<VideoRecycleAdapter.ViewHoler> {
    private List<CommentItem>  list=null;
    private Context context;
    public VideoRecycleAdapter(Context context,List<CommentItem> list) {
        super();
        this.context=context;
        this.list=list;
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        holder.title.setText(list.get(position).getUserName());
        holder.time.setText(list.get(position).getCreateTime_sys());
        holder.comment.setText(list.get(position).getQuestionContent());
        Glide.with(context).load(list.get(position).getPhotoUrl()).into(holder.imageView);

    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoler(LayoutInflater.from(context).inflate(R.layout.video_pinglun_item,parent,false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView title;
        private TextView time;
        private TextView comment;
        public ViewHoler(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.video_item_iamge);
            title=(TextView)itemView.findViewById(R.id.video_item_title);
            time=(TextView)itemView.findViewById(R.id.video_item_time);
            comment=(TextView)itemView.findViewById(R.id.video_item_comment);
        }
    }
}
