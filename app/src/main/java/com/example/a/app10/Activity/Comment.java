package com.example.a.app10.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.a.app10.R;

public class Comment extends AppCompatActivity {
    private ImageView imageView;
    private EditText editText;
    private int[] pic={R.drawable.star1,R.drawable.star2,R.drawable.star3,R.drawable.star4,R.drawable.star5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        init();
        initEvent();
    }
    private void init(){
        imageView=(ImageView)findViewById(R.id.rating);
        editText=(EditText)findViewById(R.id.input);

    }
    private void initEvent(){
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float location=event.getX()*5;
                float width=imageView.getWidth();
                if(location<width)
                    imageView.setBackgroundResource(pic[0]);
                else if(location<2*width)
                    imageView.setBackgroundResource(pic[1]);
                else if(location<3*width)
                    imageView.setBackgroundResource(pic[2]);
                else if(location<4*width)
                    imageView.setBackgroundResource(pic[3]);
                else
                    imageView.setBackgroundResource(pic[4]);
                return false;
            }
        });

    }
}
