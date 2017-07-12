package com.example.a.app10.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.a.app10.Activity.ClassActivity;
import com.example.a.app10.Activity.NewsActivity;
import com.example.a.app10.Activity.Professor;
import com.example.a.app10.Activity.ProfessorDetailActivity;
import com.example.a.app10.Activity.QuestionDetailedActivity;
import com.example.a.app10.Activity.QuickQuestionActivity;
import com.example.a.app10.Activity.ScienceActivity;
import com.example.a.app10.Activity.ShipinActivity;
import com.example.a.app10.Activity.VideoDetail;
import com.example.a.app10.Adapter.IndexExpertAdapter;
import com.example.a.app10.Adapter.ShipinAdapter;
import com.example.a.app10.Adapter.TiWenAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.ExpertItem;
import com.example.a.app10.bean.ProfessorItem;
import com.example.a.app10.bean.QuestionDetail;
import com.example.a.app10.bean.QuestionItem;
import com.example.a.app10.bean.ShipinItem;
import com.example.a.app10.tool.Net;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView indexExpertRecycleView;
    private IndexExpertAdapter indexExpertAdapter;
    private RecyclerView indexTiwenRecycleView;
    private TiWenAdapter tiWenAdapter;
    private ShipinAdapter shipinAdapter;
    private RecyclerView indexShipinRecyclerView;
    private LinearLayout quick_question;
    private LinearLayout findExpert;
    private LinearLayout kepu;
    private LinearLayout shipin;
    private LinearLayout ketang;
    private LinearLayout zixun;
    private List<ExpertItem> list1;
    private List<QuestionItem> list2;
    private List<ShipinItem> list3;

    public IndexFragment() {

    }
    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
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
        View view=inflater.inflate(R.layout.fragment_index, container, false);
        indexExpertRecycleView =(RecyclerView)view.findViewById(R.id.index_recycleview);
        indexTiwenRecycleView=(RecyclerView)view.findViewById(R.id.index_tiwen_recycleview);
        indexShipinRecyclerView=(RecyclerView)view.findViewById(R.id.index_shipin_recyclerview);
        quick_question=(LinearLayout)view.findViewById(R.id.quick_question);
        findExpert=(LinearLayout)view.findViewById(R.id.line1);
        kepu=(LinearLayout)view.findViewById(R.id.index_kepu);
        shipin=(LinearLayout)view.findViewById(R.id.index_shipin);
        ketang=(LinearLayout)view.findViewById(R.id.index_ketang);
        zixun=(LinearLayout)view.findViewById(R.id.index_zixun);
        getData();
        init();
        return view;
    }

    private void initEvent(){

    }
    private void init(){
        quick_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), QuickQuestionActivity.class);
                startActivity(intent);
            }
        });

        findExpert.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Professor.class);
                startActivity(intent);
            }
        });

        kepu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ScienceActivity.class);
                startActivity(intent);
            }
        });
        shipin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShipinActivity.class);
                startActivity(intent);
            }
        });
        ketang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ClassActivity.class);
                startActivity(intent);
            }
        });
        zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), NewsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData(){
        Call call=Net.getInstance().getExpertList();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String s=response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray=jsonObject.getJSONArray("datalist");
                    Gson gson=new Gson();
                    list1=gson.fromJson(jsonArray.toString(),new TypeToken<List<ExpertItem>>(){}.getType());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            indexExpertAdapter =new IndexExpertAdapter(getContext(),list1);
                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL,false);
                            indexExpertRecycleView.setLayoutManager(linearLayoutManager);
                            indexExpertRecycleView.setAdapter(indexExpertAdapter);
                            indexExpertAdapter.setItemOnClickListener(new IndexExpertAdapter.ItemOnClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    Intent intent=new Intent(getContext(), ProfessorDetailActivity.class);
                                    intent.putExtra("expertId",list1.get(position).getExpertId());
                                    intent.putExtra("imageUrl",list1.get(position).getImageUrl());
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
                catch (JSONException E){
                    E.printStackTrace();
                }
            }
        });
        Call call1=Net.getInstance().getQuestionList();
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                list2=new ArrayList<QuestionItem>();
                String str_response = response.body().string();

                try {
                    JSONTokener jsonTokener = new JSONTokener(str_response);
                    JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                    JSONArray jsonArray = jsonObject.getJSONArray("datalist");
                    int size=jsonArray.length()<2?jsonArray.length():2;
                    for(int i = 0 ;i<size;i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        QuestionItem questionItem = new QuestionItem();
                        questionItem.setQuestionID(item.getString("questionId"));
                        questionItem.setQuestionTitle(item.getString("questionTitle"));
                        questionItem.setQuestionContent(item.getString("questionContent"));
                        questionItem.setUsername(item.getString("userName"));
                        questionItem.setCreateTime_sys(item.getString("createTime_sys"));
                        questionItem.setPhotoUrl(item.getString("photoUrl"));
                        list2.add(questionItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tiWenAdapter=new TiWenAdapter(getContext(),list2);
                        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getContext(),LinearLayout.VERTICAL,false);
                        indexTiwenRecycleView.setLayoutManager(linearLayoutManager1);
                        tiWenAdapter.setItemOnClickListener(new TiWenAdapter.ItemOnClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Intent intent=new Intent(getContext(),QuestionDetailedActivity.class);
                                intent.putExtra("questionID",list2.get(position).getQuestionID());
                                startActivity(intent);
                            }
                        });
                        indexTiwenRecycleView.setAdapter(tiWenAdapter);


                    }
                });

            }



        });
        Call call2=Net.getInstance().shipinList(0,1);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String s=response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("datalist");
                    Gson gson=new Gson();
                    list3=gson.fromJson(jsonArray.toString(),new TypeToken<List<ShipinItem>>(){}.getType());
                    List<ShipinItem> listtem=new ArrayList<ShipinItem>();
                    for(int i=0;i<3;i++){
                        listtem.add(list3.get(i));
                    }
                    list3=listtem;
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shipinAdapter=new ShipinAdapter(getContext(),list3);
                        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(getContext(),LinearLayout.VERTICAL,false);
                        indexShipinRecyclerView.setLayoutManager(linearLayoutManager2);
                        indexShipinRecyclerView.setAdapter(shipinAdapter);
                        shipinAdapter.setItemOnClickListener(new ShipinAdapter.ItemOnClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Intent intent=new Intent(getContext(), VideoDetail.class);
                                intent.putExtra("id",list3.get(position).getVideoId());
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }
}
