package com.laioffer.lma.network;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class HttpUtils {

    static final String serverUrl = "http://lmapp.us-east-2.elasticbeanstalk.com";

    static final String register = "/api/users/register";
    static final String login = "/api/users/login";
    static final String checkEmail = "/api/checkEmail";
    static final String location = "/api/locations";
    static final String scanToOpen = "/api/scanToOpen";
    static final String scanToClose = "/api/scanToClose";

    public static JSONObject readJsonObjectFromResponse(HttpURLConnection conn) {
        if (null==conn) {
            return new JSONObject();
        }
        StringBuilder responseBody = new StringBuilder();
        try {
            // Create a BufferedReader to help read text from a character-input stream.
            // Provide for the efficient reading of characters, arrays, and lines.
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
            reader.close();
            return new JSONObject(responseBody.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    // Writes a JSONObject to http response.
    public static void writeJsonObject(HttpURLConnection conn, JSONObject obj) throws IOException {
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(obj.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
