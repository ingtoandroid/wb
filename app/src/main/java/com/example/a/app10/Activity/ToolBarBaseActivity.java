package com.example.a.app10.Activity;

import android.app.ProgressDialog;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.app10.R;

public abstract class ToolBarBaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MyOnClickListener leftlistener,rightlistener;
    private int rightId;
    private String rightTitle;
    private FrameLayout contentView,sidemenu;
    private TextView tv;
    private DrawerLayout drawer;
    private ProgressDialog mProgressDialog;

    public interface MyOnClickListener{
        void onClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar_base);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        tv= (TextView) findViewById(R.id.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//原默认标题不显示
        //将子活动的布局加载到内容布局中
        contentView= (FrameLayout) findViewById(R.id.contentView);
        LayoutInflater.from(ToolBarBaseActivity.this).inflate(getContentView(),contentView);
        //加载侧滑菜单
        sidemenu= (FrameLayout) findViewById(R.id.sidemenu);
        LayoutInflater.from(ToolBarBaseActivity.this).inflate(getSideMenu(),sidemenu);
        drawer= (DrawerLayout) findViewById(R.id.drawer);

        a();

        //给出初始化方法，传递缓存数据
        init(savedInstanceState);
    }

    protected abstract int getSideMenu();

    protected abstract void init(Bundle savedInstanceState);

    protected abstract int getContentView();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            leftlistener.onClick();
        } else {
            rightlistener.onClick();
        }
        return true;//此事件已处理
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (rightId!= 0|| !TextUtils.isEmpty(rightTitle)){
            getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (rightId!=0){
            menu.findItem(R.id.item1).setIcon(rightId);
        }
        if (!TextUtils.isEmpty(rightTitle)){
            menu.findItem(R.id.item1).setTitle(rightTitle);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    protected  void setLeftButton(int buttonId, MyOnClickListener listener){
        this.leftlistener=listener;
        toolbar.setNavigationIcon(buttonId);
    }

    protected void setRightButton(int i,String s,MyOnClickListener rightlistener){
        this.rightId=i;
        this.rightTitle=s;
        this.rightlistener=rightlistener;
    }

    protected void setMyTitle(String i){
        tv.setText(i);
    }

    protected void openDrawer(){
        drawer.openDrawer(GravityCompat.END);
    }

    private void a() {
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

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

    protected  void hideDrawer(){
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);		//关闭手势滑动
    }

    protected void closeDrawer(){
        drawer.closeDrawer(GravityCompat.END);
    }
}
