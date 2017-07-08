package com.example.a.app10.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.tool.Net;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Comment extends AppCompatActivity {
    private ImageView imageView;
    private ImageView back;
    private Button commit;
    private EditText editText;
    private String orderid="";
    private int rat;
    private int[] pic={R.drawable.star1,R.drawable.star2,R.drawable.star3,R.drawable.star4,R.drawable.star5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if(getIntent().hasExtra("orderId"))
            orderid=getIntent().getStringExtra("orderId");
        init();
        initEvent();
    }
    private void init(){
        imageView=(ImageView)findViewById(R.id.rating);
        editText=(EditText)findViewById(R.id.input);
        back=(ImageButton) findViewById(R.id.back_imag);
        commit=(Button)findViewById(R.id.comment);

    }
    private void initEvent(){
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float location=event.getX()*5;
                float width=imageView.getWidth();
                if(location<width) {
                    imageView.setBackgroundResource(pic[0]);
                    rat = 1;
                }
                else if(location<2*width){
                    imageView.setBackgroundResource(pic[1]);
                    rat=2;
                }
                else if(location<3*width){
                    imageView.setBackgroundResource(pic[2]);
                    rat=3;
                }
                else if(location<4*width) {
                    imageView.setBackgroundResource(pic[3]);
                    rat = 4;
                }
                else{
                    imageView.setBackgroundResource(pic[4]);
                    rat=5;
                }
                return false;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Call call=Net.getInstance().comment(orderid,String.valueOf(rat),editText.getText().toString());
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String res=response.body().string();
                        try{
                        JSONObject jsonObject=new JSONObject(res);
                        final String mes=jsonObject.getString("megs");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Comment.this, ""+mes, Toast.LENGTH_SHORT).show();
                                    editText.setText("");
                                    startActivity(new Intent(Comment.this,MyReservationActivity.class));
                                }
                            });
                        }
                        catch (JSONException  e){
                        e.printStackTrace();}

                    }
                });
            }
        });

    }
}
