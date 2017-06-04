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
 * Created by 12917 on 2017/6/3.
 */

public class TiWenAdapter extends  RecyclerView.Adapter<TiWenAdapter.ViewHolder>  {
    private ArrayList<HashMap<String,Object>> list=null;
    private Context context;
    public TiWenAdapter(Context context) {
        super();
        this.context=context;
        list=new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<2;i++){
            HashMap<String,Object> map=new HashMap<>();
            map.put("title","今天有雨");
            map.put("time","2017.04.14");
            map.put("question","骑自行车可以瘦腿嘛");
            map.put("answer","貌似，好像，可以的");
            list.add(map);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(list.get(position).get("title").toString());
        holder.time.setText(list.get(position).get("time").toString());
        //holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),));
        holder.question.setText(list.get(position).get("question").toString());
        holder.answer.setText(list.get(position).get("answer").toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.index_tiwen_item,parent,false));
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
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.index_tiwen_item_iamge);
            title=(TextView)itemView.findViewById(R.id.index_tiwen_index_title);
            time=(TextView)itemView.findViewById(R.id.index_tiwen_index_time);
            question=(TextView)itemView.findViewById(R.id.index_tiwen_item_question);
            answer=(TextView)itemView.findViewById(R.id.index_tiwen_item_answer);
        }
    }
}
