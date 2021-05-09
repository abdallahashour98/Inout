package com.example.inout.aut;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inout.MainActivity;
import com.example.inout.R;
import com.example.inout.SplachActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
public class LOGIN_Activity extends AppCompatActivity {

    EditText Emaillogin,passwordlogin;
    TextView createaccount,forgetpassword,arabiclanuage,englishLanguage;
    ImageView login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);


        firebaseAuth = FirebaseAuth.getInstance();
        Emaillogin = findViewById(R.id.edusernamelogin);
        passwordlogin = findViewById(R.id.edpasswordlogin);
        login = findViewById(R.id.btnlogin);
        createaccount = findViewById(R.id.tvxcreateaccountllogin);
        forgetpassword = findViewById(R.id.forgetpasswordlogin);
        arabiclanuage = findViewById(R.id.arabiclanuage);
        englishLanguage = findViewById(R.id.englishLanguage);

        englishLanguage.setOnClickListener(v -> {
            changeLanguageToEnglish();
            Intent intent = new Intent(LOGIN_Activity.this, SplachActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        arabiclanuage.setOnClickListener(v -> {
            changeLanguageToArabic();
            Intent intent = new Intent(LOGIN_Activity.this, SplachActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        forgetpassword.setOnClickListener(v -> LOGIN_Activity.this.
                startActivity(new Intent(LOGIN_Activity.this, forgetpassActivity.class)));
        login.setOnClickListener(v -> checkIf());
        createaccount.setOnClickListener(v -> {
            startActivity(new Intent(LOGIN_Activity.this, create_accountActivity.class));
        });
        loadLanguage();
    }
    private void loadLanguage(){
        String show =getSharedPreferences("language", MODE_PRIVATE).getString("my_language","");
        if (show.equals("Ar"))
            changeLanguageToArabic();
        else
            changeLanguageToEnglish();
    }

    private void changeLanguageToEnglish()
    {
        setLanguage("En");
    }

    private void changeLanguageToArabic() {
        setLanguage("Ar");
    }
    private void setLanguage(String lang) {
        getSharedPreferences("language", MODE_PRIVATE)
                .edit()
                .putString("my_language",lang)
                .apply();
        Locale l = new Locale(lang);
        Locale.setDefault(l);
        Configuration configuration = new Configuration();
        configuration.locale = l;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
    private void checkIf() {
        String email = Emaillogin.getText().toString().trim();
        String pass = passwordlogin.getText().toString().trim();
        if (email.isEmpty()) {
            Emaillogin.requestFocus();
            Emaillogin.setError(getString(R.string.pleaseenterEmail));
            return;
        }
        if (pass.isEmpty())
        {
            passwordlogin.requestFocus();
            passwordlogin.setError(getString(R.string.pleaseenterpassword));
            return;
        }
        if (pass.length()<8)
        {
            passwordlogin.requestFocus();
            passwordlogin.setError(getString(R.string.Passwordmustbe8digit));
            return;
        }
        loginWithFirebase(email,pass);
    }
    private void loginWithFirebase(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        getSharedPreferences("login", MODE_PRIVATE)
                                .edit()
                                .putBoolean("isLogin", true)
                                .apply();
                                gotoLogin();
                    }else {
                        Toast.makeText(LOGIN_Activity.this, "error is :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        boolean isLogin = getSharedPreferences("login", MODE_PRIVATE).getBoolean("isLogin", false);
        if (isLogin) {
            gotoLogin();
        }
    }
    void gotoLogin() {
        startActivity(new Intent(LOGIN_Activity.this, MainActivity.class));
        finish();
    }

}
