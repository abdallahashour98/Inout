package com.example.inout.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.inout.BuildConfig;
import com.example.inout.EditSettingActivity;
import com.example.inout.MainActivity;
import com.example.inout.R;
import com.example.inout.SplachActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class SettingFragment extends Fragment {
    TextView email, phoen, address, brithdata, gander, username, edit_profiel;
    SwitchMaterial SwitchMaterial;
    CircleImageView circleImageView;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private ProgressBar progressBar;


    public SettingFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        username = view.findViewById(R.id.username_setting);
        circleImageView = view.findViewById(R.id.profile_image);
        email = view.findViewById(R.id.email_user_setting);
        phoen = view.findViewById(R.id.phone_user_setting);
        address = view.findViewById(R.id.address_user_setting);
        brithdata = view.findViewById(R.id.brithdata_user_setting);
        gander = view.findViewById(R.id.gander_user_setting);
        progressBar = view.findViewById(R.id.progress_bar_setting);
        edit_profiel = view.findViewById(R.id.edit_profiel);
        view.findViewById(R.id.settingbacktohome).setOnClickListener(v -> startActivity(new Intent(getActivity(), MainActivity.class)));
        view.findViewById(R.id.sharepp) .setOnClickListener(v -> shareApp());
        view.findViewById(R.id.sendemsil_setting).setOnClickListener(v -> sendEmail());
        view.findViewById(R.id.privacy).setOnClickListener(v -> privacy());
        edit_profiel.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditSettingActivity.class)));
        getUserDataWithFirebase();
        view.findViewById(R.id.choselangaugeeee).setOnClickListener(v -> showChangeLanguageDialog());
        SwitchMaterial = view.findViewById(R.id.Switchcompat);
        SwitchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SettingFragment.this.saveThemes(true);
                SwitchMaterial.setChecked(true);
            } else {
                SettingFragment.this.saveThemes(false);
                SwitchMaterial.setChecked(false);
            }
        });
        loadTheme(true);

    }
    private void showChangeLanguageDialog() {
        AlertDialog builder = new AlertDialog.Builder(requireActivity()).create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.language, null);
        builder.setView(view);
        ImageView cancel_alert = view.findViewById(R.id.cancel_alert);
        cancel_alert.setOnClickListener(v -> {  builder.dismiss();});
        RadioButton radio_en = view.findViewById(R.id.radio_english);
        RadioButton radio_ar = view.findViewById(R.id.radio_arabic);
        view.findViewById(R.id.save_language).setOnClickListener(v -> {
            if (radio_en.isChecked()) {
                setLanguage("En");
            }
            else if (radio_ar.isChecked()) {
                setLanguage("Ar");
            }
        });
        builder.show();
    }

    private void setLanguage(String lang) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("language", MODE_PRIVATE).edit();
        editor.putString("my_language", lang);
        editor.apply();
        Locale l = new Locale(lang);
        Locale.setDefault(l);
        Configuration configuration = new Configuration();
        configuration.locale = l;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        Intent intent = new Intent(getActivity(), SplachActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void saveThemes(boolean b) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Dark", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("Dark", b).apply();
        if (b) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void loadTheme(boolean b) {
        boolean dark = getActivity().getSharedPreferences("Dark", MODE_PRIVATE).getBoolean("Dark", b);
        if (dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SwitchMaterial.setChecked(true);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            SwitchMaterial.setChecked(false);
        }
    }
    private void privacy() {
        Uri uri = Uri.parse("https://in-out-0.flycricket.io/privacy.html"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }
    @SuppressLint("IntentReset")
    private void sendEmail() {
        String[] TO = {"someone@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
          "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void getUserDataWithFirebase() {
        if (getActivity() != null) {
            if (firebaseAuth.getCurrentUser() != null) {
                progressBar.setVisibility(View.VISIBLE);
                String uID = firebaseAuth.getCurrentUser().getUid();
                db.collection("users").document(uID)
                        .get().addOnSuccessListener(documentSnapshot -> {
                            String emaildb = documentSnapshot.getString("email");
                            String userdb = documentSnapshot.getString("username");
                            String imagedb = documentSnapshot.getString("image");
                            String addressdb = documentSnapshot.getString("address");
                            String ganderdb = documentSnapshot.getString("gander");
                            String datadb = documentSnapshot.getString("data");
                            String phonedb = documentSnapshot.getString("phone");
                            email.setText(emaildb);
                            username.setText(getString(R.string.hi) +" "+ userdb);
                            address.setText(addressdb);
                            gander.setText(ganderdb);
                            brithdata.setText(datadb);
                            phoen.setText(phonedb);
                            assert imagedb != null;
                            if (!imagedb.equals("null")) {
                                Glide.with(SettingFragment.this.getActivity())
                                        .load(imagedb)
                                        .placeholder(R.drawable.inout)
                                        .into(circleImageView);
                            }
                            progressBar.setVisibility(View.GONE);
                        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error getting data", Toast.LENGTH_SHORT).show());
            }
        }
    }
}

