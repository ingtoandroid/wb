package com.example.a.app10.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a.app10.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 12917 on 2017/6/10.
 */

public class VideoRecycleAdapter extends RecyclerView.Adapter<VideoRecycleAdapter.ViewHoler> {
    private Context context;
    private ArrayList<HashMap<String,Object>> list=null;
    public VideoRecycleAdapter(Context context) {
        super();
        this.context=context;
        list=new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<5;i++){
            HashMap<String,Object> map=new HashMap<String,Object>();
            map.put("title","今天有雨");
            map.put("time","201701.1");
            map.put("comment","这里是评论内容，这里是评论内容");
            list.add(map);
        }
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        holder.title.setText(list.get(position).get("title").toString());
        holder.time.setText(list.get(position).get("time").toString());
        holder.comment.setText(list.get(position).get("comment").toString());
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
