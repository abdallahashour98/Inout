package com.example.inout.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.inout.MainActivity;
import com.example.inout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.inout.R.string.sendExpationdone;


public class ExpationFragment extends Fragment {
    EditText date,namee,cause,managername,notes;
    Spinner department,permissiontype;
    Button send;
    DatePickerDialog datePickerDialog;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    public ExpationFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expation, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        date = view.findViewById(R.id.dateexp); date.setOnClickListener(v -> selectData());
        namee = view.findViewById(R.id.ednameexp);
        cause = view.findViewById(R.id.edcauseexp);
        managername = view.findViewById(R.id.edmanagernameexp);
        notes = view.findViewById(R.id.enotesexp);
        department = view.findViewById(R.id.spndepartexp);
        permissiontype = view.findViewById(R.id.spnpermissionexp);
        send = view.findViewById(R.id.btnsendexp);
        view.findViewById(R.id.expationbackHome).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
        });
        send.setOnClickListener(v -> checkIf());
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void selectData() {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            datePickerDialog = new DatePickerDialog(getActivity(),
                    (view, year, monthOfYear, dayOfMonth) -> {
                        date.setText(dayOfMonth + "/"+ (monthOfYear + 1) + "/" + year);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    private void checkIf() {
        String name = namee.getText().toString().trim();
        String causee = cause.getText().toString().trim();
        String managernamee = managername.getText().toString().trim();
        String data = date.getText().toString();
        if (name.isEmpty()) {
            namee.requestFocus();
            namee.setError(getString(R.string.pleaseentername));
            return;
        }
        if (causee.isEmpty()) {
            cause.requestFocus();
            cause.setError(getString(R.string.pleaseentercause));
            return;
        }
        if (managernamee.isEmpty()) {
            managername.requestFocus();
            managername.setError(getString(R.string.pleaseentermanagername));
            return;
        }
        if (data.isEmpty()) {
            date.requestFocus();
            date.setError(getString(R.string.pleaseenterdata));
            return;
        }
        saveUserDate();
    }
    private void saveUserDate() {
        String uID = firebaseAuth.getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("id", uID);
        user.put("name", namee.getText().toString().trim());
        user.put("cause", cause.getText().toString().trim());
        user.put("managername", managername.getText().toString().trim());
        user.put("notes", notes.getText().toString().trim());
        user.put("department", department.getSelectedItem());
        user.put("permissiontype", permissiontype.getSelectedItem());
        db.collection("users")
                .document(uID)
                .collection("Expation")
                .add( user )
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), sendExpationdone, Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(getActivity(), MainActivity.class));
                })
                .addOnFailureListener(e -> Toast.makeText(ExpationFragment.this.getActivity(), "Error + \\n" + e, Toast.LENGTH_SHORT).show());
    }
}