package com.example.inout.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.inout.R;
import com.example.inout.home.signin.AttendActivity;
import com.example.inout.home.signout.LeaveActivity;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.departbtn).setOnClickListener(v -> {
            startActivity(new Intent(HomeFragment.this.getActivity(), LeaveActivity.class));
        });
        view.findViewById(R.id.attendbtn).setOnClickListener(v -> {
            startActivity(new Intent(HomeFragment.this.getActivity(), AttendActivity.class));
        });
    }
}