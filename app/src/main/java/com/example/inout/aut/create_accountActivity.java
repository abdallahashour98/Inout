package com.example.inout.aut;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.inout.R.string.accountcreatedsuccessfully;

public class create_accountActivity extends AppCompatActivity {
    EditText username, password, email, phone, address, date;
    DatePickerDialog datePickerDialog;
    Spinner gander;
    ImageView back,next;
    TextView terms;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    @RequiresApi(api = Build.VERSION_CODES.N)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        username = findViewById(R.id.edusernamerig);
        password = findViewById(R.id.edpasswordrig);
        email = findViewById(R.id.edemailrig);
        phone = findViewById(R.id.edphonerig);
        address = findViewById(R.id.edaddresrig);
        back = findViewById(R.id.imgbackrig);
        terms = findViewById(R.id.tvxterms);
        gander = findViewById(R.id.spngenderrig);
        date =  findViewById(R.id.date1);
        next = findViewById(R.id.btnrig);

        date.setOnClickListener(v -> create_accountActivity.this.birthData());
        terms.setOnClickListener(v -> create_accountActivity.this.privacy());
        back.setOnClickListener(v -> {  startActivity(new Intent(create_accountActivity.this, LOGIN_Activity.class));});
        next.setOnClickListener(v -> { create_accountActivity.this.checkIf();});

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


    }

    private void privacy() {
        Uri uri = Uri.parse("https://in-out-0.flycricket.io/privacy.html");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void birthData() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(create_accountActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // set day of month , month and year value in the edit text
                    date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void checkIf() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Phone = phone.getText().toString().trim();
        String addres = address.getText().toString().trim();
        String data = date.getText().toString();

     if (user.isEmpty()) {
            username.requestFocus();
            username.setError(getString(R.string.pleaseentername));
            return;
     }
     if (pass.isEmpty()) {
            password.requestFocus();
            password.setError(getString(R.string.pleaseenterpassword));
            return;
     }
     if (pass.length() < 8  ) {
            password.requestFocus();
            password.setError(getString(R.string.Passwordmustbe8digit));
            return;
     }
     if (Email.isEmpty()) {
            email.requestFocus();
            email.setError(getString(R.string.pleaseenterEmail));
            return;
     }

     if (Phone.isEmpty()) {
            phone.requestFocus();
            phone.setError(getString(R.string.pleaseenterphone));
            return;
     }
     if (Phone.length() < 11 || phone.length()>11) {

            phone.requestFocus();
            phone.setError(getString(R.string.pleaseenter11digit));
            return;
     }
     if (addres.isEmpty()) {
            address.requestFocus();
            address.setError(getString(R.string.pleaseenteraddress));
            return;
     }
     if (data.isEmpty()) {
            date.requestFocus();
            date.setError(getString(R.string.pleaseenterdata));
            return;
     }
     signupfirebase(Email, pass);
    }

    private void signupfirebase(String email, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserDate();
                        } else {
                            Toast.makeText(create_accountActivity.this, "Exception : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void saveUserDate() {
        String uID =firebaseAuth.getCurrentUser().getUid();

        Map<String, Object> user = new HashMap<>();
        user.put("id", uID);
        user.put("username", username.getText().toString().trim());
        user.put("password", password.getText().toString().trim());
        user.put("email", email.getText().toString().trim());
        user.put("phone", phone.getText().toString().trim());
        user.put("address", address.getText().toString().trim());
        user.put("gander", gander.getSelectedItem());
        user.put("data", date.getText().toString().trim());
        user.put("image", "null");
        db.collection("users")
                .document(uID)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                         Toast.makeText(create_accountActivity.this, accountcreatedsuccessfully, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(create_accountActivity.this, LOGIN_Activity.class));
                    } else {
                     Toast.makeText(create_accountActivity.this, "Error + \\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}