package com.example.a.app10.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a.app10.R;
import com.example.a.app10.bean.ProfessorItem;
import com.example.a.app10.tool.MyInternet;
import com.example.a.app10.tool.Net;
import com.example.a.app10.view.MyOrderList;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpertOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private OkHttpClient client;
    private ProfessorItem professor;
    private View head;
    private MyOrderList form;
    private CardView cardDetail;
    private Button btnChooseDate,btnOrder;
    private DatePickerDialog dialog;
    private Spinner spinner;
    private String userid;
    private CircleImageView image;
    private ImageView ivGrade;
    private EditText etTitle,etContent;
    private String[] rawDates=new String[7];
    private List<String> spinnerTexts,startTime,endTime,timeIds;
    private boolean isChosen=false;//是否已经有被选中的表格
    private int choseIndex=0;//被选中的标号
    private int choseTimeIndex=0;
    private String str="";
    private int[] states=new int[21];
    private String[] serviceIds=new String[21];
    private String monday,tues,wens,thur,fri,sat,sun;
    private TextView tvName,tvContent,tvMonday,tvTuesday,tvWens,tvThur,tvFri,tvSat,tvSun;
    private int[] grades={R.drawable.star0,R.drawable.star1,R.drawable.star2,R.drawable.star3,R.drawable.star4,R.drawable.star5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_order);

        Window window=getWindow();//设置透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.back);//设置标题栏

        professor= (ProfessorItem) getIntent().getExtras().get("professor");
        userid= Net.getPersonID();
        init();
        new LoadTask().execute(null,null,null);
    }

    private void init() {
        client=new OkHttpClient();
        head=findViewById(R.id.head);
        image= (CircleImageView) findViewById(R.id.image);
        Glide.with(this).load(professor.getImgUrl()).into(image);
        ivGrade= (ImageView) findViewById(R.id.ivGrade);
        ivGrade.setImageResource(grades[professor.getGrade()]);
        tvName= (TextView) findViewById(R.id.tvName);
        tvName.setTextColor(Color.WHITE);
        tvName.setText(professor.getName());
        tvContent= (TextView) findViewById(R.id.tvContent);
        tvContent.setTextColor(Color.WHITE);
        tvContent.setText(professor.getContent());
        tvMonday= (TextView) findViewById(R.id.tvMonday);
        tvTuesday= (TextView) findViewById(R.id.tvTuesday);
        tvWens= (TextView) findViewById(R.id.tvWendsday);
        tvThur= (TextView) findViewById(R.id.tvThursday);
        tvFri= (TextView) findViewById(R.id.tvFriday);
        tvSat= (TextView) findViewById(R.id.tvSatday);
        tvSun= (TextView) findViewById(R.id.tvSunday);
        spinner= (Spinner) findViewById(R.id.spinner);

        head.setBackgroundColor(Color.TRANSPARENT);
        cardDetail= (CardView) findViewById(R.id.cardDetail);
        btnChooseDate= (Button) findViewById(R.id.btnChooseDate);
        btnChooseDate.setOnClickListener(this);
        btnOrder= (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);
        etTitle= (EditText) findViewById(R.id.etTitle);
        etContent= (EditText) findViewById(R.id.etContent);
        form= (MyOrderList) findViewById(R.id.form);
        form.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()!=MotionEvent.ACTION_UP){//只要拿起事件
                    return true;
                }
                float x=motionEvent.getX();
                float y=motionEvent.getY();
                float singleWidth=form.getWidth()/7;
                float singleHeight=form.getHeight()/3;
                int index=((int)(y/singleHeight))*7+(int) (x/singleWidth);
                if (index>=20){
                   return true;
                }
                if (isChosen){
                    form.setState(choseIndex,MyOrderList.FREE);
                }
                if (form.getState(index)==MyOrderList.FREE){
                    form.setState(index,MyOrderList.CHOSEN);
                    form.invalidate();//强制刷新
                    showBottom();
                    isChosen=true;
                    choseIndex=index;
                }
                return true;
            }
        });

        initDialog();
    }

    private void showBottom() {
        String url=MyInternet.MAIN_URL+
                "expert/get_expert_order_service_time?expertId="+professor.getExpertId()
                +"&serviceId="+serviceIds[choseIndex]
                +"&serviceTimeType="+((choseIndex/7)+1);
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                spinnerTexts=new ArrayList<>();
                startTime=new ArrayList<>();
                timeIds=new ArrayList<String>();
                endTime=new ArrayList<>();//几个数组的初始化
                spinnerTexts.add("选择时间");
                startTime.add("");
                timeIds.add("");
                endTime.add("");//默认项
                try {
                    JSONObject all=new JSONObject(s);
                    JSONArray array=all.getJSONArray("dataList");
                    for (int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        if (object.getBoolean("abled")){
                            spinnerTexts.add(object.getString("time"));
                            startTime.add(object.getString("serviceStartTime"));
                            endTime.add(object.getString("serviceEndTime"));
                            timeIds.add(object.getString("timeId"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mainThread() {
                ArrayAdapter<String> adapter=new ArrayAdapter<>(ExpertOrderActivity.this,R.layout.spinner_item,spinnerTexts);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        choseTimeIndex=i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                cardDetail.setVisibility(View.VISIBLE);
            }
        },this);
    }


    private void initDialog() {
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                changeDate(i,i1,i2);
            }
        };
        dialog=new DatePickerDialog(this,listener,year,month,day);
        dialog.setTitle("选择日期");

    }

    private void changeDate(int i, int i1, int i2) {
        showProgress("加载中");
        String date="&dateStr="+i+"/"+i1+"/"+i2;
        getData(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnChooseDate:
                dialog.show();
                break;
            case R.id.btnOrder:
                if(Net.getPersonID().equals("")){
                    Intent intent = new Intent(ExpertOrderActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                order();
                break;
        }
    }

    private void order() {//预约
        if (choseTimeIndex==0){
            Toast.makeText(this,"请选择时间",Toast.LENGTH_SHORT).show();
            return;
        }
        String url=MyInternet.MAIN_URL+"expert/expert_order_service_time_save?timeId="+timeIds.get(choseTimeIndex)
                +"&expertId="+professor.getExpertId()
                +"&infoId="+userid
                +"&ordertitle="+etTitle.getText().toString()
                +"&orderContent="+etContent.getText().toString()
                +"&servieId="+serviceIds[choseIndex]
                +"&serviceStartTime="+startTime.get(choseTimeIndex)
                +"&serviceEndTime="+endTime.get(choseTimeIndex)
                +"&orderDate="+rawDates[choseIndex/7];
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                str=s;
            }

            @Override
            public void mainThread() {
                btnOrder.setClickable(false);
                btnOrder.setBackgroundResource(R.drawable.not_click_button);
                Toast.makeText(ExpertOrderActivity.this,str,Toast.LENGTH_SHORT).show();
            }
        },this);
    }

    private class LoadTask extends AsyncTask<URL,Integer,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress("加载中");
        }

        @Override
        protected Void doInBackground(URL... urls) {
            getData("");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void getData(String date) {
        String url= MyInternet.MAIN_URL+"expert/get_expert_order_service?expertId="+professor.getExpertId()+date;
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject all=new JSONObject(s);
                    monday="一"+"\n"+getDate(all.getString("monday"));
                    rawDates[0]=all.getString("monday");
                    tues="二"+"\n"+getDate(all.getString("seconday"));
                    rawDates[1]=all.getString("seconday");
                    wens="三"+"\n"+getDate(all.getString("wednesday"));
                    rawDates[2]=all.getString("wednesday");
                    thur="四"+"\n"+getDate(all.getString("thursday"));
                    rawDates[3]=all.getString("thursday");
                    fri="五"+"\n"+getDate(all.getString("friday"));
                    rawDates[4]=all.getString("friday");
                    sat="六"+"\n"+getDate(all.getString("saturday"));
                    rawDates[5]=all.getString("saturday");
                    sun="日"+"\n"+getDate(all.getString("sunday"));
                    rawDates[6]=all.getString("sunday");
                    JSONArray array=all.getJSONArray("dataList");
                    for (int i=0;i<array.length();i++){//设置表格状态
                        JSONObject object=array.getJSONObject(i);
                        states[i*7]=getState(object.getBoolean("monAbled"));
                        serviceIds[i*7]=object.getString("monday");
                        states[i*7+1]=getState(object.getBoolean("secAbled"));
                        serviceIds[i*7+1]=object.getString("seconday");
                        states[i*7+2]=getState(object.getBoolean("wedAbled"));
                        serviceIds[i*7+2]=object.getString("wednesday");
                        states[i*7+3]=getState(object.getBoolean("thuAbled"));
                        serviceIds[i*7+3]=object.getString("thursday");
                        states[i*7+4]=getState(object.getBoolean("friAbled"));
                        serviceIds[i*7+4]=object.getString("friday");
                        states[i*7+5]=getState(object.getBoolean("satAbled"));
                        serviceIds[i*7+5]=object.getString("saturday");
                        states[i*7+6]=getState(object.getBoolean("sunAbled"));
                        serviceIds[i*7+6]=object.getString("sunday");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mainThread() {
                show();
            }
        },this);
    }

    private void show() {
        hideProgress();
        tvMonday.setText(monday);
        tvTuesday.setText(tues);
        tvWens.setText(wens);
        tvThur.setText(thur);
        tvFri.setText(fri);
        tvSat.setText(sat);
        tvSun.setText(sun);
        form.setStates(states);
        form.invalidate();
    }

    //显示进度对话框
    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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

    public String getDate(String s){//字符串转“01.01”格式
        //DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        return s.substring(5,6)+"."+s.substring(8,9);
    }

    public int getState(boolean b){//获取状态
        if (b){
            return MyOrderList.FREE;
        } else {
            return MyOrderList.BUSY;
        }
    }
}
