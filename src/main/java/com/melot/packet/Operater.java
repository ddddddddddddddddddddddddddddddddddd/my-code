package com.melot.packet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.melot.executor.StatisticsExecutor;
import com.melot.util.EncryptUtil;

public class Operater {

    private static JsonParser parser = new JsonParser();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String login(int userId, String up) throws Exception {
        String url = "https://sapi.kktv1.com/meShow/entrance";
        JsonObject data = new JsonObject();
        data.addProperty("FuncTag", 40000015);
        data.addProperty("rc", "E52A4_" + userId);
        data.addProperty("up", up);
        data.addProperty("platform", 1);
        data.addProperty("a", 2);
        data.addProperty("c", 100101);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("FuncTag", 40000015);
        map.put("rc", "E52A4_" + userId);
        map.put("up", up);
        map.put("platform", 1);
        map.put("a", 2);
        map.put("c", 100101);
        String sv = EncryptUtil.slist_web(map);
        data.addProperty("sv", sv);
        String para = URLEncoder.encode(data.toString(), "UTF-8");
        url = url + "?parameter=" + para;
        URL U = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection) U.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setUseCaches(false);
        httpConn.setRequestMethod("GET");
        // 设置请求属性
        httpConn.setRequestProperty("Content-Type", "application/octet-stream");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        httpConn.setRequestProperty("Charset", "UTF-8");
        httpConn.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
            result += line;
        }
        JsonObject json = (JsonObject) parser.parse(result);
        String token = json.get("token").getAsString();
        in.close();
        return token;
    }

    public static void getRed(int userId, String token, String sendId, int roomId) throws Exception {
        String url = "https://sapi.kktv1.com/meShow/entrance";
        JsonObject data = new JsonObject();
        data.addProperty("roomId", roomId);
        data.addProperty("sendId", sendId);
        data.addProperty("FuncTag", 40000018);
        data.addProperty("userId", userId);
        data.addProperty("token", token);
        data.addProperty("platform", 1);
        data.addProperty("a", 1);
        data.addProperty("c", 100101);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roomid", roomId);
        map.put("sendId", sendId);
        map.put("FuncTag", 40000018);
        map.put("userId", userId);
        map.put("token", token);
        map.put("platform", 1);
        map.put("a", 1);
        map.put("c", 100101);
        String sv = EncryptUtil.slist_web(map);
        data.addProperty("sv", sv);
        String para = URLEncoder.encode(data.toString(), "UTF-8");
        url = url + "?parameter=" + para;
        URL U = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection) U.openConnection();
        httpConn.setDoOutput(true);// 使用 URL 连接进行输出
        httpConn.setDoInput(true);// 使用 URL 连接进行输入
        httpConn.setUseCaches(false);// 忽略缓存
        httpConn.setRequestMethod("GET");// 设置URL请求方法
        // 设置请求属性
        httpConn.setRequestProperty("Content-Type", "application/octet-stream");
        httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
        httpConn.setRequestProperty("Charset", "UTF-8");
        httpConn.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
            result += line;
        }
        JsonObject json = (JsonObject) parser.parse(result);
        String amount = "0";
        try {
            amount = json.get("getAmount").toString();
        } catch (Exception e) {

        }

        System.out.println(amount + " " + sendId + " " +
                roomId + " " + sdf.format(new Date(Long.parseLong(json.get("dtime").toString()))) + " " + userId);
//		StatisticsExecutor.putData(json);

        in.close();
    }

    public static String getWsByRoomId(int roomId) {
        String ws = "";
        try {
            String url = "http://into1.kktv8.com/?roomId=" + roomId;
            URL U = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) U.openConnection();
            httpConn.setDoOutput(true);// 使用 URL 连接进行输出
            httpConn.setDoInput(true);// 使用 URL 连接进行输入
            httpConn.setUseCaches(false);// 忽略缓存
            httpConn.setRequestMethod("GET");// 设置URL请求方法
            // 设置请求属性
            httpConn.setRequestProperty("Content-Type", "application/octet-stream");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");
            httpConn.connect();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpConn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            JsonObject obj = (JsonObject) parser.parse(result);
            ws = obj.get("ws").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ws;
    }
}
