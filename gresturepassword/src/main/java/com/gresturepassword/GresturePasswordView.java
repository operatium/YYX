package com.gresturepassword;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class GresturePasswordView extends View {
    //圆点
    private float mCircle_r;//半径
    private Paint mPaint;//圆外框
    private Paint mPaint1;//圆背景
    private Paint mPaint2;//圆内圆
    private Paint mPaint3;//连线
    private Paint mPaint4;//方向
    private Paint mPaint5;//圆点选中
    private float mTriangleHeight;//三角形高度
    private float mTriangRout = 315;//三角形左右各自旋转角度
    private PointF[] mPoints = new PointF[9];
    private ArrayList<float[]> m3jPoints = new ArrayList<>();
    private List<Integer> mPassword;    //密码序列
    private List<Integer> mCheckPassword;//核对密码
    private int mStauts = Nor;
    private int mType = GetPassword;
    private ViewCallback mViewCallback;//回调

    //颜色
    private int mBackgroundColor = Color.rgb(33,143,230);
    private int mSeleckColor = Color.GRAY;
    private int mTringColor = Color.GREEN;
    private int mCircleWaiColor = Color.WHITE;
    private int mCircleNeiColor = Color.WHITE;
    private int mCircleFillColor = Color.rgb(121,188,240);
    private int mLineColor = Color.WHITE;
    private int mCircleErrorColor = Color.RED;

    public static final int Nor = 1;
    public static final int Error = 2;
    public static final int GetPassword = 3;
    public static final int CheckPassword = 4;

    public GresturePasswordView(Context context) {
        super(context);
        init();
    }

    public GresturePasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GresturePasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getSeleckColor() {
        return mSeleckColor;
    }

    public void setSeleckColor(int mSeleckColor) {
        this.mSeleckColor = mSeleckColor;
    }

    public int getTringColor() {
        return mTringColor;
    }

    public void setTringColor(int mTringColor) {
        this.mTringColor = mTringColor;
    }

    public static int getCheckPassword() {
        return CheckPassword;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
    }

    public int getCircleWaiColor() {
        return mCircleWaiColor;
    }

    public void setCircleWaiColor(int m_CircleWaiColor) {
        this.mCircleWaiColor = m_CircleWaiColor;
    }

    public int getCircleNeiColor() {
        return mCircleNeiColor;
    }

    public void setCircleNeiColor(int m_CircleNeiColor) {
        this.mCircleNeiColor = m_CircleNeiColor;
    }

    public int getCircleFillColor() {
        return mCircleFillColor;
    }

    public void setCircleFillColor(int m_CircleFillColor) {
        this.mCircleFillColor = m_CircleFillColor;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int m_LineColor) {
        this.mLineColor = m_LineColor;
    }

    public int getCircleErrorColor() {
        return mCircleErrorColor;
    }

    public void setCircleErrorColor(int m_CircleErrorColor) {
        this.mCircleErrorColor = m_CircleErrorColor;
    }

    public float getTriangRout() {
        return mTriangRout;
    }

    public void setTriangRout(float mTriangRout) {
        this.mTriangRout = mTriangRout;
    }

    public void setStauts(int mStauts) {
        this.mStauts = mStauts;
        invalidate();
    }

    public void setCallback(ViewCallback ViewCallback) {
        this.mViewCallback = ViewCallback;
    }

    public void setType(int Type, String password) throws Exception{
        if (Type == CheckPassword) {
            if (password != null && !password.isEmpty()) {
                if (mCheckPassword == null )
                    mCheckPassword = new ArrayList<>();
                else
                    mCheckPassword.clear();
                int length = password.length();
                for (int i=0 ; i <length ; ++i){
                    char c = password.charAt(i);
                    int pas = Integer.parseInt(c+"");
                    if (pas >=0 && pas <9){
                        mCheckPassword.add(pas);
                    }
                }
            }
            else{
                throw new Exception("核对密码模式下，必须要有密码");
            }
        }
        mType = Type;
    }

    //抖动
    public void shakeAnimation(){
        tada(this,mCircle_r/4).start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
//        int speMode = MeasureSpec.getMode(heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
//        if(speMode == MeasureSpec.AT_MOST){
//        }
//        if(speMode == MeasureSpec.EXACTLY){
//        }
//        if(speMode == MeasureSpec.UNSPECIFIED){
//        }
        setMeasuredDimension(mWidth, mHeight);
        if (mWidth < mHeight) {
            mCircle_r = mWidth / 10;
        } else
            mCircle_r = mHeight / 10;
        setCircle_c();
        mTriangleHeight = mCircle_r/8;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //初始化
        setPaint();
        canvas.drawColor(mBackgroundColor);//背景
        switch (mStauts){
            case Nor:
                drawCircle(canvas);
                line(canvas);
                drawSelectCircle(canvas);
                break;
            case Error:
                drawErrorCircle(canvas);
                break;
        }

    }
    //初始化
    private void init(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return onMyTouchEvent(motionEvent);
            }
        });
    }

    //画全部是红色的圆
    private void drawErrorCircle(Canvas canvas){
        int color = mPaint.getColor();
        int color2 = mPaint2.getColor();
        mPaint.setColor(mCircleErrorColor);
        mPaint2.setColor(mCircleErrorColor);
        drawCircle(canvas);
        mPaint.setColor(color);
        mPaint2.setColor(color2);
    }

    private void drawCircle(Canvas canvas){
        for (int i = 0 ; i < 9 ; ++i){
            canvas.drawCircle(mPoints[i].x,mPoints[i].y,mCircle_r,mPaint);
            canvas.drawCircle(mPoints[i].x,mPoints[i].y,mCircle_r,mPaint1);
            canvas.drawCircle(mPoints[i].x,mPoints[i].y,mCircle_r/2,mPaint2);
        }
    }

    private void drawSelectCircle(Canvas canvas){
        for (int i = 0 ; i < 9 ; ++i){
            if(mPassword != null && mPassword.contains(i))
                canvas.drawCircle(mPoints[i].x,mPoints[i].y,mCircle_r/4,mPaint5);
        }
    }

    private void setPaint(){
        if (mPaint == null)
            mPaint = new Paint();
        mPaint.setAntiAlias(true);                       //设置画笔为无锯齿
        mPaint.setColor(mCircleWaiColor);                    //设置画笔颜色
        mPaint.setStrokeWidth(6f);              //线宽
        mPaint.setStyle(Paint.Style.STROKE);                   //空心效果

        if(mPaint1 == null)
            mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setColor(mCircleFillColor);
        mPaint1.setStyle(Paint.Style.FILL);

        if(mPaint2 == null)
            mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(mCircleNeiColor);
        mPaint2.setStyle(Paint.Style.FILL);

        if (mPaint3 == null)
            mPaint3 = new Paint();
        mPaint3.setColor(mLineColor);
        mPaint3.setAntiAlias(true);
        mPaint3.setStrokeCap(Paint.Cap.ROUND);
        mPaint3.setStrokeWidth(mCircle_r/4);

        if (mPaint4 == null)
            mPaint4 = new Paint();
        mPaint4.setColor(mTringColor);
        mPaint4.setAntiAlias(true);
        mPaint4.setStyle(Paint.Style.FILL);

        if (mPaint5 == null)
            mPaint5 = new Paint();
        mPaint5.setColor(mSeleckColor);
        mPaint5.setAntiAlias(true);
        mPaint5.setStyle(Paint.Style.FILL);
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
                mPoints[i] = new PointF(mCircle_r * w, mCircle_r * h);
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
                if (mViewCallback != null)
                    mViewCallback.init(this);
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
                switch (mType){
                    case CheckPassword:
                        if (mViewCallback != null)
                            mViewCallback.checkPassword(this,IsTrueCheckPassword(),printfPassword());
                        break;
                    case GetPassword:
                        if (mViewCallback != null)
                            mViewCallback.getPassword(this,printfPassword());
                        break;
                }
                result = true;
                break;
        }
        if (result)
            invalidate();
        return result;
    }

    private void clearPassword(){
        mPassword=null;
    }

    //碰撞有效返回密码 -1 = 无碰撞
    private int printfPassword(int x, int y){
        mStauts = Nor;
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
    private boolean collisionDetection(float x1 ,float y1, float x2 ,float y2, float r){
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
                m3jPoints.add(crossoverPoint(mPoints[idx1].x,mPoints[idx1].y,mPoints[idx2].x,mPoints[idx2].y));
            }
            canvas.drawLines(lines,mPaint3);
            for (float[] it : m3jPoints){
                Path path = new Path();
                path.moveTo(it[0],it[1]);
                path.lineTo(it[2],it[3]);
                path.lineTo(it[4],it[5]);
                path.close();
                canvas.drawPath(path,mPaint4);
            }
            m3jPoints.clear();
        }
    }

    //圆和线的交叉点
    private float[] crossoverPoint(float x1, float y1, float x2, float y2){
        float[] result = new float[6];
        double x = 0,y = 0;//交点
        double x3j =0, y3j = 0;
        double k = (y2-y1)*1.0/(x2-x1);//y = (x-x1)*k+y1;直线公式 y= x*k-x1*k+y1
        double b = -x1*k+y1;
        double l = Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
        double heng;
        if (x1 == x2){
            k = 0;
            if (y1 < y2) {
                y = y1 + mCircle_r;
                y3j = y+mTriangleHeight;
            }
            else {
                y = y1 - mCircle_r;
                y3j = y-mTriangleHeight;
            }
            x = x1;
            x3j = x1;
        }else if (y1 ==y2){
            k = 0;
            if (x1 < x2) {
                x = x1 + mCircle_r;
                x3j = x+mTriangleHeight;
            }
            else {
                x = x1 - mCircle_r;
                x3j = x - mTriangleHeight;
            }
            y = y1;
            y3j = y1;
        }else if (x1 < x2){
            heng = mCircle_r/l*(x2-x1);
            x = x1+heng;
            y = getYFormLine(k,b,x);
            x3j = x1 + (mCircle_r+mTriangleHeight)/l*(x2-x1);
            y3j = getYFormLine(k,b,x3j);
        }else if (x1 >x2){
            heng = mCircle_r/l*(x1-x2);
            x = x1-heng;
            y = getYFormLine(k,b,x);
            x3j = x1 - (mCircle_r+mTriangleHeight)/l*(x1-x2);
            y3j = getYFormLine(k,b,x3j);
        }
        result[0] = (float) x3j;
        result[1] = (float) y3j;
        //三角形左边
        double k1 = (Math.tan(mTriangRout)+k)/(1-Math.tan(mTriangRout)*k);
        double b1 = y3j - k1*x3j;
        //三角形右边
        double k2 = (k-Math.tan(mTriangRout))/(Math.tan(mTriangRout)*k+1);
        double b2 = y3j - k2*x3j;
        //三角形下边
        double k3 = -1/k;
        if (k == 0)
            k3 = 0;
        double b3 = y - k3*x;


//        Paint p = new Paint();
//        p.setColor(Color.RED);
//        canvas.drawLine(0f,(float)b1,1000f,(float) (1000*k1+b1),p);
//        canvas.drawLine(0f,(float)b2,1000f,(float) (1000*k2+b2),p);
//        canvas.drawLine(0f,(float)b3,1000f,(float) (1000*k3+b3),p);
        if (y1 != y2) {//切线存在斜率的时候
//            canvas.drawLine(0f, (float) b, 1000f, (float) (1000 * k + b), p);
            PointF p1 = getPointFormTwoLinecrossover(k1,b1,k3,b3);
            result[2] = p1.x;
            result[3] = p1.y;
            PointF p2 = getPointFormTwoLinecrossover(k2,b2,k3,b3);
            result[4] = p2.x;
            result[5] = p2.y;
        }
        else {//切线不存在斜率的时候
//            canvas.drawLine((float) x, (float) b, (float) x, (float) b, p);
            PointF p1 = getPointFormTwoLinecrossover(k1,b1,x);
            result[2] = p1.x;
            result[3] = p1.y;
            PointF p2 = getPointFormTwoLinecrossover(k2,b2,x);
            result[4] = p2.x;
            result[5] = p2.y;
        }
        return result;
    }

    //2线交点 没有斜率
    private PointF getPointFormTwoLinecrossover(double k1,double b1, double x2){
        PointF p = new PointF();
        double y;
        //k1*x+b1-y = k2*x+b2-y
        y = getYFormLine(k1,b1,x2);
        p.x = (float) x2;
        p.y = (float) y;
        return p;
    }

    //2线交点
    private PointF getPointFormTwoLinecrossover(double k1,double b1, double k2, double b2){
        PointF p = new PointF();
        double x , y;
        //k1*x+b1-y = k2*x+b2-y
        if ( k1 != k2)
            x = (b2-b1 ) / (k1-k2);
        else
            x = 0;
        y = getYFormLine(k1,b1,x);
        p.x = (float) x;
        p.y = (float) y;
        return p;
    }

    private double getYFormLine(double k, double b, double x){
        return x*k+b;
    }

    private ObjectAnimator tada(View view, float shakeFactor) {
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(0.10f, -shakeFactor),
                Keyframe.ofFloat(0.26f, shakeFactor),
                Keyframe.ofFloat(0.42f, -shakeFactor),
                Keyframe.ofFloat(0.58f, shakeFactor),
                Keyframe.ofFloat(0.74f, -shakeFactor),
                Keyframe.ofFloat(0.90f, shakeFactor),
                Keyframe.ofFloat(1f, 0f)
        );
