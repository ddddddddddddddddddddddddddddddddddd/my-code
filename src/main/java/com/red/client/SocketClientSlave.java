package com.red.packet;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.red.thread.CloseSessionThread;

@ClientEndpoint
public class SocketClientSlave {

    JsonParser parser = new JsonParser();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("slave open....");
    }

    @OnMessage
    public void onMessage(String message) throws Exception {
        if (message.contains("sendId")) {
            JsonObject json = (JsonObject) parser.parse(message);
            final String sendId = json.get("sendId").getAsString();
            final String roomId = json.get("roomId").getAsString();

            if (UserRoomConstants.RED_FILTER.putIfAbsent(roomId + "_" + sendId, System.currentTimeMillis() * 2 * 60000) != null) {
                return;
            }
            Set<String> userIdSet = UserRoomConstants.UESER_TOKEN_MAP.keySet();
            for (final String userId : userIdSet) {
                final String token = UserRoomConstants.UESER_TOKEN_MAP.get(userId);
                ReapExecutor.getRedPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Operater.getRed(userId, token, sendId, roomId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
        CloseSessionThread.putSession(userId, roomId, System.currentTimeMillis() + 3 * 60 * 1000, session);
    }

}
