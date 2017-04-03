package com.red.thread;

import com.red.client.SocketClientSlave;
import com.red.constant.CommonConstants;
import com.red.util.DataUtil;
import com.red.util.OperUtil;
import com.red.util.PoolUtil;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LoginRoomThread extends Thread {

    public void run() {
        while (true) {
            try {
                final String roomId = DataUtil.ROOM_QUEUE.take();
                Set<String> idSet = DataUtil.UESER_TOKEN.keySet();
                for (final String id : idSet) {
                    final String token = DataUtil.UESER_TOKEN.get(id);
                    PoolUtil.ROOM_WS_POOL.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String ws = OperUtil.getWsByRoomId(roomId, OperUtil.openConnection(CommonConstants.WS_URL));
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
