package com.laioffer.lma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;
import com.laioffer.lma.model.User;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends OnboarderActivity {

    List<OnboarderPage> onboarderPages;
    Intent nextActivity;
    final Context context = this;
    final User user = User.getInstance(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStartDialog();
    }

    private void showStartDialog() {
        onboarderPages = new ArrayList<OnboarderPage>();

        // Create page
        OnboarderPage onboarderPage1 = new OnboarderPage("LOCATION", "Choose your laundry room location", R.drawable.location);
        OnboarderPage onboarderPage2 = new OnboarderPage("OVERVIEW", "Check and Reserve machines", R.drawable.overview_reserve);
        OnboarderPage onboarderPage3 = new OnboarderPage("OVERVIEW", "Check and Reserve machines", R.drawable.overview_usage);
        OnboarderPage onboarderPage4 = new OnboarderPage("SCAN", "Scan QR Code to start your laundry", R.drawable.scan);

        // You can define title and description colors (by default white)
        onboarderPage1.setTitleColor(R.color.white);
        onboarderPage1.setDescriptionColor(R.color.white);

        // Don't forget to set background color for your page
        onboarderPage1.setBackgroundColor(R.color.colorPrimary);
        onboarderPage2.setBackgroundColor(R.color.colorAccent);
        onboarderPage3.setBackgroundColor(R.color.colorAccent);
        onboarderPage4.setBackgroundColor(R.color.colorAccent);

        onboarderPage1.setTitleTextSize(36);
        onboarderPage1.setDescriptionTextSize(21);
        onboarderPage2.setTitleTextSize(36);
        onboarderPage2.setDescriptionTextSize(21);
        onboarderPage3.setTitleTextSize(36);
        onboarderPage3.setDescriptionTextSize(21);
        onboarderPage4.setTitleTextSize(36);
        onboarderPage4.setDescriptionTextSize(21);

        shouldDarkenButtonsLayout(true);
        setSkipButtonHidden();

        // Add your pages to the list
        onboarderPages.add(onboarderPage1);
        onboarderPages.add(onboarderPage2);
        onboarderPages.add(onboarderPage3);
        onboarderPages.add(onboarderPage4);

        // And pass your pages to 'setOnboardPagesReady' method
        setOnboardPagesReady(onboarderPages);
    }

    @Override
    public void onFinishButtonPressed() {
        if (user.isLoggedIn()) {
            nextActivity = new Intent(context, MainActivity.class);
            startActivity(nextActivity);
            finish();
        } else {
            nextActivity = new Intent(context, OnBoardingActivity.class);
            startActivity(nextActivity);
            finish();
        }

    }
}
