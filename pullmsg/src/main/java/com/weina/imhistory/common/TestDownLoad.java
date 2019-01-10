package com.weina.imhistory.common;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * @Description:
 * @Author: mayc
 * @Date: 2019/01/10 08:57
 */
public class TestDownLoad {
    public static void main(String[] args) {
        try {
            a();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void a() throws IOException {
        try {
            SSLUtil.ignoreSsl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileUrl = "https://a1.easemob.com/1117181106211103/hx-987456123/chatfiles/67b04620-13d8-11e9-ba82-91d7d85529e8";
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setConnectTimeout(5*1000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.connect();
        System.out.println("获取文件的大小为"+conn.getContentLength());
        //InputStream in = new GZIPInputStream();
        DataInputStream dataInputStream = new DataInputStream(conn.getInputStream());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        FileOutputStream fileOutputStream = new FileOutputStream(new File("F:/aa.jpg"));
        while ((length = dataInputStream.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        byte[] context=output.toByteArray();
        conn.disconnect();
        fileOutputStream.write(output.toByteArray());
        dataInputStream.close();
        fileOutputStream.close();
    }
}
