package com.sunny.focussessions_simpletimertodo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.focussessions_simpletimertodo.data.TodoSessionDatabase;
import com.sunny.focussessions_simpletimertodo.data.entities.Session;
import com.sunny.focussessions_simpletimertodo.data.entities.Todo;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    //1
    //initialize variables
    private List<Session> dataList;
    private Activity context;
    private TodoSessionDatabase db;

    //2
    //constructor
    public SessionAdapter(Activity context,List<Session> dataList){
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.history_item,parent,false);
        return new SessionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionAdapter.ViewHolder holder, int position) {
        Session session = dataList.get(position);
        String format = new SimpleDateFormat(" dd/MM/yyyy ").format(new Date(session.getDate()));
        holder.date.setText(String.format("Date: %s", format));
        holder.timeSpent.setText(MessageFormat.format("Time spent: {0} mins", session.getTime_spent()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView timeSpent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textView_historyItem_date);
            timeSpent = itemView.findViewById(R.id.textView_historyItem_timeSpent);
        }
    }
}