//        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
//                Keyframe.ofFloat(0f, 1f),
//                Keyframe.ofFloat(.1f, .9f),
//                Keyframe.ofFloat(.2f, .9f),
//                Keyframe.ofFloat(.3f, 1.1f),
//                Keyframe.ofFloat(.4f, 1.1f),
//                Keyframe.ofFloat(.5f, 1.1f),
//                Keyframe.ofFloat(.6f, 1.1f),
//                Keyframe.ofFloat(.7f, 1.1f),
//                Keyframe.ofFloat(.8f, 1.1f),
//                Keyframe.ofFloat(.9f, 1.1f),
//                Keyframe.ofFloat(1f, 1f)
//        );
//
//        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
//                Keyframe.ofFloat(0f, 1f),
//                Keyframe.ofFloat(.1f, .9f),
//                Keyframe.ofFloat(.2f, .9f),
//                Keyframe.ofFloat(.3f, 1.1f),
//                Keyframe.ofFloat(.4f, 1.1f),
//                Keyframe.ofFloat(.5f, 1.1f),
//                Keyframe.ofFloat(.6f, 1.1f),
//                Keyframe.ofFloat(.7f, 1.1f),
//                Keyframe.ofFloat(.8f, 1.1f),
//                Keyframe.ofFloat(.9f, 1.1f),
//                Keyframe.ofFloat(1f, 1f)
//        );

