package com.yyx.GresturePasswordyyx;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by java on 2017/7/24.
 */

public class mainview extends View {
    private Context mContext;
    private int mWidth;
    private int mHeight;
    //圆点
    private int mCircle_r;//半径
    private Paint mPaint;//圆外框
    private Paint mPaint1;//圆背景
    private Paint mPaint2;//圆内圆
    private Point[] mPoints = new Point[9];
    //密码序列
    private List<Integer> mPassword;


    public mainview(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public mainview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public mainview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int speMode = MeasureSpec.getMode(heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        if(speMode == MeasureSpec.AT_MOST){
        }
        if(speMode == MeasureSpec.EXACTLY){
        }
        if(speMode == MeasureSpec.UNSPECIFIED){
        }
        setMeasuredDimension(mWidth, mHeight);
        if (mWidth < mHeight) {
            mCircle_r = mWidth / 10;
        } else
            mCircle_r = mHeight / 10;
        setCircle_c();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //初始化
        canvas.drawColor(Color.rgb(33,143,230));
        for (int i = 0 ; i < 9 ; ++i){
            drawCircle(canvas,i);
        }
        line(canvas);
    }



    //初始化
    private void init(){
        setPaint();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onMyTouchEvent(motionEvent);
                return true;
            }
        });
    }

    private void drawCircle(Canvas canvas, int i){
        canvas.drawCircle(mPoints[i].x,mPoints[i].y,mCircle_r,mPaint);
        canvas.drawCircle(mPoints[i].x,mPoints[i].y,mCircle_r,mPaint1);
        canvas.drawCircle(mPoints[i].x,mPoints[i].y,mCircle_r/2,mPaint2);
    }

    private void setPaint(){
        if (mPaint == null)
            mPaint = new Paint();
        mPaint.setAntiAlias(true);                       //设置画笔为无锯齿
        mPaint.setColor(Color.WHITE);                    //设置画笔颜色
        mPaint.setStrokeWidth(6f);              //线宽
        mPaint.setStyle(Paint.Style.STROKE);                   //空心效果

        if(mPaint1 == null)
            mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setColor(Color.rgb(121,188,240));
        mPaint1.setStyle(Paint.Style.FILL);

        if(mPaint2 == null)
            mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(Color.WHITE);
        mPaint2.setStyle(Paint.Style.FILL);
    }

    private void setCircle_c(){
        setC(0,2,2);
        setC(1,5,2);
        setC(2,8,2);
        setC(3,2,5);
        setC(4,5,5);
        setC(5,8,5);
        setC(6,2,8);
        setC(7,5,8);
        setC(8,8,8);
    }

    private void setC(int i , int w, int h){
        try {
            if (mPoints[i] == null)
                mPoints[i] = new Point(mCircle_r * w, mCircle_r * h);
            else {
                mPoints[i].x = mCircle_r * w;
                mPoints[i].y = mCircle_r * h;
            }
        }catch (Exception e){
            Log.e("201707241057",e.toString());
        }
    }

    //事件处理
    private boolean onMyTouchEvent(MotionEvent motionEvent){
        boolean result = false;
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int password = printfPassword(x,y);
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                clearPassword();
                if (password >=0 && password <9) {
                    if (mPassword == null)
                        mPassword = new ArrayList<>();
                    if (!ishava(password)) {
                        mPassword.add(password);
                    }
                }
                result = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (password >=0 && password <9) {
                    if (mPassword == null)
                        mPassword = new ArrayList<>();
                    if (!ishava(password)) {
                        mPassword.add(password);
                        result = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                String pass = "";
                for (Integer it : mPassword){
                    pass = pass+it;
                }
                Log.e("show","password= "+ pass);
                result = true;
                break;
        }
        if (result)
            postInvalidate();
        return result;
    }
    private void clearPassword(){
        mPassword=null;
    }

    //碰撞有效返回密码 -1 = 无碰撞
    private int printfPassword(int x, int y){
        for (int i =0; i<9;++i){
            boolean collision = collisionDetection(mPoints[i].x,mPoints[i].y,x,y,mCircle_r/2);
            if (collision){
                return i;
            }
        }
        return -1;
    }

    //检测重复
    private boolean ishava(int password){
        boolean result = false;
        if (mPassword != null){
            for (Integer it : mPassword){
                if (it == password){
                    result = true;
                }
            }
        }
        return result;
    }

    //碰撞检测
    private boolean collisionDetection(int x1 ,int y1, int x2 ,int y2, int r){
        if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) <= r) {
            // 如果点和圆心距离小于或等于半径则认为发生碰撞
            return true;
        }else{
            return false;
        }
    }

    //连线绘制
    private void line(Canvas canvas){
        if (mPassword != null && mPassword.size()>1){
            int leng = mPassword.size();
            float[] lines = new float[leng*4-4];
            for (int i = 0; i < leng-1 ; ++i){
                int idx1 = mPassword.get(i);
                int idx2 = mPassword.get(i+1);
                lines[i*4] = mPoints[idx1].x;
                lines[i*4+1] = mPoints[idx1].y;
                lines[i*4+2] = mPoints[idx2].x;
                lines[i*4+3] = mPoints[idx2].y;
            }
            canvas.drawLines(lines,mPaint2);
        }
    }
}
