package com.yyx.GesturePassword;

/**
 * Created by java on 2017/4/21.
 */

public interface GestureCallBack {
    /**
     * 用户设置/输入了手势密码
     */
    void onGestureCodeInput(String inputCode);

    /**
     * 代表用户绘制的密码与传入的密码相同
     */
    void checkedSuccess();

    /**
     * 代表用户绘制的密码与传入的密码不相同
     */
    void checkedFail();
}
