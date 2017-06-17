package com.example.a.app10.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.a.app10.R;


/**
 * Created by lenovo on 2017/6/15.
 */

public class MyOrderList extends View {

    private int width,height;
    private Paint paint;
    private int[] states;
    public final static int BUSY =1;
    public final static int  FREE =2;
    public final static int CHOSEN =3;
    private final int RADIOUS=35;

    public MyOrderList(Context context) {
        this(context,null);
    }

    public MyOrderList(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyOrderList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint=new Paint();
        paint.setAntiAlias(true);
        states=new int[21];
        for (int i=0;i<21;i++){
            states[i]= BUSY;
        }
        states[10]= FREE;
        states[6]= FREE;
        states[14]= CHOSEN;
        states[2]= CHOSEN;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width=getWidth();
        height=getHeight();

        paint.setColor(Color.BLACK);
        for (int i=0;i<=2;i++){
            canvas.drawLine(0,i*(height/3),width,i*(height/3),paint);//画横线边框
        }
        canvas.drawLine(0,height-1,width,height-1,paint);//画底线
        for (int i=1;i<=7;i++){
            canvas.drawLine(i*(width/7),0,i*(width/7),height,paint);//画竖线边框
        }
        paint.setColor(getResources().getColor(R.color.main));
        for (int i=0;i<21;i++){
            int row=i/7+1;
            int column=i%7+1;
            int x=(width/7)*(column-1)+width/14;
            int y=(height/3)*(row-1)+height/6;
            switch (states[i]){
                case BUSY:
                    break;
                case FREE:
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(x,y,RADIOUS,paint);
                    break;
                case CHOSEN:
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(x,y,RADIOUS,paint);
                    break;
            }
        }
    }

    public int[] getStates() {
        return states;
    }

    public void setStates(int[] states) {
        this.states = states;
    }

    public void setState(int pos,int value){
        states[pos]=value;
    }
    public int getState(int pos){
        return states[pos];
    }
}
