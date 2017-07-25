package com.yyx.GesturePassword;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by java on 2017/4/19.
 */

public class GesturePasswordView extends ViewGroup implements Subject {
    private List<Observer> m_observers = new ArrayList<Observer>();
    String m_password = null;
    GestureCallBack m_callback = null;
    Context m_context = null;
    LineView m_lineView = null;
    private List<PointViewData> m_datas = new ArrayList<>();

    public GesturePasswordView(Context context, String password, GestureCallBack callback) {
        super(context);
        m_password = password;
        m_callback = callback;
        m_context = context;
        setBackgroundColor(Color.WHITE);
        addChildPoints();
        m_lineView = new LineView(m_context,this);
        addView(m_lineView);
        attach(m_lineView);
    }


    @Override
    protected void onLayout(boolean b1, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;
        for (int j = 0; j < getChildCount(); j++) {
            View pointView =  getChildAt(j);
            if (pointView instanceof PointView) {
                int viewWidth = width / 6;
                int w = width / 8;
                int row = j % 3;
                int col = j / 3;
                int left = row * (viewWidth + w) + w;
                int top = col * (viewWidth + w) + w;
                int right = left + viewWidth;
                int bottom = top + viewWidth;
                pointView.layout(left, top, right, bottom);
                PointViewData viewData = m_datas.get(j);
                viewData.setRow(row).setCol(col).setLeft(left);
                viewData.setRight(right);
                viewData.setTop(top);
                viewData.setBottom(bottom);
                viewData.setCenterX((right + left) >> 1);
                viewData.setCenterY((top + bottom) >> 1);
            }
            else
                pointView.layout(l, t, r, b);
        }
        Log.d("show", "onLayout: ");
    }

    private void addChildPoints() {
        for (int i = 0; i < 9; i++) {
            PointView iv = new PointView(m_context, i);
            GesturePasswordView.this.addView(iv);
            attach(iv);
            m_datas.add(new PointViewData().setTag(i));
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void attach(Observer observer) {
        m_observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        m_observers.remove(observer);
    }

    @Override
    public void notify(Object message) {
        for (Observer it : m_observers) {
            it.update(message);
        }
    }
    public List<PointViewData> getM_datas() {
        return m_datas;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF point = new PointF(event.getX(),event.getY());
        PointViewData pointViewData = null;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                pointViewData = pointDetection(point);
                break;
            case MotionEvent.ACTION_UP:
                String password = readPassword();
                if (!m_password.isEmpty()) {
                    if (password.equals(m_password)) {
                        m_callback.checkedSuccess();
                        pointViewData = new PointViewData().setTag(-1);
                    }
                    else {
                        m_callback.checkedFail();
                        pointViewData = new PointViewData().setTag(-2);
                    }
                }
                else
                {
                    pointViewData = new PointViewData().setTag(-1);
                    m_callback.onGestureCodeInput(password);
                }
                break;
        }
        if (pointViewData != null)
        {
            Log.d("show", "onTouchEvent: "+pointViewData.getTag());
            notify(pointViewData);
        }
        return true;
    }

    //检测当前的点属于哪个view
    public PointViewData pointDetection(PointF point)
    {
        for (PointViewData pointViewData : m_datas )
        {
            int l = pointViewData.getLeft();
            int r = pointViewData.getRight();
            int t = pointViewData.getTop();
            int b = pointViewData.getBottom();
            float x = point.x;
            float y = point.y;
            if ( x>=l && x<=r && y >=t && y<=b)
            {
                return pointViewData;
            }
        }
        return null;
    }

    public String readPassword()
    {
        StringBuilder stringBuilder = new StringBuilder();
        List<PointViewData> line= m_lineView.getM_line();
        if(line != null) {
            for (PointViewData it : line) {
                stringBuilder.append(it.getTag());
            }
        }
        return stringBuilder.toString();
    }
}
