package com.example.inout.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inout.R;
import com.example.inout.model.TimeStringModel;
import com.example.inout.ui.MonthActivity;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private List<TimeStringModel> list;
    private Context context;
    private String year , month;


    public DayAdapter(List<TimeStringModel> list, Context context, String year, String month) {
        this.list = list;
        this.context = context;
        this.year = year;
        this.month = month;
    }

    @NonNull
    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DayAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.format_time_text, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull DayAdapter.ViewHolder holder, int position) {
        TimeStringModel model = list.get(position);

        holder.textView.setText(model.getText());


        holder.constraintLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, MonthActivity.class)
                    .putExtra("day", model.getText());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_TimeText);
            constraintLayout = itemView.findViewById(R.id.container);
        }
    }
}
