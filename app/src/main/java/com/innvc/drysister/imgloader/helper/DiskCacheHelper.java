package com.innvc.drysister.imgloader.helper;

import android.content.Context;

/**
 * @author: Jacky
 * @date : 2018/3/2 17:25
 * @Email : aa1986779407@163.com
 * @description: 磁盘缓存相关
 */
public class DiskCacheHelper {
    private static final String TAG              = "DiskCacheHelper";
    private static final long   DISK_CACHE_SIZE  = 1024 * 1024 * 50;   //设置磁盘缓存区的大小为:50MB
    private static final int    DISK_CACHE_INDEX = 0;

    private Context mContext;
    private boolean mIsDiskLruCacheCreated = false; //磁盘缓存是否创建
}
