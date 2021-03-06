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
 * Created by lenovo on 2017/6/9.
 * 课程列表的适配器
 */

    public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder> {

    private List<ClassItem> list;
    private Context context;
    private OnItenClickListener listener;

    public interface OnItenClickListener {
        public void onItemClick(View view, int position);
        public  void onItemLongClick(View view,int position);

    }

    public void setLisenter(ClassAdapter.OnItenClickListener lisenter){
        this.listener=lisenter;
    }

    public ClassAdapter(List<ClassItem> list, Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder=new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.class_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
            ClassItem item=list.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvNumber.setText(item.getNumber()+"人已参加");
        holder.tvTime.setText("开课时间："+item.getTime());
        if (item.getImgUrl().length()>1){
            Glide.with(context).load(item.getImgUrl()).into(holder.iv);
        }

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