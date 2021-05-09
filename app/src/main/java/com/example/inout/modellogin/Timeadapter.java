package com.example.inout.modellogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inout.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Timeadapter extends RecyclerView.Adapter<Timeadapter.ViewHolder> {
    private Context context;
    private List<timelogin> list;

    public Timeadapter(Context context, List<timelogin> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.timelogin, parent, false ) );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDay( list.get( position ).getDay() );
        long time = list.get( position ).getTime().getTime();
        holder.setTime( time );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            day = itemView.findViewById( R.id.day );
            time = itemView.findViewById( R.id.time );


        }

        void setDay(String dayy) {
            day.setText( dayy );
        }

        void setTime(long _time)
        {
            time.setText( String.valueOf( getDate( _time ) ) );
        }

        private String getDate(long time) {
            Date date = new Date(time);
            SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return sfd.format(date);
        }
    }
}
