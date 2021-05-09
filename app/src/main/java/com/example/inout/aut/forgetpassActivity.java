package com.example.inout.aut;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inout.R;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpassActivity extends AppCompatActivity {
    ImageView icback;
    EditText emailregest;
    Button sendregest;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        icback=findViewById(R.id.imgbackrigtorig);
        icback.setOnClickListener(v -> startActivity(new Intent(forgetpassActivity.this,LOGIN_Activity.class)));
        emailregest=findViewById(R.id.emailforgetpass);
        sendregest=findViewById(R.id.buutonsend);
        firebaseAuth=FirebaseAuth.getInstance();
        sendregest.setOnClickListener(v -> firebaseAuth.sendPasswordResetEmail(emailregest.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        AlertDialog.Builder forget = new AlertDialog.Builder(forgetpassActivity.this);
                        forget.setTitle(R.string.forgetpassword);
                        forget.setMessage(R.string.changepasswordfromemail);
                        forget.setPositiveButton(R.string.thanks,  (dialog, which) -> { forgetpassActivity.this.startActivity(new Intent(forgetpassActivity.this, LOGIN_Activity.class));
                                });
                        forget.create().show();
                    } else {
                        Toast.makeText(forgetpassActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}