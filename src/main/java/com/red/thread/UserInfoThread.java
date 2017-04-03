package com.red.thread;

import com.red.constant.CommonConstants;
import com.red.util.DataUtil;
import com.red.util.OperUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class UserInfoThread {

    ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();

    public void execute() {
        //延迟60分钟执行，每天执行一次
        schedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> userMap = DataUtil.UESER_TOKEN;
                    if (!userMap.isEmpty()) {
                        Iterator<String> it = userMap.keySet().iterator();
                        int totalAmount = 0;
                        while (it.hasNext()) {
                            String userId = it.next();
                            String token = userMap.get(userId);
                            int amount = OperUtil.getUserInfo(userId, token);
                            System.out.println(userId + " has money: " + amount / 1000);
                            totalAmount += amount;
                            Thread.sleep(5000L);
                        }
                        System.out.println("total money " + totalAmount / 1000);
                    }
                } catch (Exception e) {
                    System.out.println(" UserResidualExecutor error !");
                    e.printStackTrace();
                }

            }
        }, 10, 24 * 60 * 60, TimeUnit.SECONDS);
    }


}
