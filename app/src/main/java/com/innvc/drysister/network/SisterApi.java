package com.innvc.drysister.network;

import android.util.Log;

import com.innvc.drysister.bean.entity.Sister;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author: Jacky
 * @date : 2018/3/1 23:54
 * @Email : aa1986779407@163.com
 * @description: 网络请求处理相关类
 */
public class SisterApi {
    private static final String TAG = "Network";
    // private static final String BASE_URL = "http://gank.io/api/data/福利/";

    // 中文转码问题，而HttpUrlConnection无法打开含有中文的链接，需要 对中文部分调用URLEncoder.encode(中文部分,"utf-8");进行转码
    private static final String BASE_URL = "http://gank.io/api/data/%e7%a6%8f%e5%88%a9/";

    /**
     * 查询妹子信息
     *
     * @param count
     * @param page
     * @return
     */
    public ArrayList<Sister> fetchSister(int count, int page) {
        String fetchUrl = BASE_URL + count + "/" + page;
        ArrayList<Sister> sisters = new ArrayList<>();
        try {
            URL url = new URL(fetchUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            int code = conn.getResponseCode();
            Log.d(TAG, "fetchSister: Server respons= " + code);
            if (code == 200) {
                InputStream in = conn.getInputStream();
                byte[] data = readFromStream(in);
                String result = new String(data, "UTF-8");
                sisters = parseSister(result);
            } else {
                Log.e(TAG, "fetchSister: 请求失败= " + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sisters;
    }

    /**
     * 读取流中数据的方法
     *
     * @param in
     * @return
     * @throws IOException
     */
    private byte[] readFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int len;
        while ((len = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        in.close();
        return outputStream.toByteArray();
    }

    /**
     * 解析返回Json数据的方法
     *
     * @param content
     * @return
     */
    private ArrayList<Sister> parseSister(String content) throws JSONException {
        ArrayList<Sister> sisters = new ArrayList<>();
        JSONObject object = new JSONObject(content);
        JSONArray array = object.getJSONArray("results");
        for (int i = 0; i < array.length(); i++) {
            JSONObject results = (JSONObject) array.get(i);
            Sister sister = new Sister();
            sister.set_id(results.getString("_id"));
            sister.setCreateAt(results.getString("createdAt"));
            sister.setDesc(results.getString("desc"));
            sister.setPublishedAt(results.getString("publishedAt"));
            sister.setSource(results.getString("source"));
            sister.setType(results.getString("type"));
            sister.setUrl(results.getString("url"));
            sister.setUsed(results.getBoolean("used") ? 1 : 0);
            sister.setWho(results.getString("who"));
            sisters.add(sister);
        }
        return sisters;
    }
}
