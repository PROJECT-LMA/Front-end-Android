package com.laioffer.lma;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.laioffer.lma.model.User;

public class LauncherActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User.initializeUser(this);
        final User user = User.getInstance(this);
        final Context context = this;
        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent nextActivity;

                // first enter the app
                if (user.isFirstVisit()) {
                    nextActivity = new Intent(context, TutorialActivity.class);
                    user.setFirstVisit(false);
                    user.saveUserStats(context);

                    startActivity(nextActivity);
                    finish();
                    return;
                }

                // user has logged in, has chosen to remember logged in, and location is set
                if (user.isLoggedIn() && user.isRememberLoggedIn() && !user.getLocationId().isEmpty()) {
                    nextActivity = new Intent(context, SetupActivity.class);
                    startActivity(nextActivity);
                    finish();
                } else if (user.isLoggedIn() && user.isRememberLoggedIn()) {
                    nextActivity = new Intent(context, MainActivity.class);
                    startActivity(nextActivity);
                    finish();
                } else {
                    nextActivity = new Intent(context, OnBoardingActivity.class);
                    startActivity(nextActivity);
                    finish();
                }
            }
        };
        handler.postDelayed(runnable, 2000);
    }

}
