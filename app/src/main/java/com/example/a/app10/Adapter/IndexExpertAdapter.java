package com.example.a.app10.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a.app10.R;
import com.example.a.app10.bean.ExpertItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 12917 on 2017/6/2.
 */

public class IndexExpertAdapter extends RecyclerView.Adapter<IndexExpertAdapter.ViewHolder> {

    private IndexExpertAdapter.ItemOnClickListener itemOnClickListener;
    private  List<ExpertItem> list;
    private Context context;
    public IndexExpertAdapter(Context context, List<ExpertItem> list) {
        super();
        this.context=context;
        this.list=list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImageUrl()).into(holder.imageView);
        holder.name.setText(list.get(position).getExpertName());
        holder.type.setText(list.get(position).getExpertArea());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.index_expert_item,parent,false),itemOnClickListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView name;
        TextView type;
        public ViewHolder(View itemView,ItemOnClickListener itemOnClickListener) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.expert_item_image);
            name=(TextView)itemView.findViewById(R.id.expert_item_name);
            type=(TextView)itemView.findViewById(R.id.expert_item_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemOnClickListener.onClick(v,getPosition());
        }
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public interface ItemOnClickListener{
        public void onClick(View view, int position);
    }
}
