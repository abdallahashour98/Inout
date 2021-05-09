//package com.example.inout.modelleave;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.inout.R;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//public class Timeleaveadapter extends RecyclerView.Adapter<Timeleaveadapter.ViewHolder> {
//        List<TimeModel> list;
//    private Context context;
//
//    public Timeleaveadapter(List<TimeModel> list, Context context) {
//        this.list = list;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.timeleavemodel, parent, false ) );    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.setDay(list.get(position).getDay());
//        long time = list.get( position ).getTime().getTime();
//        holder.setLeave( time );
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder {
//        TextView day;
//        TextView leave;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            day = itemView.findViewById(R.id.dayleave);
//            leave = itemView.findViewById(R.id.timeleave);
//        }
//
//        void setDay(String Day) {
//            day.setText(Day);
//        }
//
//        void setLeave(Long time) {
//            leave.setText(String.valueOf(getDate(time)));
//        }
//
//        private String getDate(long time) {
//            Date date = new Date(time);
//            SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
//            return sfd.format(date);
//        }
//
//
//    }
//}
