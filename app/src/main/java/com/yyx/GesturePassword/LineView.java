package com.yyx.GesturePassword;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.Px;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by java on 2017/4/20.
 */

public class LineView extends View implements Observer {
    private GesturePasswordView m_parentView = null;
    private Bitmap m_bitmap = null;
    private Paint m_paint = null;
    private Canvas m_canvas = null;
    private List<PointViewData> m_line = null;

    public LineView(Context context, GesturePasswordView parentView) {
        super(context);
        this.m_parentView = parentView;
        m_paint = new Paint();
        m_canvas = new Canvas();
//        m_paint.setStrokeWidth(ImageUtile.dip2px(19));
        m_paint.setColor(Color.rgb(0, 160, 222));
        m_paint.setAntiAlias(true);// 不显示锯齿
        m_paint.setStyle(Paint.Style.STROKE);// 设置非填充
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_bitmap, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void layout(@Px int l, @Px int t, @Px int r, @Px int b) {
        super.layout(l, t, r, b);
        if (m_bitmap == null)
            m_bitmap = Bitmap.createBitmap(r - l, b - t, Bitmap.Config.ARGB_8888);
        m_canvas.setBitmap(m_bitmap);
    }

    @Override
    public void update(Object message) {
        if (m_line == null)
            m_line = new ArrayList<>();
        PointViewData pointViewData = (PointViewData) message;
        if (pointViewData != null) {
            int mytag = pointViewData.getTag();
            if (mytag <0)//清屏
            {
                m_line.clear();
                clear();
            }
            else
            {
                if (m_line.size() > 0) {
                    PointViewData end = m_line.get(m_line.size() - 1);
                    if (end.getTag() != mytag) {
                        m_line.add(pointViewData);
                        drawLine();
                    }
                } else {
                    m_line.add(pointViewData);
                    drawLine();
                }
            }
        }
    }

    public void drawLine() {
        m_canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        int fromx = -999;
        int fromy = -999;
        for (PointViewData pointData : m_line) {
            int x = pointData.getCenterX();
            int y = pointData.getCenterY();
            if (fromx >= 0 && fromy >= 0) {
                m_canvas.drawLine(fromx, fromy, x, y, m_paint);
            }
            fromx = x;
            fromy = y;
        }
        invalidate();
    }

    public void clear() {
        m_canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public List<PointViewData> getM_line() {
        return m_line;
    }
}