//        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
//                Keyframe.ofFloat(0f, 0f),
//                Keyframe.ofFloat(.1f, -3f * shakeFactor),
//                Keyframe.ofFloat(.2f, -3f * shakeFactor),
//                Keyframe.ofFloat(.3f, 3f * shakeFactor),
//                Keyframe.ofFloat(.4f, -3f * shakeFactor),
//                Keyframe.ofFloat(.5f, 3f * shakeFactor),
//                Keyframe.ofFloat(.6f, -3f * shakeFactor),
//                Keyframe.ofFloat(.7f, 3f * shakeFactor),
//                Keyframe.ofFloat(.8f, -3f * shakeFactor),
//                Keyframe.ofFloat(.9f, 3f * shakeFactor),
//                Keyframe.ofFloat(1f, 0)
//        );

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(1000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mStauts = Nor;
                clearPassword();
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    private boolean IsTrueCheckPassword( ){
        String pass = printfPassword();
        String mypass="";
        if (mCheckPassword != null && mCheckPassword.size()>0) {
            for (int it : mCheckPassword) {
                mypass = mypass + it;
            }
        }
        if (mypass.isEmpty() || pass.isEmpty())
            return false;
        return pass.equals(mypass);
    }

    private String printfPassword(){
        String pass = "";
        if (mPassword != null && mPassword.size()>0) {
            for (Integer it : mPassword) {
                pass = pass + it;
            }
        }
        return pass;
    }

    public interface ViewCallback {
        void init(GresturePasswordView view);
        void checkPassword(GresturePasswordView view, boolean check, String printfpassword);
        void getPassword(GresturePasswordView view, String password);
    }

}
