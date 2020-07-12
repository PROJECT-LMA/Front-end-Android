package com.laioffer.lma.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Location {

    public static class Result {
        private List<com.laioffer.lma.model.Location> locations;
        private boolean status;

        public Result(List<com.laioffer.lma.model.Location> locations, boolean status) {
            this.locations = locations;
            this.status = status;
        }

        public List<com.laioffer.lma.model.Location> getLocations() {
            return locations;
        }

        public boolean isStatus() {
            return status;
        }
    }

    public static Result getAllLocations() {
        Result result;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.location);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();

            JSONObject object = HttpUtils.readJsonObjectFromResponse(conn);
            if (object.getBoolean("isSuccess")) {
                List<com.laioffer.lma.model.Location> locations = new ArrayList<>();
                JSONArray array = object.getJSONArray("locations");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject temp = array.getJSONObject(i);
                    locations.add(new com.laioffer.lma.model.Location(temp.getString("_id"),
                            temp.getString("name")));
                }
                result = new Result(locations, true);
            } else {
                result = new Result(null, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(null, false);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    public static Result setLocation(String locationID, String token) {
        Result result;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.location);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            JSONObject body = new JSONObject();
            body.put("locationID", locationID);
            body.put("token", token);
            HttpUtils.writeJsonObject(conn, body);
            conn.connect();

            JSONObject object = HttpUtils.readJsonObjectFromResponse(conn);
            if (object.getBoolean("isSuccess")) {
                result = new Result(null, true);
            } else {
                result = new Result(null, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(null, false);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }
}
