package com.example.a.app10.Adapter;

import android.content.Context;
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

public class ShipinAdapter extends RecyclerView.Adapter<ShipinAdapter.ViewHolder> {
    private ArrayList<HashMap<String,Object>> list;
    private Context context;
    private ItemOnClickListener itemOnClickListener;
    public ShipinAdapter(Context context) {
        super();
        list=new ArrayList<HashMap<String, Object>>();
        this.context=context;
        for(int i=0;i<2;i++){
            HashMap<String,Object> map=new HashMap<String,Object>();
            map.put("title","Hiit适应性训练");
            map.put("count","123456");
            map.put("time","2017.10.10");
            list.add(map);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(list.get(position).get("title").toString());
        holder.count.setText("已有"+list.get(position).get("count").toString()+"人参加");
        holder.time.setText(list.get(position).get("time").toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.index_shipin_item,parent,false),itemOnClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView title;
        private TextView count;
        private TextView time;
        public ViewHolder(View itemView,ItemOnClickListener itemOnClickListener) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.index_shipin_item_image);
            title=(TextView)itemView.findViewById(R.id.index_shipin_item_title);
            count=(TextView)itemView.findViewById(R.id.index_shipin_item_count);
            time=(TextView)itemView.findViewById(R.id.index_shipin_item_time);
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
