package com.innvc.drysister.imgloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * @author: Jacky
 * @date : 2018/3/5 11:56
 * @Email : aa1986779407@163.com
 * @description: 加载结果类
 * 就是异步加载图片后传给Handler的数据集合
 */
public class LoaderResult {
    public ImageView img;
    public String    uri;
    public Bitmap    bitmap;
    public int       reqWidth;
    public int       reqHeight;

    public LoaderResult(ImageView img, String uri, Bitmap bitmap, int reqWidth, int reqHeight) {
        this.img = img;
        this.uri = uri;
        this.bitmap = bitmap;
        this.reqWidth = reqWidth;
        this.reqHeight = reqHeight;
    }
}
