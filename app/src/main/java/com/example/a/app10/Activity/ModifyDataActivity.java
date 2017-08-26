package com.example.a.app10.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a.app10.R;
import com.example.a.app10.tool.Net;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModifyDataActivity extends AppCompatActivity {

    private String nickName = "";
    private String location = "";
    private String telephone = "";
    private String sex = "";
    private String signature = "";
    private String headImageUrl = "";

    private ImageButton back;
    private LinearLayout line_back;
    private ImageView im_headImage;
    private EditText ed_username;
    private TextView tx_phoneNumber;
    private EditText ed_sex;
    private EditText ed_location;
    private EditText ed_signature;
    private TextView tx_saveInfo;
    private RelativeLayout relativeLayout;
    private int REQUEST_CODE_LOCAL=0;
    private RadioGroup radioGroup;
    private static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_modify_data);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        init();
        initEvent();

        getPersonalInfo();
        getPermission();
    }

    private void init(){
        line_back = (LinearLayout)findViewById(R.id.line_back);
        back = (ImageButton) findViewById(R.id.back);
        im_headImage = (ImageView)findViewById(R.id.head_image);
        ed_username = (EditText)findViewById(R.id.username);
        tx_phoneNumber = (TextView)findViewById(R.id.phone_number);
//        ed_sex = (EditText)findViewById(R.id.sex);
//        ed_location = (EditText)findViewById(R.id.location);
        ed_signature = (EditText)findViewById(R.id.signature);
        tx_saveInfo = (TextView)findViewById(R.id.save_info);
        relativeLayout=(RelativeLayout)findViewById(R.id.head_image_change_item);
        radioGroup = (RadioGroup)findViewById(R.id.rg);
    }

    private void initEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        line_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tx_saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_username = ed_username.getText().toString().trim();
                String str_signature = ed_signature.getText().toString().trim();
                RadioButton rb = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                String str_sex = rb.getText().toString().trim();
                if(str_sex.equals("男")||str_sex.equals("女")) {
                    modifyInfo(str_username, str_signature, str_sex);
                }else{
                    Toast.makeText(ModifyDataActivity.this,"性别不符",Toast.LENGTH_SHORT).show();
                }
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicFromLocal();
            }
        });
    }
    //获取用户信息
    private void getPersonalInfo(){
        Call call = Net.getInstance().getPersonalInfo(Net.getPersonID());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str_response = response.body().string();
                JSONTokener jsonTokener = new JSONTokener(str_response);
                try {
                    JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                    nickName = jsonObject.getString("nickName");
                    headImageUrl = jsonObject.getString("modelName");//图片路径，绝对
                    telephone = jsonObject.getString("telephone");
                    sex = jsonObject.getString("sex");
                    signature = jsonObject.getString("signature");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ed_username.setText(nickName);
                        tx_phoneNumber.setText(telephone);
                        if(sex.equals("男")){
                            radioGroup.check(R.id.rb1);
                        }else {
                            radioGroup.check(R.id.rb2);
                        }
//                        ed_sex.setText(sex);
                        Glide.with(ModifyDataActivity.this).load(headImageUrl).into(im_headImage);
                        //头像设置
//                        ed_location.setText();
                        ed_signature.setText(signature);
                    }
                });
            }
        });
    }

    //修改会员信息
    public void modifyInfo(String username,String signature,String sex){
        Call call = Net.getInstance().modifyInfo(Net.getPersonID(),username,signature,sex);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str_response = response.body().string();
                JSONTokener jsonTokener = new JSONTokener(str_response);
                try {
                    JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                    String megs = jsonObject.getString("megs");
                    //修改成功
                    if(megs.equals("修改成功")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifyDataActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifyDataActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    protected void selectPicFromLocal() {
        if(ContextCompat.checkSelfPermission(ModifyDataActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
             Toast.makeText(ModifyDataActivity.this,"未授予读写权限",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_LOCAL) {
            final Uri uri = ( data != null ? data.getData() : null );
            if (uri != null) {
                Call call = Net.getInstance().setHeadImage(new File(getPath(uri)));
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifyDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String s = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int megs = jsonObject.getInt("megs");
                            if (megs == 0) {
                                Net.setPhotoUrl(jsonObject.getString("modelName"));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(ModifyDataActivity.this).load(uri).into(im_headImage);
                                        Toast.makeText(ModifyDataActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (megs == -1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ModifyDataActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException E) {
                            E.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    protected String  getPath(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, com.hyphenate.easeui.R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return null;
            }
            return picturePath;
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, com.hyphenate.easeui.R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return null;

            }
            return  file.getAbsolutePath();
        }

    }

    private void getPermission() {
        List<String> list = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(ModifyDataActivity.this, PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED) {
            list.add(PERMISSIONS[0]);
        }

        String[] strings = new String[list.size()];
        list.toArray(strings);
        Log.e("list",""+strings.length);
        if(strings.length != 0) {
            ActivityCompat.requestPermissions(ModifyDataActivity.this,
                    strings, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }
}
