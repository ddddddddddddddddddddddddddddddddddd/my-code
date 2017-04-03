package com.red.executor;

import com.red.constant.CommonConstants;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CloseSessionExecutor extends Thread implements Executor {

    private static ConcurrentHashMap<String, List<Object>> sessionMap = new ConcurrentHashMap<String, List<Object>>();

    @Override
    public void execute() {
        this.start();
    }

    public void run() {
        while (true) {
            try {
                Set<String> keys = sessionMap.keySet();
                Long now = System.currentTimeMillis();
                for (String key : keys) {
                    List<Object> list = sessionMap.get(key);
                    Long time = (Long) list.get(0);
                    if (time.longValue() <= now.longValue()) {
                        sessionMap.remove(key);
                        Session session = (Session) list.get(1);
                        Integer roomId = (Integer) list.get(2);
                        session.close();
                        ReapExecutor.removeUserByRoomId(roomId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void putSession(String userId, String roomId, Long time, Session session) {
        List<Object> list = new ArrayList<Object>(2);
        list.add(time);
        list.add(session);
        list.add(roomId);
        sessionMap.put(String.format(CommonConstants.ROOM_USER_FORMAT, roomId, userId), list);
    }
}