package com.red.util;

import com.google.gson.JsonObject;
import com.red.constant.CommonConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OperUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void getRed(String userId, String token, String sendId, String roomId) throws Exception {
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
        HttpURLConnection httpConn = openConnection(CommonConstants.URL, para);

        BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
            result += line;
        }
        JsonObject json = (JsonObject) DataUtil.JSON_PARSER.parse(result);
        String amount = "0";
        try {
            amount = json.get("getAmount").toString();
        } catch (Exception e) {

        }

        System.out.println(amount + " " + sendId + " " +
                roomId + " " + sdf.format(new Date(System.currentTimeMillis())) + " " + userId);

        in.close();
    }

    public static String login(String userId, String up) throws Exception {
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

        HttpURLConnection httpConn = openConnection(CommonConstants.URL, para);
        BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
            result += line;
        }
        JsonObject json = (JsonObject) DataUtil.JSON_PARSER.parse(result);
        String token = json.get("token").getAsString();
        in.close();
        return token;
    }

    public static String getWsByRoomId(String roomId) {
        String ws = "";
        try {
            HttpURLConnection httpConn = openConnection(CommonConstants.WS_URL, roomId);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpConn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            JsonObject obj = (JsonObject) DataUtil.JSON_PARSER.parse(result);
            ws = obj.get("ws").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ws;
    }

    public static int getUserInfo(String userId, String token) {
        int amount = 0;
        try {
            JsonObject data = new JsonObject();
            data.addProperty("FuncTag", 10005001);
            data.addProperty("userId", userId);
            data.addProperty("token", token);
            data.addProperty("platform", 1);
            data.addProperty("a", 2);
            data.addProperty("c", 100101);

            String para = URLEncoder.encode(data.toString(), "UTF-8");
            HttpURLConnection httpConn = openConnection(CommonConstants.URL, para);

            BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            JsonObject json = (JsonObject) DataUtil.JSON_PARSER.parse(result);

            if (json.get("money") != null) {
                amount = json.get("money").getAsInt();
            }
        } catch (Exception e) {

        }
        return amount;
    }

    public static HttpURLConnection openConnection(String urlAddr, String param) throws IOException {
        URL url = new URL(String.format(urlAddr, param));
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
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
        return httpConn;
    }
}
