package com.example.a.app10.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a.app10.Activity.Professor;
import com.example.a.app10.Activity.ProfessorActivity;
import com.example.a.app10.Adapter.ExpertAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.ExpertSearchItem;
import com.example.a.app10.tool.MyInternet;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExpertFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView expertRecyclerview;
    private ExpertAdapter expertAdapter;
    private ProgressDialog mProgressDialog;
    private List<ExpertSearchItem> list;
    private OkHttpClient client;


    public ExpertFragment() {
    }


    public static ExpertFragment newInstance(String param1, String param2) {
        ExpertFragment fragment = new ExpertFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_expert,container,false);
        expertRecyclerview=(RecyclerView)view.findViewById(R.id.expert_recyclerview);
        init();
        showProgress("加载中");
        getData();
        return view;
    }

    private void getData() {
        String url= MyInternet.MAIN_URL+"expert/get_expert_area";
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject all=new JSONObject(s);
                    JSONArray array=all.getJSONArray("yjfxList");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        list.add(new ExpertSearchItem(obj.getString("key"),
                                obj.getString("value"),obj.getString("filePath")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mainThread() {
                show();
            }
        },getActivity());
    }

    private void show() {
        hideProgress();
        expertAdapter=new ExpertAdapter(getContext(),list);
        expertAdapter.setItemOnClickListener(new ExpertAdapter.ItemOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(getContext(),ProfessorActivity.class);
                intent.putExtra("expertArea",list.get(position).getValue());
                startActivity(intent);
            }
        });
        expertRecyclerview.setAdapter(expertAdapter);
    }

    private void init(){
        list=new ArrayList<>();
        client=new OkHttpClient();
        expertAdapter=new ExpertAdapter(getContext(),list);
        expertRecyclerview.setLayoutManager(new GridLayoutManager(getContext(),4));
        expertRecyclerview.setAdapter(expertAdapter);
    }


    //显示进度对话框
    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
