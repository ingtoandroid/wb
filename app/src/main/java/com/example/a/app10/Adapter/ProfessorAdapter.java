package com.example.a.app10.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.a.app10.R;
import com.example.a.app10.bean.ProfessorItem;

import java.util.List;

/**
 * Created by lenovo on 2017/6/4.
 */

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.MyViewHolder> {

    private List<ProfessorItem> list;
    private Context context;
    private Resources resources;
    private ProfessorAdapter.OnItenClickListener listener;
    private int[] gradeImageIds={R.drawable.star1, R.drawable.star2,R.drawable.star3,R.drawable.star4,R.drawable.star5};

    public interface OnItenClickListener {
        public void onItemClick(View view, int position);
        public  void onItemLongClick(View view,int position);

    }

    public void setLisenter(ProfessorAdapter.OnItenClickListener lisenter){
        this.listener=lisenter;
    }

    public ProfessorAdapter(List<ProfessorItem> list, Context context){
        this.list=list;
        this.context=context;
        this.resources=context.getResources();
    }

    @Override
    public ProfessorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProfessorAdapter.MyViewHolder holder=new ProfessorAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.professor_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ProfessorAdapter.MyViewHolder holder, final int position) {
        ProfessorItem item=list.get(position);
        holder.tvName.setText(item.getName());
        holder.tvContent.setText(item.getContent());
        holder.image.setImageBitmap(item.getImage());
        holder.ivGrade.setImageBitmap(BitmapFactory.decodeResource(resources,gradeImageIds[item.getGrade()-1]));

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

        TextView tvName,tvContent;
        ImageView image,ivGrade;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.tvName);
            tvContent= (TextView) itemView.findViewById(R.id.tvContent);
            image= (ImageView) itemView.findViewById(R.id.image);
            ivGrade= (ImageView) itemView.findViewById(R.id.ivGrade);
        }
    }
}
