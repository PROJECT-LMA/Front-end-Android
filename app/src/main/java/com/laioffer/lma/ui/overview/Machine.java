package com.laioffer.lma.ui.overview;

public class Machine {
    private String id = null;
    private boolean isAvailable = false;
    private String userId = null;
    private String locationID = null;

    Machine(MachineBuilder machine) {
        this.id = machine.id;
        this.isAvailable = machine.isAvailable;
        this.userId = machine.userId;
        this.locationID = machine.locationID;
    }

    public String getId() {
        return id;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public String getUserId() {
        return userId;
    }

    public String getLocationID() {
        return locationID;
    }

    public static class MachineBuilder {
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

    }

}


