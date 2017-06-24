package com.example.a.app10.Adapter;

import android.content.ClipData;
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
import com.example.a.app10.bean.QuestionDetail;
import com.example.a.app10.bean.QuestionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 12917 on 2017/6/3.
 */

public class TiWenAdapter extends  RecyclerView.Adapter<TiWenAdapter.ViewHolder>  {
    private List<QuestionItem> list;
    private Context context;
    private ItemOnClickListener itemOnClickListener;
    public TiWenAdapter(Context context,List<QuestionItem> list) {
        super();
        this.context=context;
        this.list=list;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getQuestionContent());
        holder.time.setText(list.get(position).getCreateTime_sys());
        //holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),));
        Glide.with(context).load(list.get(position).getPhotoUrl()).into(holder.imageView);
//        holder.question.setText(list.get(position).getQuestionContent());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.index_tiwen_item,parent,false),itemOnClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;
        TextView time;
        TextView question;
        TextView answer;
        public ViewHolder(View itemView, final ItemOnClickListener itemOnClickListener) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.index_tiwen_item_iamge);
            title=(TextView)itemView.findViewById(R.id.index_tiwen_index_title);
            time=(TextView)itemView.findViewById(R.id.index_tiwen_index_time);
            question=(TextView)itemView.findViewById(R.id.index_tiwen_item_question);
            //answer=(TextView)itemView.findViewById(R.id.index_tiwen_item_answer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickListener.onClick(v,getPosition());
                }
            });
        }
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public interface ItemOnClickListener{
        public void onClick(View view, int position);
    }
}
