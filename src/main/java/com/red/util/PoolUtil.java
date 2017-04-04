package com.red.util;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Admin on 2017/4/3.
 */
public class PoolUtil {

    public static ExecutorService ROOM_WS_POOL = Executors.newFixedThreadPool(50);

    public static Executor RED_THREAD_POOL = Executors.newFixedThreadPool(50);

}
