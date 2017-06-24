package com.example.a.app10.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a.app10.Adapter.ViewPagerAdapter;
import com.example.a.app10.R;

public class Main1Activity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mainTab;
    private int[] tab_titles=new int[]{R.string.title1,R.string.title2,R.string.title3};
    private int[] tab_images=new int[]{R.drawable.index,R.drawable.expert,R.drawable.info};
    private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initView();
    }
    private void initView(){
        mainTab=(TabLayout)findViewById(R.id.main_tab);
        viewPager=(ViewPager)findViewById(R.id.main_viewpager);
        setTabs();
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainTab));
        viewPager.setOffscreenPageLimit(1);
        mainTab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }
    private void setTabs(){
        for(int i=0;i<tab_titles.length;i++){
            TabLayout.Tab tab=mainTab.newTab();
            View view=getLayoutInflater().inflate(R.layout.tab_item,null);
            tab.setCustomView(view);
            TextView textView=(TextView)view.findViewById(R.id.tab_text);
            ImageView imageView=(ImageView)view.findViewById(R.id.tab_img);
            textView.setText(getResources().getText(tab_titles[i]).toString());
            imageView.setImageResource(tab_images[i]);
            mainTab.addTab(tab);
        }
    }
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);

    }

}
