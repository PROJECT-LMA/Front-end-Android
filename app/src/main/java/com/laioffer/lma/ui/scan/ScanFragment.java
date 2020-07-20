package com.laioffer.lma.ui.scan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.laioffer.lma.R;
import com.laioffer.lma.model.User;
import com.laioffer.lma.network.Scan;
import com.laioffer.lma.service.TimerService;

public class ScanFragment extends Fragment {

    private Fragment self;
    private static final int MY_PERMISSION_CODE = 1888;
    private String type = "";
    private String token = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        final User user = User.getInstance(getContext());
        self = this;
        Button scanToOpen = root.findViewById(R.id.scanToOpen_button);
        Button scanToClose = root.findViewById(R.id.scanToClose_button);

        // scan to open
        scanToOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQRCode("open", user.getToken());
            }
        });

        //scan to close
        scanToClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQRCode("close", user.getToken());
            }
        });
        return root;
    }

    // scan QR code
    private void scanQRCode(String type, String token) {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_CODE);
        } else {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(self);
            integrator.setOrientationLocked(false);
            integrator.setPrompt("Scan QR code");
            integrator.setBeepEnabled(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            this.type = type;
            this.token = token;

            integrator.initiateScan();
        }
    }

    // handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        switch (requestCode){
            case MY_PERMISSION_CODE:
                if (permissionCheck == PackageManager.PERMISSION_GRANTED){
                    IntentIntegrator integrator = IntentIntegrator.forSupportFragment(self);
                    integrator.setOrientationLocked(false);
                    integrator.setPrompt("Scan QR code");
                    integrator.setBeepEnabled(false);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                    this.type = type;
                    this.token = token;

                    integrator.initiateScan();
                } else {
                    Toast.makeText(getContext(),"This app requires camera permission to be granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // handle scan result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // if washer is available, start service
                switch (type) {
                    // case 0
                    case "open":
                        Thread thread1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final Scan.ScanResult result1 = Scan.scanToOpen(
                                        result.getContents(),
                                        token
                                );
                                Activity activity = getActivity();

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), result1.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                                if (result1.isStatus()) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //start service
                                            getActivity().startService(new Intent(getActivity(), TimerService.class));
                                        }
                                    });
                                }
                            }
                        });
                        thread1.start();
                        break;

                    // case 1
                    case "close":
                        Thread thread2 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final Scan.ScanResult result2 = Scan.scanToOpen(
                                        result.getContents(),
                                        token
                                );
                                Activity activity = getActivity();

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), result2.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                                if (result2.isStatus()) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //finish
                                            Toast.makeText(getContext(), "Thank you for using", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
                        thread2.start();
                        break;
                }
            }
        }
    }
}