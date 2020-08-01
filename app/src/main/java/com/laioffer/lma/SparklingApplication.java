package com.laioffer.lma;

import android.app.Application;

import com.google.firebase.messaging.FirebaseMessaging;

public class SparklingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().subscribeToTopic("reservation");
    }
}
