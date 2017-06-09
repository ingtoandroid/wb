package com.example.a.app10.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.a.app10.R;
import com.example.a.app10.bean.MyClassItem;

import java.util.List;

/**
 * Created by lenovo on 2017/6/7.
 */

public class MyClassAdapter extends RecyclerView.Adapter<MyClassAdapter.MyViewHolder> {

    private List<MyClassItem> list;
    private Context context;
    private OnItenClickListener listener;

    public interface OnItenClickListener {
        public void onItemClick(View view, int position);
        public  void onItemLongClick(View view,int position);

    }

    public void setLisenter(OnItenClickListener lisenter){
        this.listener=lisenter;
    }

    public MyClassAdapter(List<MyClassItem> list, Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder=new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.myclass_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        MyClassItem item=list.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvNumber.setText("评论 "+item.getCommentNumber()+"  提问 "+item.getQuestionNumber());
        holder.tvFinish.setText(item.isIfFinish()? "已学完":"未学完");
        holder.iv.setImageBitmap(item.getBitmap());

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
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle,tvNumber,tvFinish;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.tvTitle);
            tvNumber= (TextView) itemView.findViewById(R.id.tvNumber);
            tvFinish= (TextView) itemView.findViewById(R.id.tvFinish);
            iv= (ImageView) itemView.findViewById(R.id.image);
        }
    }
}