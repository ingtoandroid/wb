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
import com.example.a.app10.Activity.QuickQuestionActivity;
import com.example.a.app10.Activity.ScienceActivity;
import com.example.a.app10.Activity.ShipinActivity;
import com.example.a.app10.Activity.VideoDetail;
import com.example.a.app10.Adapter.IndexExpertAdapter;
import com.example.a.app10.Adapter.ShipinAdapter;
import com.example.a.app10.Adapter.TiWenAdapter;
import com.example.a.app10.R;

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
    private ImageButton kepu;
    private ImageButton shipin;
    private ImageButton ketang;
    private ImageButton zixun;


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
        kepu=(ImageButton)view.findViewById(R.id.index_kepu);
        shipin=(ImageButton)view.findViewById(R.id.index_shipin);
        ketang=(ImageButton)view.findViewById(R.id.index_ketang);
        zixun=(ImageButton)view.findViewById(R.id.index_zixun);
        init();
        return view;
    }
    private void init(){
        indexExpertAdapter =new IndexExpertAdapter(getContext());
        tiWenAdapter=new TiWenAdapter(getContext());
        shipinAdapter=new ShipinAdapter(getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getContext(),LinearLayout.VERTICAL,false);
        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(getContext(),LinearLayout.VERTICAL,false);
        indexExpertRecycleView.setLayoutManager(linearLayoutManager);
        indexExpertRecycleView.setAdapter(indexExpertAdapter);
        indexTiwenRecycleView.setLayoutManager(linearLayoutManager1);
        indexTiwenRecycleView.setAdapter(tiWenAdapter);
        indexShipinRecyclerView.setLayoutManager(linearLayoutManager2);
        indexShipinRecyclerView.setAdapter(shipinAdapter);
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
        indexExpertAdapter.setItemOnClickListener(new IndexExpertAdapter.ItemOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(getContext(), ProfessorDetailActivity.class);
                startActivity(intent);
            }
        });
        shipinAdapter.setItemOnClickListener(new ShipinAdapter.ItemOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(getContext(), VideoDetail.class);
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

}
