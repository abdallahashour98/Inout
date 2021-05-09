package com.example.inout.home.signin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.inout.MainActivity;
import com.example.inout.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.inout.R.string.ErroraddingTime;
import static com.example.inout.R.string.writtenwithTime;


public class Fingerprintlogin extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //initialize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    TextView latitude, longitude;
    private Object Timestamp;
    String userdb;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        firebaseAuth = FirebaseAuth.getInstance();
        //assign variable
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        //method finger
        finger();
        //method currentmap
        currentMap();
    }
    private void currentMap() {
        //assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager() .findFragmentById(R.id.googelemap);
        //initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);
        //check permissions
        if (ActivityCompat.checkSelfPermission(Fingerprintlogin.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Fingerprintlogin.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permissions granted
            @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    //sync map
                    supportMapFragment.getMapAsync(googleMap -> {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        //create marker
                        MarkerOptions options = new MarkerOptions().position(latLng).title("current location");
                        //zoom map
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        //add marker on map
                        googleMap.addMarker(options);
                        /////
                        Location Location = task.getResult();
                        if (Location != null) {
                            //when location not null
                            //set location in textview
                            latitude.setText(String.valueOf(Location.getLatitude()));
                            longitude.setText(String.valueOf(Location.getLongitude()));
                        }
                    });

                };
            });
        } else {
            //when permissions denied
            //req permissions

           ActivityCompat.requestPermissions(Fingerprintlogin.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

        }
    }
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void finger() {
        Executor executor = Executors.newSingleThreadExecutor();

        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle(getString(R.string.fingerprint))
                .setNegativeButton(getString(R.string.cancel), executor, (dialog, which) -> {
                }).build();

        Fingerprintlogin activity = this;
        biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                {
                    activity.runOnUiThread(() -> {
                        AlertDialog.Builder scan = new AlertDialog.Builder(Fingerprintlogin.this);
                        scan.setTitle(R.string.fingerprint);
                        scan.setMessage(R.string.authenticate);
                        scan.setPositiveButton(R.string.thanks, (dialog, which) -> startActivity(new Intent(Fingerprintlogin.this, MainActivity.class)));
                        scan.create().show();
                        settimeinfirebase();

                    });
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            //when permissions granted
            //call method
            currentMap();
        }else {
            //when permissions denied
            //display Toast
            Toast.makeText( Fingerprintlogin.this, R.string.permissiondenied, Toast.LENGTH_SHORT ).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void settimeinfirebase() {


        Map<String, Object> data = new HashMap<>();
        String uID = firebaseAuth.getCurrentUser().getUid();
        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        Calendar mCalendar = Calendar.getInstance();
        String Month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = mCalendar.get(Calendar.YEAR);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        data.put( "id", uID );
        data.put( "Latitude", latitude.getText().toString().trim() );
        data.put( "Longitude", longitude.getText().toString().trim() );
        data.put( "day", weekday_name.trim());
        data.put( "time", FieldValue.serverTimestamp());
        assert Month != null;
        db.collection( "users" )
                .document( uID )
                .collection("Login")
                .document(String.valueOf(year))
                .collection(Month)
                .add( data )
                .addOnSuccessListener(documentReference -> Toast.makeText( Fingerprintlogin.this, writtenwithTime, Toast.LENGTH_SHORT ).show())
                .addOnFailureListener(e -> Toast.makeText( Fingerprintlogin.this, getString(ErroraddingTime) + e, Toast.LENGTH_SHORT ).show()

                );
    }

    }







