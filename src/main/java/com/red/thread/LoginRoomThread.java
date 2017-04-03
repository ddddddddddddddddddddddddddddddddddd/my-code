package com.red.executor;

import com.red.constant.UserRoomConstants;
import com.red.packet.Operater;
import com.red.packet.SocketClientSlave;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginRoomExecutor extends Thread implements Executor {

    private static ExecutorService roomPool = Executors.newFixedThreadPool(20);

    @Override
    public void execute() {
        this.start();

    }

    public void run() {
        while (true) {
            try {
                final String roomId = UserRoomConstants.ROOM_QUEUE.take();
                Set<String> idSet = UserRoomConstants.UESER_TOKEN.keySet();
                for (final String id : idSet) {
                    final String token = UserRoomConstants.UESER_TOKEN.get(id);
                    roomPool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String ws = Operater.getWsByRoomId(roomId);
                                SocketClientSlave.connect(id, roomId, token, ws);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
