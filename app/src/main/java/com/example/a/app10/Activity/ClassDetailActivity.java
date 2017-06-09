package com.example.a.app10.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.a.app10.R;


public class ClassDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvContent1,tvContent2,tvContent3,tvOpen1,tvOpen2,tvOpen3;
    private final int CURRENT_LINES= 3;
    private boolean[] isOpen={false,false,false};//标记三个文本是否展开
    private Button btnBack,btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        init();
    }

    private void init() {
        tvContent1= (TextView) findViewById(R.id.tvContent1);
        tvContent1.setHeight(tvContent1.getLineHeight()*CURRENT_LINES);
        tvContent2= (TextView) findViewById(R.id.tvContent2);
        tvContent2.setHeight(tvContent2.getLineHeight()*CURRENT_LINES);
        tvContent3= (TextView) findViewById(R.id.tvContent3);
        tvContent3.setHeight(tvContent3.getLineHeight()*CURRENT_LINES);
        tvOpen1= (TextView) findViewById(R.id.tvOpen1);
        tvOpen2= (TextView) findViewById(R.id.tvOpen2);
        tvOpen3= (TextView) findViewById(R.id.tvOpen3);
        btnBack= (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnJoin= (Button) findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(this);

        tvOpen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen[0]==false){
                    tvContent1.setHeight(tvContent1.getLineHeight()*tvContent1.getLineCount());
                    isOpen[0]=true;
                    tvOpen1.setText("点击收回");
                } else {
                    tvContent1.setHeight(tvContent1.getLineHeight()*CURRENT_LINES);
                    isOpen[0]=false;
                    tvOpen1.setText("展开全部");
                }}
        });
        tvOpen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen[1]==false){
                    tvContent2.setHeight(tvContent2.getLineHeight()*tvContent2.getLineCount());
                    isOpen[1]=true;
                    tvOpen2.setText("点击收回");
                } else {
                    tvContent2.setHeight(tvContent2.getLineHeight()*CURRENT_LINES);
                    isOpen[1]=false;
                    tvOpen2.setText("展开全部");
                }}
        });
        tvOpen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen[2]==false){
                    tvContent3.setHeight(tvContent3.getLineHeight()*tvContent3.getLineCount());
                    isOpen[2]=true;
                    tvOpen3.setText("点击收回");
                } else {
                    tvContent3.setHeight(tvContent3.getLineHeight()*CURRENT_LINES);
                    isOpen[2]=false;
                    tvOpen3.setText("展开全部");
                }}
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnJoin:
                break;
        }
    }
}
