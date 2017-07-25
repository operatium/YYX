//package com.yyx.GesturePassword;
//
//import android.content.Context;
//
///**
// * Created by java on 2017/4/19.
// */
//
//public class ImageUtile {
//
//    private static class ImageUtileHolder {
//        private static final ImageUtile INSTANCE = new ImageUtile();
//    }
//
//    private ImageUtile() {
//    }
//
//    public static final ImageUtile getInstance() {
//        return ImageUtileHolder.INSTANCE;
//    }
//
//    public static RotateTransformation getGlideRotate(Context context, float rotateRotationAngle)
//    {
//        return new RotateTransformation(context,rotateRotationAngle);
//    }
//
//    //dip--->px   1dp = 1px   1dp = 2px
//    public static int dip2px(int dip){
//        //dp和px的转换关系比例值
//        float density = BaseApplication.getContext().getResources().getDisplayMetrics().density;
//        return (int)(dip*density+0.5);
//    }
//    //px---->dp
//    public static int px2dip(int px){
//        //dp和px的转换关系比例值
//        float density = BaseApplication.getContext().getResources().getDisplayMetrics().density;
//        return (int)(px/density+0.5);
//    }
//}
