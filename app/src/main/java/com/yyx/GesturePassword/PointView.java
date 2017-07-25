package com.yyx.GesturePassword;

import android.content.Context;
import android.support.annotation.Px;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

import com.yyx.R;


/**
 * Created by java on 2017/4/19.
 */


public class PointView extends android.support.v7.widget.AppCompatImageView implements Observer{
    private int m_tag;
    private PointViewStatus m_status;

    public PointView(Context context, int tag) {
        super(context);
        setImageResource(R.drawable.gresturepoint_normal);
        m_tag = tag;
        m_status = PointViewStatus.normal;
    }

    @Override
    public void layout(@Px int l, @Px int t, @Px int r, @Px int b) {
        super.layout(l, t, r, b);
    }

    @Override
    public void update(Object message) {
        PointViewData pointViewData = (PointViewData) message;
        int tag = pointViewData.getTag();
        if (tag == m_tag)
        {
            setImageResource(R.drawable.gresturepoint_pressed);
        }
        else if(tag == -1)
        {
            setImageResource(R.drawable.gresturepoint_normal);
        }
        else if (tag == -2)
        {
            setImageResource(R.drawable.gresturepoint_error);
            TranslateAnimation translateAnimation = new TranslateAnimation(0,5,0,0);
            translateAnimation.setDuration(50);
            translateAnimation.setInterpolator(new OvershootInterpolator());
            translateAnimation.setRepeatCount(3);
            translateAnimation.setRepeatMode(Animation.REVERSE);
            startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setImageResource(R.drawable.gresturepoint_normal);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public int getM_tag() {
        return m_tag;
    }

    public void setM_tag(int m_tag) {
        this.m_tag = m_tag;
    }

    public PointViewStatus getM_status() {
        return m_status;
    }
}
