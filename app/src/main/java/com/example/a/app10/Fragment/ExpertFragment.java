package com.example.a.app10.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a.app10.Adapter.ExpertAdapter;
import com.example.a.app10.R;

public class ExpertFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView expertRecyclerview;
    private ExpertAdapter expertAdapter;


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
        return view;
    }
    private void init(){
        expertAdapter=new ExpertAdapter(getContext());
        expertRecyclerview.setLayoutManager(new GridLayoutManager(getContext(),4));
        expertRecyclerview.setAdapter(expertAdapter);
    }

}
