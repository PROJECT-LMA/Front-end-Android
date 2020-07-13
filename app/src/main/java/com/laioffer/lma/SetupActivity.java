package com.laioffer.lma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

    }

    public void buttonOnClick(View view) {
        Button button = (Button) view;
        Toast toast = Toast.makeText(this, "Region selected.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
