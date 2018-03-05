package com.innvc.drysister.imgloader;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.innvc.drysister.R;
import com.innvc.drysister.imgloader.helper.DiskCacheHelper;
import com.innvc.drysister.imgloader.helper.MemoryCacheHelper;
import com.innvc.drysister.imgloader.helper.NetworkHelper;
import com.innvc.drysister.utils.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Jacky
 * @date : 2018/3/5 11:57
 * @Email : aa1986779407@163.com
 * @description: 图片加载逻辑控制类：
 */
public class SisterLoader {
    private static final String TAG = "SisterLoader";

    public static final  int MESSAGE_POST_RESULT = 1;
    private static final int TAG_KEY_URI         = R.id.sister_loader_uri;  //一个常量值，setTag用到

    private static final int  CPU_COUNT         = Runtime.getRuntime().availableProcessors();    //获取CPU个数
    private static final int  CORE_POOL_SIZE    = CPU_COUNT + 1;    //核心线程数
    private static final int  MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1; //最大线程池大小
    private static final long KEEP_ALIVE        = 10L; //线程空闲时间

    private       Context           mContext;
    private final MemoryCacheHelper mMemoryHelper;
    private final DiskCacheHelper   mDiskHelper;

    private static final ThreadFactory mFactory             = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "SisterLoader#" + mCount.getAndIncrement());
        }
    };
    /**
     * 线程池管理线程
     */
    public static final  Executor      THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), mFactory);
    private              Handler       mMainHandler         = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public SisterLoader(Context context) {
        mContext = context.getApplicationContext();
        mMemoryHelper = new MemoryCacheHelper(mContext);
        mDiskHelper = new DiskCacheHelper(context);
    }

    public static SisterLoader getInstance(Context context) {
        return new SisterLoader(context);
    }

    /**
     * 同步加载图片,该方法只能在主线程执行
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        String key = NetworkHelper.hashKeyFormUrl(url);
        //先到内存缓存里找
        Bitmap bitmap = mMemoryHelper.getBitmapFromMemoryCache(url);
        if (bitmap != null) {
            return bitmap;
        }
        // 到磁盘缓存里找
        try {
            bitmap = mDiskHelper.loadBitmapFromDiskCache(key, reqWidth, reqHeight);
            //如果磁盘缓存中找到，往内存缓存里面塞一个
            if (bitmap != null) {
                mMemoryHelper.addBitmapToMemoryCache(key, bitmap);
                return bitmap;
            }
            //磁盘里也找不到，加载网络
            if (NetworkUtils.isAvailable(mContext)) {
                bitmap = mDiskHelper.saveImgByte(key, reqWidth, reqHeight, NetworkHelper.downloadUrlToStream(url));
                Log.d(TAG, "加载网络上的图片，URL：" + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap == null && !mDiskHelper.getIsDiskCacheCreate()) {
            Log.w(TAG, "磁盘缓存未创建！");
            bitmap = NetworkHelper.downloadBitmapFromUrl(url);
        }
        return bitmap;
    }

    public void bindBitmap(final String url, final ImageView imageView, final int reqWidth, final int reqHeight) {
        String key = NetworkHelper.hashKeyFormUrl(url);
        imageView.setTag(TAG_KEY_URI, url);
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(imageView, url, bitmap, reqWidth, reqHeight);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();
                }
            }
        };
    }
}
