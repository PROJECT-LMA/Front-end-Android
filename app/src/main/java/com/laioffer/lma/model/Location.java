package com.laioffer.lma.model;

public class Location {

    private String id;
    private String name;
    private int defaultRunningTime;
    private int defaultReservationExpireTime;
    private int defaultPickupTime;

    public Location() {}

    public Location(String id, String name, int defaultRunningTime, int defaultReservationExpireTime, int defaultPickupTime) {
        this.id = id;
        this.name = name;
        this.defaultRunningTime = defaultRunningTime;
        this.defaultReservationExpireTime = defaultReservationExpireTime;
        this.defaultPickupTime = defaultPickupTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefaultRunningTime() {
        return defaultRunningTime;
    }

    public void setDefaultRunningTime(int defaultRunningTime) {
        this.defaultRunningTime = defaultRunningTime;
    }

    public int getDefaultReservationExpireTime() {
        return defaultReservationExpireTime;
    }

    public void setDefaultReservationExpireTime(int defaultReservationExpireTime) {
        this.defaultReservationExpireTime = defaultReservationExpireTime;
    }

    public int getDefaultPickupTime() {
        return defaultPickupTime;
    }

    public void setDefaultPickupTime(int defaultPickupTime) {
        this.defaultPickupTime = defaultPickupTime;
    }
}
