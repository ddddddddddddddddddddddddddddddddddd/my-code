package com.red.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.red.util.DataUtil;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class SocketClient extends Thread {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("master open ... ");
    }

    @OnMessage
    public void onMessage(String message) throws Exception {
        try {
            if (message.contains("红包")) {
                JsonObject json = (JsonObject) DataUtil.JSON_PARSER.parse(message);
                JsonArray array = json.get("MsgList").getAsJsonArray();
                JsonObject obj = (JsonObject) array.get(0);
                String roomId = obj.get("roomId").getAsString();
                DataUtil.ROOM_QUEUE.add(roomId);
            }
        } catch (Exception e) {
            System.out.println("master error !");
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() throws IOException {
        System.out.println("master closed...");
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    public static void connect(String userId, String roomId, String token, String ws) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        Session session = container.connectToServer(SocketClient.class, URI.create(ws));
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
    }

}
