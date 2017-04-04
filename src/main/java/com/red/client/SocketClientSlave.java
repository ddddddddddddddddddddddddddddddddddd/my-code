package com.red.client;

import com.google.gson.JsonObject;
import com.red.util.DataUtil;
import com.red.util.OperUtil;
import com.red.util.PoolUtil;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

@ClientEndpoint
public class SocketClientSlave {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("slave open....");
    }

    @OnMessage
    public void onMessage(String message) throws Exception {
        if (message.contains("sendId")) {
            JsonObject json = (JsonObject) DataUtil.JSON_PARSER.parse(message);
            final String sendId = json.get("sendId").getAsString();
            final String roomId = json.get("roomId").getAsString();

            if (DataUtil.RED_FILTER.putIfAbsent(roomId + "_" + sendId, System.currentTimeMillis() + 2 * 60000) != null) {
                return;
            }
//            int count = 0;
            Set<String> userIdSet = DataUtil.UESER_TOKEN.keySet();
            for (final String userId : userIdSet) {
                final String token = DataUtil.UESER_TOKEN.get(userId);
                /*if (count > 1) {
                    try {
                        OperUtil.getRed(userId, token, sendId, roomId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {*/
                    PoolUtil.RED_THREAD_POOL.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OperUtil.getRed(userId, token, sendId, roomId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
//                }
//                count += 1;
            }
        }
    }

    @OnClose
    public void onClose() throws IOException {
        System.out.println("slave close...");
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    public static void connect(String userId, String roomId, String token, String ws) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        Session session = container.connectToServer(SocketClientSlave.class, URI.create(ws));
        JsonObject msg = new JsonObject();
        msg.addProperty("MsgTag", 10010201);
        msg.addProperty("platform", 1);
        msg.addProperty("roomId", roomId);
        msg.addProperty("container", 1);
        msg.addProperty("softVersion", 10040);
        msg.addProperty("linking", "www.kktv5.com");
        msg.addProperty("userId", userId);
        msg.addProperty("token", token);
        session.getBasicRemote().sendText(msg.toString());
        DataUtil.putSession(userId, roomId, System.currentTimeMillis() + 3 * 60 * 1000, session);
    }

}
