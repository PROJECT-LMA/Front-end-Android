package com.laioffer.lma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends OnboarderActivity {

    List<OnboarderPage> onboarderPages;
    Intent nextActivity;
    final Context context = this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStartDialog();
    }

    private void showStartDialog() {
        onboarderPages = new ArrayList<OnboarderPage>();

        Log.d("hello", "hello");
        // Create your first page
        OnboarderPage onboarderPage1 = new OnboarderPage("Step 1", "Description 1", R.drawable.ic_washer);
        OnboarderPage onboarderPage2 = new OnboarderPage("Step 2", "Description 2", R.drawable.ic_dryer);
        //OnboarderPage onboarderPage2 = new OnboarderPage(R.string.app_title, R.string.app_description, R.drawable.my_awesome_image);

        // You can define title and description colors (by default white)
        onboarderPage1.setTitleColor(R.color.white);
        onboarderPage1.setDescriptionColor(R.color.white);

        // Don't forget to set background color for your page
        onboarderPage1.setBackgroundColor(R.color.colorPrimary);
        onboarderPage2.setBackgroundColor(R.color.colorAccent);

        // Add your pages to the list
        onboarderPages.add(onboarderPage1);
        onboarderPages.add(onboarderPage2);

        // And pass your pages to 'setOnboardPagesReady' method
        setOnboardPagesReady(onboarderPages);

        onboarderPage1.setTitleTextSize(35);
        onboarderPage1.setDescriptionTextSize(20);
        onboarderPage2.setTitleTextSize(35);
        onboarderPage2.setDescriptionTextSize(20);
        setDividerVisibility(View.GONE);
        shouldUseFloatingActionButton(true);
    }

    @Override
    public void onSkipButtonPressed() {
        // Optional: by default it skips onboarder to the end
        super.onSkipButtonPressed();
        // Define your actions when the user press 'Skip' button
        nextActivity = new Intent(context, OnBoardingActivity.class);
        startActivity(nextActivity);
        finish();
    }

    @Override
    public void onFinishButtonPressed() {
        nextActivity = new Intent(context, OnBoardingActivity.class);
        startActivity(nextActivity);
        finish();
    }
}
