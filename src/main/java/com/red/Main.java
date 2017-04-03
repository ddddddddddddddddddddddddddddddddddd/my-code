/**
 * This document and its contents are protected by copyright 2012 and owned by Melot Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or any
 * incorporation of the same into any other material in any media or format of any kind is strictly prohibited.
 * All rights are reserved.
 * <p>
 * Copyright (c) Melot Inc. 2015
 */
package com.melot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Random;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.melot.constant.UserRoomConstants;
import com.melot.executor.CloseSessionExecutor;
import com.melot.executor.LoginRoomExecutor;
import com.melot.executor.UserResidualExecutor;
import com.melot.packet.Operater;
import com.melot.packet.SocketClient;

public class Main {

    private static JsonParser parser = new JsonParser();

    public static void main(String[] args) throws Exception {

        LoginRoomExecutor loginRoomExecutor = new LoginRoomExecutor();
        loginRoomExecutor.execute();
        CloseSessionExecutor closeSessionExecutor = new CloseSessionExecutor();
        closeSessionExecutor.execute();

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
                String userId = json.get("userId").getAsString();
                String up = json.get("up").getAsString();
                String token = Operater.login(userId, up);
                UserRoomConstants.UESER_TOKEN.put(userId, token);
                System.out.println("login " + userId);
                Thread.sleep(random.nextInt(20000) + 10000);
            }
            read.close();
        }


        UserResidualExecutor userResidualExecutor = new UserResidualExecutor();
        userResidualExecutor.execute();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String userId = "122831357";
                    String roomId = "117366944";
                    String up = "8I1J1D1212111K1D1K1I1ZPM8E1JYUJ3JYJZ1D121K7E";
                    // 获取房间socket
                    String ws = Operater.getWsByRoomId(roomId);
                    //登录
                    String token = Operater.login(userId, up);
                    SocketClient.connect(userId, roomId, token, ws);
//                    ReapExecutor.putUser(roomId, userId + "_" + token);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        t.start();
    }
}
