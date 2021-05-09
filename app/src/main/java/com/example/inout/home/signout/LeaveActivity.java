package com.example.inout.home.signout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inout.R;

public class LeaveActivity extends AppCompatActivity {
    ImageView qr,finger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        qr=findViewById(R.id.qrleave);
        finger=findViewById(R.id.fingerleave);
        qr.setOnClickListener(v -> startActivity(new Intent( LeaveActivity.this, QRActivityleave.class)));
        finger.setOnClickListener(v -> startActivity(new Intent(LeaveActivity.this, Fingerprintleave.class)));
    }
}