package com.example.inout.home.signin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inout.R;

import java.util.Locale;

public class AttendActivity extends AppCompatActivity {
    ImageView qr,finger;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);
        qr = findViewById(R.id.qrcode);
        finger = findViewById(R.id.finger);

//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
//
//        }else
//        {
//            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)  .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//
//        }
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(this)
                    .setMessage("gps_network_not_enabled")
                    .setPositiveButton("open_location_settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    //.setNegativeButton("Cancel",null)
                    .show();

        }else
        {
            finger.setOnClickListener(v -> startActivity(new Intent(AttendActivity.this, Fingerprintlogin.class)));

        }


        qr.setOnClickListener(v -> startActivity(new Intent(  AttendActivity.this, QRActivitylogin.class)));
    }
}