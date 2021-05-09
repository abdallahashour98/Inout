package com.example.inout.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.inout.R;

public class timetoleaveFragment extends Fragment {

//    private RecyclerView recycle;
//    private ProgressBar progressBar;
//    private List<TimeModel>list;
//    private Timeleaveadapter adapter;
//    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timetoleave, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        firestore=FirebaseFirestore.getInstance();
//        view.findViewById(R.id.timetolivebacktohome).setOnClickListener(v -> startActivity(new Intent(getActivity(), MainActivity.class)));
//        recycle=view.findViewById(R.id.recycle_view);
//        progressBar=view.findViewById(R.id.progress_bar_timeLeave);
//        recycle.setHasFixedSize(true);
//        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
//        list =new ArrayList<>();
//        adapter=new Timeleaveadapter(list, getActivity());
//        recycle.setAdapter(adapter);
        getData();

    }
    private void getData(){
//        progressBar.setVisibility(View.VISIBLE);
//        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        Calendar mCalendar = Calendar.getInstance();
//        String Month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
//        int year = mCalendar.get(Calendar.YEAR);
//        assert Month != null;
//        firestore.collection("users")
//                .document(currentUser)
//                .collection("out")
//                .document(String.valueOf(year))
//                .collection(Month)
//                .orderBy("time", Query.Direction.ASCENDING)
//                .addSnapshotListener((value, error) -> {
//                    if (error == null)
//                    {
//                        if (value==null)
//                        {
//                            Toast.makeText(getActivity(),"value is null", Toast.LENGTH_SHORT).show();
//                        }else {
//                            for (DocumentChange documentChange :value.getDocumentChanges()){
//                                TimeModel mod = documentChange.getDocument().toObject(TimeModel.class);
//                                list.add(mod);
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                        progressBar.setVisibility(View.GONE);
//                    }else
//                    {
//                        Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                });
    }
}
