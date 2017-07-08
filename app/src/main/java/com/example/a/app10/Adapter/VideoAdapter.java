package com.example.a.app10.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a.app10.R;
import com.example.a.app10.bean.ShipinItem;

import java.util.List;

/**
 * Created by 12917 on 2017/6/15.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private List<ShipinItem> list;
    private Context context;
    private VideoAdapter.OnItenClickListener listener;
    public interface OnItenClickListener {
        public void onItemClick(View view, int position);
        public  void onItemLongClick(View view,int position);

    }

    public void setListener(OnItenClickListener listener) {
        this.listener = listener;
    }

    public VideoAdapter(List<ShipinItem> list,Context context) {
        this.list=list;
        this.context=context;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ShipinItem item=list.get(position);
        holder.tvTitle.setText(item.getVideoTitle());
        holder.tvNumber.setText(item.getPlayNum()+"人已参加");
        holder.tvTime.setText("类型: "+item.getVideoType());
        Glide.with(context).load(item.getImageUrl()).into(holder.iv);

        if (listener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view,position);
                }

            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(view,position);
                    return false;
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VideoAdapter.MyViewHolder holder=new VideoAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.class_item,parent,false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle,tvNumber,tvTime;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.tvTitle);
            tvNumber= (TextView) itemView.findViewById(R.id.tvNumber);
            tvTime= (TextView) itemView.findViewById(R.id.tvTime);
            iv= (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
