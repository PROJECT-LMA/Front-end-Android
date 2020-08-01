package com.laioffer.lma.network;

import com.laioffer.lma.model.User;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class Firebase {

    public static void sendToken(String token, String userToken) {
        HttpURLConnection conn = null;

        try{
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.sendToken);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            JSONObject object = new JSONObject();
            object.put("registrationToken", token);
            object.put("userToken", userToken);

            HttpUtils.writeJsonObject(conn, object);
            conn.connect();

            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
