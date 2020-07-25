package com.laioffer.lma.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.laioffer.lma.MainActivity;
import com.laioffer.lma.OnBoardingActivity;
import com.laioffer.lma.R;
import com.laioffer.lma.SetupActivity;
import com.laioffer.lma.adapter.LocationListAdaptor;
import com.laioffer.lma.model.Location;
import com.laioffer.lma.model.User;

import java.util.List;



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
        Preference location_preference = (Preference)findPreference("choose_location");

        if (user.getLocation().getName() != null &&  user.getLocation().getName().length() > 0) {
            location_preference.setTitle(user.getLocation().getName());
        } else {
            location_preference.setTitle("No location choosed");
        }
        setup_location(location_preference);



        //notification listener
        Preference notif_pref = (Preference)findPreference("notifications");
        notif_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(),"Opening the notification setting page",Toast.LENGTH_SHORT).show();
                //startActivityForResult(new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS), 0);
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                // for Android 8 and above
                intent.putExtra("android.provider.extra.APP_PACKAGE", getContext().getPackageName());
                startActivity(intent);
                return true;//jump to notification page
            }
        });

        // preference click, not used anymore
        Preference button = (Preference) findPreference("logout");
        setup_sign_out_btn(button);

    }

    private void setup_sign_out_btn(Preference button) {
        if (button == null) {
            Log.d("button","Button is null");
        }
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                User.logout();
                user.saveUserStats(getContext());
                Log.d("logout","Yo, user logging out");
                Intent launchActivity = new Intent(getActivity(), OnBoardingActivity.class);
                startActivity(launchActivity);
                getActivity().finish();
                return true;
            }
        });
    }

    private void setup_location(Preference location_preference) {
        if (location_preference == null) {
            return;
        }
        location_preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent setupActivity = new Intent(getActivity(), SetupActivity.class);
                startActivity(setupActivity);
                return true;
            }
        });
    }

/*
    private void setup_listPreference(ListPreference location_preference) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Location> locations = com.laioffer.lma.network.Location.getAllLocations().getLocations();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setListPreference(location_preference, locations);
                    }
                });
            }
        });
        thread.start();
    }

    private void setListPreference(ListPreference listPreference, List<Location> locations) {
        int n = locations.size();
        CharSequence[] entries = new CharSequence[n];
        CharSequence[] entryValues = new CharSequence[n];
        for (int i = 0; i < locations.size(); i++) {
            entries[i] = locations.get(i).getName();
            entryValues[i] = "0" + i;
        }
        listPreference.setEntries(entries);
        listPreference.setEntryValues(entryValues);
    }




        //log out button
        final View container = new View(getContext());
        if (container != null) {
            final Button button = (Button)container.findViewById(R.id.btn_log_out);
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User.logout();
                        user.saveUserStats(context);
                        Intent launchActivity = new Intent(getActivity(), OnBoardingActivity.class);
                        startActivity(launchActivity);
                        getActivity().finish();
                    }
                });
            }

        }*/
}


/*public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView_firstName;
    private TextView textView_lastName;
    private TextView textView_location;
    private int num = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_setting);
        textView_firstName = (TextView) root.findViewById(R.id.first_name);
        textView_lastName = (TextView) root.findViewById(R.id.last_name);
        textView_location = (TextView) root.findViewById(R.id.location_id);
        final Context context = getContext();
        final User user = User.getInstance(context);
        textView_firstName.setText(user.getFirstName());
        textView_lastName.setText(user.getLastName());
        textView_location.setText(user.getLocation().getName());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView_firstName.setText(user.getFirstName());
                        textView_lastName.setText(user.getLastName());
                        textView_location.setText(user.getLocation().getName());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        final Button button = (Button)root.findViewById(R.id.button_sign_out);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logout();
                user.saveUserStats(context);
                Intent launchActivity = new Intent(getActivity(), OnBoardingActivity.class);
                startActivity(launchActivity);
                getActivity().finish();
            }
        });
        return root;
    }
}*/


       /* settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/