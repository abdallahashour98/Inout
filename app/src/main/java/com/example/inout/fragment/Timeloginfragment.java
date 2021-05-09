package com.example.inout.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inout.R;
import com.example.inout.adapter.YearAdapter;
import com.example.inout.model.TimeStringModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Timeloginfragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private YearAdapter adapter;
    protected List<TimeStringModel> list;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timelogin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar_timelogin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        adapter = new YearAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {

        firestore.collection("Year").addSnapshotListener((value, error) -> {
            if (error == null)
            {
                if (value==null)
                {
                    Toast.makeText(getActivity(),"value is null", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

//    private void getData() {
//        progressBar.setVisibility(View.VISIBLE);
//        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        Calendar mCalendar = Calendar.getInstance();
//        String Year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
//        String Month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
//        String Day = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
//
//
//        firestore.collection("Year")
//                .document(Year)
//                .collection("Month")
//                .document(Month)
//                .collection("Day")
//                .document(Day)
//                .addSnapshotListener((value, error) -> {
//                    if (error == null) {
//                        if (value == null) {
//                            Toast.makeText(getActivity(), "value is null", Toast.LENGTH_SHORT).show();
//                        } else {
//                            for (DocumentChange documentChange : value.getDocumentChanges()) {
//
//                                TimeModel model = documentChange.getDocument().toObject(TimeModel.class);
//
//
//                                firestore.collection("users")
//                                        .document(model.getUser_id()).addSnapshotListener((value1, error1) -> )
//
//
//                            }
//                        }
//                        progressBar.setVisibility(View.GONE);
//                    } else {
//                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }
}