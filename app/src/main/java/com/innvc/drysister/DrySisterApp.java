package com.innvc.drysister;

import android.app.Application;
import android.content.Context;

/**
 * @author: Jacky
 * @date : 2018/3/2 10:45
 * @Email : aa1986779407@163.com
 * @description:
 */
public class DrySisterApp extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        // CrashHandler.getInstance().init(this);
    }

    public static DrySisterApp getContext() {
        return (DrySisterApp) sContext;
    }
}
