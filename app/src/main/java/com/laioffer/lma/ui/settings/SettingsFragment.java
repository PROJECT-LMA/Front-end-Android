package com.laioffer.lma.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.laioffer.lma.OnBoardingActivity;
import com.laioffer.lma.R;
import com.laioffer.lma.SetupActivity;
import com.laioffer.lma.model.User;

public class SettingsFragment extends PreferenceFragmentCompat {
    final Context context = getContext();
    final User user = User.getInstance(context);

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        Preference user_name = findPreference("user_name_preference");
        if (user_name != null) {
            user_name.setTitle(user.getFirstName() + " " + user.getLastName());
        }

        //set location list
        Preference location_preference = findPreference("choose_location");
        if (location_preference == null) {
            return;
        }
        if (user.getLocation().getName() != null && user.getLocation().getName().length() > 0) {

            location_preference.setTitle(user.getLocation().getName());
        } else {
            location_preference.setTitle("No location choosed");
        }
        setup_location(location_preference);


        //notification listener
        Preference notification_pref = findPreference("notifications");
        if (notification_pref == null) {
            return;
        }
        notification_pref.setOnPreferenceClickListener(preference -> {
            Toast.makeText(getActivity(), "Opening the notification setting page", Toast.LENGTH_SHORT).show();
            //startActivityForResult(new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS), 0);
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            // for Android 8 and above
            intent.putExtra("android.provider.extra.APP_PACKAGE", requireContext().getPackageName());
            startActivity(intent);
            return true;//jump to notification page
        });

        // preference click, not used anymore
        Preference button = findPreference("logout");
        setup_sign_out_btn(button);

    }

    private void setup_sign_out_btn(Preference button) {
        if (button == null) {
            return;
        }
        button.setOnPreferenceClickListener(preference -> {
            User.logout();
            user.saveUserStats(getContext());
            Intent launchActivity = new Intent(getActivity(), OnBoardingActivity.class);
            startActivity(launchActivity);
            requireActivity().finish();
            return true;
        });
    }

    private void setup_location(Preference location_preference) {
        if (location_preference == null) {
            return;
        }
        location_preference.setOnPreferenceClickListener(preference -> {
            Intent setupActivity = new Intent(getActivity(), SetupActivity.class);
            startActivity(setupActivity);
            return true;
        });
    }

}
