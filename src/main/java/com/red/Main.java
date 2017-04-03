
package com.red;

import com.google.gson.JsonObject;
import com.red.client.SocketClient;
import com.red.thread.ClearRedFilterThread;
import com.red.thread.CloseSessionThread;
import com.red.thread.LoginRoomThread;
import com.red.thread.UserInfoThread;
import com.red.util.DataUtil;
import com.red.util.OperUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {

        //副账户
        File file = new File(Main.class.getResource("/").getPath() + "user");
        System.out.println(Main.class.getResource("/").getPath() + "user");
        String encoding = "UTF-8";
        Random random = new Random();
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                JsonObject json = DataUtil.JSON_PARSER.parse(lineTxt).getAsJsonObject();
                String userId = json.get("userId").getAsString();
                String up = json.get("up").getAsString();
                String token = OperUtil.login(userId, up);
                DataUtil.UESER_TOKEN.put(userId, token);
                System.out.println("login " + userId);
                Thread.sleep(random.nextInt(20000) + 10000);
            }
            read.close();
        }

        //thread start
        UserInfoThread userInfoThread = new UserInfoThread();
        userInfoThread.execute();

        LoginRoomThread loginRoomThread = new LoginRoomThread();
        loginRoomThread.start();

        CloseSessionThread closeSessionThread = new CloseSessionThread();
        closeSessionThread.start();

        ClearRedFilterThread clearRedFilterThread = new ClearRedFilterThread();
        clearRedFilterThread.start();

        //main account
        String userId = "122831357";
        String roomId = "117366944";
        String up = "8I1J1D1212111K1D1K1I1ZPM8E1JYUJ3JYJZ1D121K7E";
        String ws = OperUtil.getWsByRoomId(roomId);
        String token = OperUtil.login(userId, up);
        SocketClient.connect(userId, roomId, token, ws);

    }
}
