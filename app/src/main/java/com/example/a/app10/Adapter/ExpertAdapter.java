package com.example.a.app10.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.a.app10.R;
import com.example.a.app10.bean.ExpertSearchItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 12917 on 2017/6/3.
 */

public class ExpertAdapter extends RecyclerView.Adapter<ExpertAdapter.ViewHolder> {
    private Context context;
    private List<ExpertSearchItem> list;
    private ItemOnClickListener itemOnClickListener;
    public ExpertAdapter(Context context,List<ExpertSearchItem> list) {
        super();
        this.context=context;
        this.list=list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImageUrl()).into(holder.imageView);
        holder.textView.setText(list.get(position).getKey());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.expert_item,parent,false),itemOnClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView textView;
        public ViewHolder(View itemView,ItemOnClickListener itemOnClickListener)  {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.expert_image);
            textView=(TextView)itemView.findViewById(R.id.expert_text);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            if (itemOnClickListener != null) {
                itemOnClickListener.onClick(v,getPosition());
            }

        }
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public interface ItemOnClickListener{
        public void onClick(View view, int position);
    }
}
