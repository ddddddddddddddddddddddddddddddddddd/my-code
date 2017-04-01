package com.melot.executor;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.JsonObject;
import com.melot.packet.Operater;
import com.melot.packet.SocketClientSlave;

public class HandleExecutor extends Thread implements Executor {

    private static BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();

    public static Map<Integer, String> userMap = new HashMap<Integer, String>();

    @Override
    public void execute() {
        this.start();

    }

    public void run() {
        while (true) {
            try {
                int roomId = queue.take();
                Set<Integer> idSet = userMap.keySet();
                for (Integer id : idSet) {
                    String token = userMap.get(id);
                    Set<String> set = ReapExecutor.getUserByRoomId(roomId);
                    if (set == null || !set.contains(id + "_" + token)) {
                        String ws = Operater.getWsByRoomId(roomId);
                        SocketClientSlave.connect(id, roomId, token, ws);
                        ReapExecutor.putUser(roomId, id + "_" + token);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void putRoomid(int roomId) {
        queue.add(roomId);
    }

}
