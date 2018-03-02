package com.innvc.drysister.imgloader.helper;

import android.content.Context;

import com.innvc.drysister.imgloader.SisterCompress;
import com.innvc.drysister.imgloader.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

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

    private Context        mContext;
    private SisterCompress mCompress;
    private boolean mIsDiskLruCacheCreated = false; //磁盘缓存是否创建
    private DiskLruCache mDiskLruCache;

    public DiskCacheHelper(Context context) {
        mContext = context;
        mCompress = new SisterCompress();
        File diskCacheDir = getDiskCacheDir(mContext, "diskCache");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdir();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询可用空间大小(兼容2.3以下版本)
     *
     * @param path
     * @return
     */
    private long getUsableSpace(File path) {
        return 0;
    }

    /**
     * 获得磁盘缓存的目录
     *
     * @param context
     * @param dirName
     * @return
     */
    private File getDiskCacheDir(Context context, String dirName) {
        return null;
    }
}
