package com.innvc.drysister.imgloader.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * @author: Jacky
 * @date : 2018/3/2 16:38
 * @Email : aa1986779407@163.com
 * @description: 内存缓存协助类
 */
public class MemoryCacheHelper {
    private static final String TAG = "MemoryCacheHelper";
    private Context                  mContext;
    private LruCache<String, Bitmap> mMemoryCache;

    public MemoryCacheHelper(Context mContext) {
        this.mContext = mContext;
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);  // 获得应用最大内存
        int catchSize = maxMemory / 8;    // 缓存大小
        mMemoryCache = new LruCache<String, Bitmap>(catchSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    /**
     * 获取LruCache对象
     *
     * @return
     */
    public LruCache<String, Bitmap> getmMemoryCache() {
        return mMemoryCache;
    }

    /**
     * 根据key取出LruCache中的Bitmap
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        Log.v(TAG, "加载内存缓存中的图片.");
        return mMemoryCache.get(key);
    }

    /**
     * 按照key值往LruCache里塞Bitmap
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            Log.v(TAG, "addBitmapToMemoryCache");
            mMemoryCache.put(key, bitmap);
        }
    }
}
