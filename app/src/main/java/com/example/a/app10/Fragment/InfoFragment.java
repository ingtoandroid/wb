package com.example.a.app10.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a.app10.Activity.ClassDetailActivity;
import com.example.a.app10.Activity.LoginActivity;
import com.example.a.app10.Activity.MessageReminderActivity;
import com.example.a.app10.Activity.MyClassActivity;
import com.example.a.app10.Activity.SettingActivity;
import com.example.a.app10.Activity.ModifyDataActivity;
import com.example.a.app10.Activity.QuestionActivity;
import com.example.a.app10.Activity.MyMessageActivity;
import com.example.a.app10.Activity.MyPointsActivity;
import com.example.a.app10.Activity.MyReservationActivity;
import com.example.a.app10.R;
import com.example.a.app10.tool.GlideCircleTransform;
import com.example.a.app10.tool.Net;

import q.rorbin.badgeview.QBadgeView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RelativeLayout reservation;
    private RelativeLayout course_info;
    private RelativeLayout my_integral;
    private RelativeLayout my_message;
    private RelativeLayout my_body_data;
    private RelativeLayout my_question;
    private Button cog;
    private Button messageMinder;
    private TextView username;
    private ImageView headImage;
    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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
        // Inflate the layout for this fragment
//        if(Net.getPersonID().equals("")){
//            Intent intent = new Intent(getContext(), LoginActivity.class);
//            startActivity(intent);
//        }


        View view=inflater.inflate(R.layout.fragment_info,container,false);
        reservation = (RelativeLayout)view.findViewById(R.id.reservation);
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Net.getPersonID().equals("")){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), MyReservationActivity.class);
                    startActivity(intent);
                }
            }
        });

        course_info = (RelativeLayout)view.findViewById(R.id.course_infomation);
        course_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Net.getPersonID().equals("")){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), MyClassActivity.class);
                    startActivity(intent);
                }
            }
        });

        my_integral = (RelativeLayout) view.findViewById(R.id.my_integral);
        my_integral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Net.getPersonID().equals("")){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), MyPointsActivity.class);
                    startActivity(intent);
                }
            }
        });

        my_message = (RelativeLayout) view.findViewById(R.id.my_message);
        my_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Net.getPersonID().equals("")){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), MyMessageActivity.class);
                    startActivity(intent);
                }
            }
        });
        my_body_data = (RelativeLayout) view.findViewById(R.id.my_body_data);
        my_body_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        my_question = (RelativeLayout) view.findViewById(R.id.my_question);
        my_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Net.getPersonID().equals("")){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), QuestionActivity.class);
                    startActivity(intent);
                }
            }

        });

        cog = (Button) view.findViewById(R.id.cog);
        cog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        messageMinder = (Button) view.findViewById(R.id.speech_bubbles);
        new QBadgeView(getContext()).bindTarget(messageMinder).setBadgeNumber(Net.getMegsSize());
        messageMinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Net.getPersonID().equals("")){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), MessageReminderActivity.class);
                    startActivity(intent);
                }
            }
        });

        username = (TextView)view.findViewById(R.id.username);
        username.setText(Net.getPersonName());

        headImage = (ImageView)view.findViewById(R.id.head_image);
        Glide.with(getContext()).load(Net.getPhotoUrl()).into(headImage);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
