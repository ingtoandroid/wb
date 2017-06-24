package com.example.a.app10.Adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a.app10.R;
import com.example.a.app10.bean.ShipinItem;

import java.util.List;

/**
 * Created by lenovo on 2017/6/9.
 * 课程列表的适配器
 */

public class OrderAdapter extends BaseAdapter implements SpinnerAdapter {

    private List<String> texts;
    private List<Boolean> list;
    private Context context;

    public OrderAdapter(Context context,List<String> texts, List<Boolean> list) {
        this.texts = texts;
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv=new TextView(context);
        tv.setText(texts.get(i));
        if (list.get(i)){
            tv.setTextColor(Color.GREEN);
        } else {
            tv.setTextColor(context.getResources().getColor(R.color.stringLight));
            tv.setOnClickListener(null);
            tv.setClickable(false);
        }
        return tv;
    }
}