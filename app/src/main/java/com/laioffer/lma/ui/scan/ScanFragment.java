package com.laioffer.lma.ui.scan;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.laioffer.lma.MainActivity;
import com.laioffer.lma.R;
import com.laioffer.lma.network.Account;
import com.laioffer.lma.ui.service.Myservice;

import static android.view.View.getDefaultSize;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class ScanFragment extends Fragment {

    private ScanViewModel scanViewModel;
    private TextView txtResult;
    private Button scanToOpen;
    private Button scanToClose;
    private Fragment self;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanViewModel =
                ViewModelProviders.of(this).get(ScanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        /*
        final TextView textView = root.findViewById(R.id.text_notifications);
        scanViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


         */

        txtResult = root.findViewById(R.id.txt_result);
        scanToOpen = root.findViewById(R.id.scanToOpen_button);
        scanToClose = root.findViewById(R.id.scanToClose_button);
        self = this;

        scanToOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQRCode();
            }
        });

        scanToClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQRCode();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d(result.toString(), "test");
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                //txtResult.setText(result.getContents());
                Toast.makeText(getContext(), "Scanned : " + result.getContents(), Toast.LENGTH_LONG).show();

                // if washer is available, start service
                /*
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Account.AccountResult result = Account.scanToOpen(
                                scanString.getText().toString(),
                                token.getText().toString());
                        Activity activity = getActivity();

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                        if (result.isStatus()) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //start service
                                    getActivity().startService(new Intent(getActivity(),Myservice.class));
                                    Log.i(TAG, "Started service");
                                }
                            });
                        }
                    }
                });
                thread.start();
                */
            }
        }
    }

    private void scanQRCode() {
        // scan QR code
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1888);
        } else {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(self);

            //integrator.setCaptureActivity(MainActivity.class);
            integrator.setOrientationLocked(false);
            integrator.setPrompt("Scan QR code");
            integrator.setBeepEnabled(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);


            integrator.initiateScan();
        }
    }

    //timer
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver(br, new IntentFilter(Myservice.COUNTDOWN_BR));
        Log.i(TAG, "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            requireActivity().unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), Myservice.class));
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            Log.i(TAG, "Countdown seconds remaining: " +  millisUntilFinished / 1000);
        }
    }
}