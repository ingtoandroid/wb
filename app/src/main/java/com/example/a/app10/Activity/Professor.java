package com.example.a.app10.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.example.a.app10.Adapter.ExpertAdapter;
import com.example.a.app10.R;

public class Professor extends AppCompatActivity {
    private RecyclerView professor;
    private ExpertAdapter expertAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initViews();
        initEvents();
    }
    private void initViews(){
        professor=(RecyclerView)findViewById(R.id.professor_recyclerview);
    }
    private void initEvents(){
        expertAdapter=new ExpertAdapter(Professor.this);
        professor.setLayoutManager(new GridLayoutManager(Professor.this,4));
        professor.setAdapter(expertAdapter);
        expertAdapter.setItemOnClickListener(new ExpertAdapter.ItemOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(Professor.this,ProfessorActivity.class);
                startActivity(intent);
            }
        });

    }
}
