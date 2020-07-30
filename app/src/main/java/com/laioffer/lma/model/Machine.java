package com.laioffer.lma.model;

import org.json.JSONException;
import org.json.JSONObject;
public class Machine {

    private final static String TAG = "lifecycle";
    private String SN = null;
    private String id = null;
    private String isAvailable = null;
    private String machineType = null;
    private String userID = null;
    private String locationID = null;
    private String scanString = null;
    private String startTime = null;
    private String isReserved = null;
//is picked_up
    public void setupMachine(JSONObject response) throws JSONException {
        //Log.d(TAG, response.toString());
        this.id = response.getString("_id");
        this.SN = response.getString("sn");
        this.isAvailable = response.getString("isAvailable");
        this.machineType = response.getString("machineType");
        //Log.d(TAG, "machineType : "+ machineType);
        this.userID = response.getString("userID");
        this.locationID = response.getString("locationID");

        //Log.d(TAG, "locationID "+ locationID);
        this.scanString = response.getString("scanString");
        this.startTime = response.getString("startTime");
        this.isReserved = response.getString("isReserved");
    }


    public String getId() {
        return id;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public String getUserID() {
        return userID;
    }

    public String getLocationID() {
        return locationID;
    }

    public String getMachineType() { return machineType; }

    public String getSN() { return SN; }

    public String getStartTime() { return startTime; }

    public String getIsReserved() {return isReserved;}
}

 /*Machine(MachineBuilder machine) {
        this.id = machine.id;
        this.isAvailable = machine.isAvailable;
        this.userId = machine.userId;
        this.locationID = machine.locationID;
    }*/

    /*public static class MachineBuilder {
        private String id = null;
        private boolean isAvailable = false;
        private String userId = null;
        private String locationID = null;


        public void setId(String id) {
            this.id = id;
        }

        public void setIsAvailable(boolean isAvailable) {
            this.isAvailable = isAvailable;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setlocationID(String locationID) {
            this.locationID = locationID;
        }

        public Machine build() {
            return new Machine(this);
        }

    }*/



