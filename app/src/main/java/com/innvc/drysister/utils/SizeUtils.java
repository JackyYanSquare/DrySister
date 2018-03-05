package com.innvc.drysister.utils;

import android.content.Context;

/**
 * @author: Jacky
 * @date : 2018/3/5 11:29
 * @Email : aa1986779407@163.com
 * @description: 尺寸转换工具类
 */
public class SizeUtils {
    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
