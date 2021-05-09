package com.example.inout.home.signout;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inout.MainActivity;
import com.example.inout.R;
import com.example.inout.home.Capture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.inout.R.string.ErroraddingTime;
import static com.example.inout.R.string.writtenwithTime;

public class QRActivityleave extends AppCompatActivity {

    TextView resultData;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r);
        scanner();
        resultData=findViewById(R.id.resultdata);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentIntegrator = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentIntegrator.getContents()!= null)
        {
            resultData.setText(intentIntegrator.getContents());
            AlertDialog.Builder scan = new AlertDialog.Builder(QRActivityleave.this);
            scan.setTitle(R.string.scanQRcode);
            scan.setMessage(intentIntegrator.getContents());
            scan.setPositiveButton(R.string.thanks, (dialog, which) -> {
                settimeinfirebase();
                startActivity(new Intent(QRActivityleave.this, MainActivity.class));
            });
            scan.setNegativeButton(R.string.again, (dialog, which) -> {
                scanner();
            });
            scan.create().show();

        }else
        {
            Toast.makeText(getApplicationContext(),  R.string.sorryplaseagain, Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void settimeinfirebase() {
        Map<String, Object> data = new HashMap<>();
        String uID = firebaseAuth.getCurrentUser().getUid();
        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        data.put( "id",  resultData.getText().toString().trim());
        data.put( "day", weekday_name.trim());
        data.put( "time", FieldValue.serverTimestamp() );
        Calendar mCalendar = Calendar.getInstance();
        String Month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = mCalendar.get(Calendar.YEAR);

        assert Month != null;
        db.collection( "users" )
                .document( uID )
                .collection("out")
                .document(String.valueOf(year))
                .collection(Month)
                .add( data )
                .addOnSuccessListener(documentReference -> Toast.makeText( QRActivityleave.this,  writtenwithTime, Toast.LENGTH_SHORT ).show())
                .addOnFailureListener(e -> Toast.makeText( QRActivityleave.this, getString(ErroraddingTime) + e, Toast.LENGTH_SHORT ).show());
    }
     private void scanner()
   {
    IntentIntegrator intentIntegrator = new IntentIntegrator(QRActivityleave.this);
    intentIntegrator.setPrompt(getString(R.string.forflash) + "\n"+ getString(R.string.didntuseflash));
    intentIntegrator.setBeepEnabled(true);
    intentIntegrator.setOrientationLocked(true);
    intentIntegrator.setCaptureActivity(Capture.class);
    intentIntegrator.initiateScan();
  }

}
