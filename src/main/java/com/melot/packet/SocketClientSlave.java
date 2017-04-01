package com.melot.packet;

import java.io.IOException;
import java.net.URI;

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
import com.melot.executor.ReapExecutor;

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
            String sendId = json.get("sendId").getAsString();
            String roomId = json.get("roomId").getAsString();
            ReapExecutor.putData(roomId + "_" + sendId);
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

    public static void connect(int userId, int roomId, String token, String ws) throws Exception {
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
    }

}
