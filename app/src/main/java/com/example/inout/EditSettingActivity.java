package com.example.inout;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditSettingActivity extends AppCompatActivity {
    TextView saveUserNameEdit, saveUserPasswordEdit, saveUserPhoneEdit, saveUserAddressEdit, saveUserGenderEdit, saveUserBirthDataEdit;
    EditText userNameEdit, userPasswordEdit, userPhoneEdit, userAddressEdit, userBirthDataEdit;
    Spinner userGenderEdit;
    DatePickerDialog datePickerDialog;
    CircleImageView circleImageView;
    ImageView changeProfielSetting,backtosetting;
    private ProgressBar progressbar;
    private Uri imageUri = null;
    private static String ImageURL = null;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private String userID;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editsetting);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        loadTheme(true);
        getImageImage();

        progressbar = findViewById(R.id.progress_bar);
        userNameEdit = findViewById(R.id.userNameEdit);
        userPasswordEdit = findViewById(R.id.userPasswordEdit);
        userPhoneEdit = findViewById(R.id.userPhoneEdit);
        userAddressEdit = findViewById(R.id.userAddressEdit);
        userGenderEdit = findViewById(R.id.userGenderEdit);
        userBirthDataEdit = findViewById(R.id.userBirthDataEdit);
        circleImageView = findViewById(R.id.profile_image1);
        changeProfielSetting = findViewById(R.id.changeProfielSetting1);
        saveUserNameEdit = findViewById(R.id.saveUserNameEdit);
        saveUserPasswordEdit = findViewById(R.id.saveUserPasswordEdit);
        saveUserPhoneEdit = findViewById(R.id.saveUserPhoneEdit);
        saveUserAddressEdit = findViewById(R.id.saveUserAddressEdit);
        saveUserGenderEdit = findViewById(R.id.saveUserGenderEdit);
        saveUserBirthDataEdit = findViewById(R.id.saveUserBirthDataEdit);
        backtosetting = findViewById(R.id.backToSetting);

        backtosetting.setOnClickListener(v -> finish());
        changeProfielSetting.setOnClickListener(v -> checkPermission());
        userBirthDataEdit.setOnClickListener(v -> birthData());
        saveUserNameEdit.setOnClickListener(v -> saveChangeUserName());
        saveUserPasswordEdit.setOnClickListener(v -> saveChangeUserPassword());
        saveUserAddressEdit.setOnClickListener(v -> saveChangeUserAddress());
        saveUserBirthDataEdit.setOnClickListener(v -> saveChangeUserBirthData());
        saveUserGenderEdit.setOnClickListener(v -> saveChangeUserGander());
        saveUserPhoneEdit.setOnClickListener(v -> saveChangeUserPhone());
        progressbar.setVisibility(View.VISIBLE);

    }
    private void loadTheme(boolean b) {
        boolean dark = this.getSharedPreferences("Dark", MODE_PRIVATE).getBoolean("Dark", b);
        if (dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void checkPermission() {

        //use permission to READ_EXTERNAL_STORAGE For Device >= Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permissionDenied", Toast.LENGTH_SHORT).show();

                // to ask user to reade external storage
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {
                OpenGalleryImagePicker();
            }

            //implement code for device < Marshmallow
        } else {

            OpenGalleryImagePicker();
        }
    }

    private void OpenGalleryImagePicker() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    private void uploadImage() {
        if (firebaseAuth.getCurrentUser() != null) {

            // chick if user image is null or not
            if (imageUri != null) {

                userID = firebaseAuth.getCurrentUser().getUid();

                // mack progress bar dialog
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("uploading");
                progressDialog.setCancelable(false);
                progressDialog.show();


                // mack collection in fireStorage
                final StorageReference ref = storageReference.child("profile_image_user").child(userID + ".png");

                // get image user and give to imageUserPath
                ref.putFile(imageUri).addOnProgressListener(taskSnapshot -> {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("upload " + (int) progress + "%");

                }).continueWithTask(task -> {
                    if (!task.isSuccessful()) {

                        throw task.getException();

                    }
                    return ref.getDownloadUrl();

                }).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        Uri downloadUri = task.getResult();

                        assert downloadUri != null;
                        ImageURL = downloadUri.toString();
                        saveChangeImage();

                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(this, " Error in addOnCompleteListener " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();

                // set image user in ImageView ;
                circleImageView.setImageURI(imageUri);

                uploadImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, "Error : " + error, Toast.LENGTH_LONG).show();

            }
        }
    }

    private void saveChangeImage() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            Map<String, Object> user = new HashMap<>();
            user.put("image", ImageURL);
            db.collection("users")
                    .document(userId)
                    .update(user)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditSettingActivity.this, "done", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditSettingActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void getImageImage() {
        if (this != null) {
            if (firebaseAuth.getCurrentUser() != null) {
                String uID = firebaseAuth.getCurrentUser().getUid();
                db.collection("users").document(uID) .get().addOnSuccessListener(documentSnapshot -> {
                    String imagedb = documentSnapshot.getString("image");
                    assert imagedb != null;
                    if (!imagedb.equals("null")) {
                        Glide.with(this)
                                .load(imagedb)
                                .placeholder(R.drawable.inout)
                                .into(circleImageView);
                    }
                    progressbar.setVisibility(View.GONE);
                }).addOnFailureListener(e -> Toast.makeText(this, "Error getting data", Toast.LENGTH_SHORT).show());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void birthData() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(EditSettingActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // set day of month , month and year value in the edit text
                    userBirthDataEdit.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void saveChangeUserName() {
        String email = userNameEdit.getText().toString().trim();
        if (email.isEmpty()) {
            userNameEdit.requestFocus();
            userNameEdit.setError(getString(R.string.pleaseentername));
            return;
        } else {

            String userId = firebaseAuth.getCurrentUser().getUid();
            Map<String, Object> userNameSave = new HashMap<>();
            userNameSave.put("username", userNameEdit.getText().toString().trim());
            db.collection("users")
                    .document(userId)
                    .update(userNameSave)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditSettingActivity.this, "done", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditSettingActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }
    private void saveChangeUserPassword() {
        if (firebaseAuth.getCurrentUser()!=null) {
            String password = userPasswordEdit.getText().toString().trim();

            if (password.isEmpty()) {
                userPasswordEdit.requestFocus();
                userPasswordEdit.setError(getString(R.string.pleaseenterpassword));

            } else if (password.length() < 8)
            {
                userPasswordEdit.requestFocus();
                userPasswordEdit.setError(getString(R.string.Passwordmustbe8digit));
            }
            else
            {
                String userId = firebaseAuth.getCurrentUser().getUid();
                Map<String, Object> userPasswordSave = new HashMap<>();
                userPasswordSave.put("password", userPasswordEdit.getText().toString().trim());
                db.collection("users")
                        .document(userId)
                        .update(userPasswordSave)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditSettingActivity.this, "done", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditSettingActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
    }
    private void saveChangeUserAddress() {
        if (firebaseAuth.getCurrentUser()!=null)
        {
            String address = userPasswordEdit.getText().toString().trim();

            if (address.isEmpty()) {
                userAddressEdit.requestFocus();
                userAddressEdit.setError(getString(R.string.pleaseenterpassword));
            } else {
                String userId = firebaseAuth.getCurrentUser().getUid();
                Map<String, Object> userAddressSave = new HashMap<>();
                userAddressSave.put("address", userAddressEdit.getText().toString().trim());
                db.collection("users")
                        .document(userId)
                        .update(userAddressSave)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditSettingActivity.this, "done", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditSettingActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
    private void saveChangeUserGander() {
        if (firebaseAuth.getCurrentUser()!=null)
        {
            String userId = firebaseAuth.getCurrentUser().getUid();
            Map<String,Object> userGanderSave = new HashMap<>();
            userGanderSave.put("gander",userGenderEdit.getSelectedItem());
            db.collection("users")
                    .document(userId)
                    .update(userGanderSave)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                        {
                            progressbar.setVisibility(View.VISIBLE);

                            Toast.makeText(EditSettingActivity.this, "done", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(EditSettingActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
    private void saveChangeUserPhone() {
        if (firebaseAuth.getCurrentUser()!=null) {
            String Phone = userPhoneEdit.getText().toString().trim();

            if (Phone.isEmpty()) {
                userPhoneEdit.requestFocus();
                userPhoneEdit.setError(getString(R.string.pleaseenterphone));
            } else if (Phone.length() < 11) {
                userPhoneEdit.requestFocus();
                userPhoneEdit.setError(getString(R.string.pleaseenter11digit));
            } else {
                String userId = firebaseAuth.getCurrentUser().getUid();
                Map<String, Object> userPhoneSave = new HashMap<>();
                userPhoneSave.put("phone", userPhoneEdit.getText().toString().trim());
                db.collection("users")
                        .document(userId)
                        .update(userPhoneSave)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditSettingActivity.this, "done", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditSettingActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
    }
    private void saveChangeUserBirthData() {
        if (firebaseAuth.getCurrentUser() != null) {
            String data = userBirthDataEdit.getText().toString().trim();

            if (data.isEmpty()) {
                userBirthDataEdit.requestFocus();
                userBirthDataEdit.setError(getString(R.string.pleaseenterdata));
            } else {
                String userId = firebaseAuth.getCurrentUser().getUid();
                Map<String, Object> userBirthDataSave = new HashMap<>();
                userBirthDataSave.put("data", userBirthDataEdit.getText().toString().trim());
                db.collection("users")
                        .document(userId)
                        .update(userBirthDataSave)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditSettingActivity.this, "done", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditSettingActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
    }

}