package com.red.thread;

import com.red.util.DataUtil;

import java.util.Set;

/**
 * Created by Admin on 2017/4/3.
 */
public class ClearRedFilterThread extends Thread {

    public void run() {
        while (true) {
            try {
                Set<String> keys = DataUtil.RED_FILTER.keySet();
                Long now = System.currentTimeMillis();
                for (String key : keys) {
                    long time = DataUtil.RED_FILTER.get(key);
                    if (time <= now.longValue()) {
                        DataUtil.RED_FILTER.remove(key);
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

}
