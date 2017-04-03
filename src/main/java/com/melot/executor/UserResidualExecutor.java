package com.melot.executor;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.melot.packet.Operater;

public class UserResidualExecutor implements Executor {

    ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void execute() {
        //延迟60分钟执行，每天执行一次
        schedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Map<Integer, String> userMap = HandleExecutor.getUserMap();
                if (!userMap.isEmpty()) {
                    Iterator<Integer> it = userMap.keySet().iterator();
                    int totalAmount = 0;
                    while (it.hasNext()) {
                        int userId = it.next();
                        String token = userMap.get(userId);
                        int amount = Operater.getUserInfo(userId, token);
                        System.out.println(userId + "has money(rmb): " + amount / 1000);
                        totalAmount += amount;
                    }
                    System.out.println("total money " + totalAmount / 1000);
                }
            }
        }, 1, 24 * 60, TimeUnit.MINUTES);
    }


}
