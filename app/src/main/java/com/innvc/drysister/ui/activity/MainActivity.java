package com.innvc.drysister.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.innvc.drysister.R;
import com.innvc.drysister.bean.entity.Sister;
import com.innvc.drysister.imgloader.PictureLoader;
import com.innvc.drysister.imgloader.SisterLoader;
import com.innvc.drysister.network.SisterApi;

import java.util.ArrayList;

/**
 * @author : Jacky
 * @date : 2018/3/2 0:26
 * @Email : aa1986779407@163.com
 * @description: http://www.runoob.com/w3cnote/android-tutorial-exercise-2.html
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int curPos = 0;      // 当前显示的是哪一张
    private int page   = 1;      // 当前页数

    private ImageView         showImg;
    private Button            showBtn;
    private Button            refreshBtn;
    private PictureLoader     loader;
    private SisterApi         sisterApi;
    private ArrayList<Sister> data;
    private SisterTask        sisterTask;
    private SisterLoader      mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sisterApi = new SisterApi();
        loader = new PictureLoader();
        mLoader = SisterLoader.getInstance(MainActivity.this);
        mLoader.bindBitmap(data.get(curPos).getUrl(), showImg, 400, 400);
        initData();
        initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sisterTask.cancel(true);
    }

    private void initData() {
        data = new ArrayList<>();
        // new SisterTask(page).execute();  // 该方法可能导致内存溢出
        /*data.add("http://ww4.sinaimg.cn/large/610dc034jw1f6ipaai7wgj20dw0kugp4.jpg");
        data.add("http://ww3.sinaimg.cn/large/610dc034jw1f6gcxc1t7vj20hs0hsgo1.jpg");
        data.add("http://ww4.sinaimg.cn/large/610dc034jw1f6f5ktcyk0j20u011hacg.jpg");
        data.add("http://ww1.sinaimg.cn/large/610dc034jw1f6e1f1qmg3j20u00u0djp.jpg");
        data.add("http://ww3.sinaimg.cn/large/610dc034jw1f6aipo68yvj20qo0qoaee.jpg");
        data.add("http://ww3.sinaimg.cn/large/610dc034jw1f69c9e22xjj20u011hjuu.jpg");
        data.add("http://ww3.sinaimg.cn/large/610dc034jw1f689lmaf7qj20u00u00v7.jpg");
        data.add("http://ww3.sinaimg.cn/large/c85e4a5cjw1f671i8gt1rj20vy0vydsz.jpg");
        data.add("http://ww2.sinaimg.cn/large/610dc034jw1f65f0oqodoj20qo0hntc9.jpg");
        data.add("http://ww2.sinaimg.cn/large/c85e4a5cgw1f62hzfvzwwj20hs0qogpo.jpg");*/
    }

    private void initUI() {
        showImg = findViewById(R.id.iv_img);
        showBtn = findViewById(R.id.btn_check);
        refreshBtn = findViewById(R.id.btn_change);
        showBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check:
                if (data != null && !data.isEmpty()) {
                    if (curPos > 9) {
                        curPos = 0;
                    }
                    loader.load(showImg, data.get(curPos).getUrl());
                    curPos++;
                }
                break;
            case R.id.btn_change:
                // page++;
                sisterTask = new SisterTask();
                sisterTask.execute();
                curPos = 0;
                break;
        }
    }

    private class SisterTask extends AsyncTask<Void, Void, ArrayList<Sister>> {

        public SisterTask() {
        }

        @Override
        protected ArrayList<Sister> doInBackground(Void... voids) {
            return sisterApi.fetchSister(10, page);
        }

        @Override
        protected void onPostExecute(ArrayList<Sister> sisters) {
            super.onPostExecute(sisters);
            data.clear();
            data.addAll(sisters);
            page++;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            sisterTask = null;
        }
    }
}
