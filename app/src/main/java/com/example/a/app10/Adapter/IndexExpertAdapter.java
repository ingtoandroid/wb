package com.example.a.app10.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
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
 * Created by 12917 on 2017/6/2.
 */

public class IndexExpertAdapter extends RecyclerView.Adapter<IndexExpertAdapter.ViewHolder> {

    private IndexExpertAdapter.ItemOnClickListener itemOnClickListener;
    private  ArrayList<HashMap<String,Object>> list;
    private Context context;
    public IndexExpertAdapter(Context context) {
        super();
        this.context=context;
        list=new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<5;i++){
            HashMap<String,Object> map=new HashMap<String,Object>();
            map.put("image", BitmapFactory.decodeResource(context.getResources(),R.drawable.expert));
            map.put("name","Mikie");
            map.put("type","有氧训练");
            list.add(map);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.imageView.setImageBitmap((Bitmap)list.get(position).get("image"));
        holder.name.setText(list.get(position).get("name").toString());
        holder.type.setText(list.get(position).get("type").toString());

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
