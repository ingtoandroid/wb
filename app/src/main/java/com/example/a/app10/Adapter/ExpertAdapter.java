package com.example.a.app10.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.a.app10.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 12917 on 2017/6/3.
 */

public class ExpertAdapter extends RecyclerView.Adapter<ExpertAdapter.ViewHolder> {
    private ArrayList<HashMap<String,Object>> list;
    private Context context;
    private ItemOnClickListener itemOnClickListener;
    public ExpertAdapter(Context context) {
        super();
        TypedArray typedArray=context.getResources().obtainTypedArray(R.array.expert_tabs);
        TypedArray typedArray1=context.getResources().obtainTypedArray(R.array.expert_tab_text);
        this.context=context;
        list=new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<8;i++){
            HashMap<String,Object> map=new HashMap<String,Object>();
            map.put("image",typedArray.getResourceId(i,0));
            map.put("text",typedArray1.getString(i));
            list.add(map);
        }
        typedArray.recycle();
        typedArray1.recycle();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageResource((int)list.get(position).get("image"));
        holder.textView.setText(list.get(position).get("text").toString());

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
