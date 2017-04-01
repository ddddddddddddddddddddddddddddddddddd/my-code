package com.melot.executor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.JsonObject;

/**
 * <p>Title: 统计数据</p>
 * <p>Description: </p>
 * <p>Company: </p>
 *
 * @author Tong
 * @date 2017年3月29日
 */
public class StatisticsExecutor extends Thread implements Executor {

    private static BlockingQueue<JsonObject> queue = new LinkedBlockingQueue<JsonObject>();

    private Map<Integer, Integer> roomRedAmount = new HashMap<Integer, Integer>();//房间发的红包数量

    private Map<Integer, Integer> userTotalRed = new HashMap<Integer, Integer>();//用户抢的红包总额

    private Map<Integer, Integer> userRedAmount = new HashMap<Integer, Integer>();//用户抢的红包个数

    @Override
    public void execute() {
        this.start();

    }

    public void handle(JsonObject json) {
        if (json.get("getAmount") != null) {
            int roomId = json.get("roomId").getAsInt();
            int userId = json.get("userId").getAsInt();
            int amount = json.get("getAmount").getAsInt();
            if (roomRedAmount.get(roomId) == null) {
                roomRedAmount.put(roomId, 1);
            } else {
                int oldAmount = roomRedAmount.get(roomId);
                roomRedAmount.put(roomId, oldAmount + 1);
            }

            if (userRedAmount.get(userId) == null) {
                userRedAmount.put(userId, 1);
            } else {
                int userOldAmount = userRedAmount.get(userId);
                userRedAmount.put(userId, userOldAmount + 1);
            }

            if (userTotalRed.get(userId) == null) {
                userTotalRed.put(userId, amount);
            } else {
                int oldTotal = userTotalRed.get(userId);
                userTotalRed.put(userId, oldTotal + amount);
            }

            printData();
        }
    }

    public void run() {
        while (true) {
            try {
                JsonObject obj = queue.take();
                if (obj != null) {
                    handle(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void putData(JsonObject json) {
        queue.add(json);
    }

    public void printData() {
        Iterator<Integer> it = roomRedAmount.keySet().iterator();
        while (it.hasNext()) {
            int key = (int) it.next();
            int value = roomRedAmount.get(key);
        }

        Iterator<Integer> it1 = userRedAmount.keySet().iterator();
        int total = 0;
        while (it1.hasNext()) {
            int key = (int) it1.next();
            int value = userRedAmount.get(key);
            total += userTotalRed.get(key);
        }
    }
}
