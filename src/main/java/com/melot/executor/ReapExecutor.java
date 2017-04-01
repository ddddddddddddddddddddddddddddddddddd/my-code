package com.melot.executor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.JsonObject;
import com.melot.packet.Operater;

public class ReapExecutor extends Thread implements Executor {

    private static BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    private static Map<Integer, Set<String>> userMap = new HashMap<Integer, Set<String>>();

    private static Set<String> uniqueSendIdSet = new HashSet<>();

    @Override
    public void execute() {
        this.start();

    }

    public void run() {
        while (true) {
            try {
                String data = queue.take();
                String[] room = data.split("_");
                int roomId = Integer.valueOf(room[0]);
                String sendId = room[1];
                Set<String> list = userMap.get(roomId);
                if (list == null) continue;
                for (String user : list) {
                    String[] usr = user.split("_");
                    int userId = Integer.valueOf(usr[0]);
                    String token = usr[1];
                    Operater.getRed(userId, token, sendId, roomId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void putData(String data) {
        if (uniqueSendIdSet.add(data))
            queue.add(data);
    }

    public static void putUser(int roomId, String user) {
        Set<String> set = userMap.get(roomId);
        if (set == null) {
            set = new HashSet<String>();
        }
        set.add(user);
        userMap.put(roomId, set);
    }

    public static Set<String> getUserByRoomId(int roomId) {
        return userMap.get(roomId);
    }
}
