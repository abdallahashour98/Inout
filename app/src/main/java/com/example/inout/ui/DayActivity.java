package com.example.inout.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inout.R;
import com.example.inout.adapter.DayAdapter;
import com.example.inout.model.TimeStringModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DayAdapter adapter;
    protected List<TimeStringModel> list;
    private FirebaseFirestore firestore;
    private String year;
    private String month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);


        if (getIntent().getStringExtra("year").isEmpty()||getIntent().getStringExtra("month").isEmpty()){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }else {
            year=getIntent().getStringExtra("year");
            month=getIntent().getStringExtra("month");
        }

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar_timelogin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new DayAdapter(list,DayActivity.this,year,month);
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {


        firestore.collection("Year").document(year).collection("Month").document(month).collection("Day").addSnapshotListener((value, error) -> {
            if (error == null)
            {
                if (value==null)
                {
                    Toast.makeText(this,"value is null", Toast.LENGTH_SHORT).show();
                }else {
                    for (DocumentChange documentChange :value.getDocumentChanges()){
                        TimeStringModel model = documentChange.getDocument().toObject(TimeStringModel.class);
                        list.add(model);
                        adapter.notifyDataSetChanged();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }else
            {
                Toast.makeText(this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }
}