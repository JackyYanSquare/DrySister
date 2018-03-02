package com.innvc.drysister.imgloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * @author: Jacky
 * @date : 2018/3/2 14:28
 * @Email : aa1986779407@163.com
 * @description: 图片压缩类
 */
public class SisterCompress {
    private static final String TAG = "ImageCompress";

    public SisterCompress() {
    }

    /**
     * 压缩资源图片
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        //计算缩放比例
        options.inSampleSize = computeSimpleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 压缩图片文件
     *
     * @param descriptor
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeBitmapFromFileDescriptor(FileDescriptor descriptor, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(descriptor, null, options);
        options.inSampleSize = computeSimpleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(descriptor, null, options);
    }

    /**
     * 计算缩放比例的方法
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int computeSimpleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        int inSampleSize = 1;
        int height = options.outHeight;
        int width = options.outWidth;
        Log.v(TAG, "原图大小为：" + width + "x" + height);
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.v(TAG, "inSampleSize = " + inSampleSize);
        return inSampleSize;
    }
}
