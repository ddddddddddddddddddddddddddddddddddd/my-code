/**
 * This document and its contents are protected by copyright 2012 and owned by Melot Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or any
 * incorporation of the same into any other material in any media or format of any kind is strictly prohibited.
 * All rights are reserved.
 * <p>
 * Copyright (c) Melot Inc. 2015
 */
package com.melot;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.melot.executor.HandleExecutor;
import com.melot.executor.ReapExecutor;
import com.melot.packet.Operater;
import com.melot.packet.SocketClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static JsonParser parser = new JsonParser();
    private static List<JsonObject> user = new ArrayList<JsonObject>();

    public static void main(String[] args) throws Exception {

        HandleExecutor handleExecutor = new HandleExecutor();
        handleExecutor.execute();
        ReapExecutor reapExecutor = new ReapExecutor();
        reapExecutor.execute();

        File file = new File(Main.class.getResource("/").getPath() + "user.conf");
        System.out.println(Main.class.getResource("/").getPath() + "user.conf");
        String encoding = "UTF-8";
        Random random = new Random();
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                JsonObject json = parser.parse(lineTxt).getAsJsonObject();
                int userId = json.get("userId").getAsInt();
                String up = json.get("up").getAsString();
                String token = Operater.login(userId, up);
                HandleExecutor.userMap.put(userId, token);
                System.out.println("login " + userId);
                Thread.sleep(random.nextInt(20000) + 10000);
            }
            read.close();
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int userId = 122831357;
                    int roomId = 79853182;
                    String up = "8I1J1D1212111K1D1K1I1ZPM8E1JYUJ3JYJZ1D121K7E";
                    // 获取房间socket
                    String ws = Operater.getWsByRoomId(roomId);
                    //登录
                    String token = Operater.login(userId, up);
                    SocketClient.connect(userId, roomId, token, ws);
                    ReapExecutor.putUser(roomId, userId + "_" + token);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        t.start();
    }
}
