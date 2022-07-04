package com.sunny.focussessions_simpletimertodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.focussessions_simpletimertodo.data.TodoSessionDatabase;
import com.sunny.focussessions_simpletimertodo.data.entities.Todo;

import java.text.MessageFormat;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
//    private static final String TAG = "TodoAdapter";
    //1
    //initialize variables
    private List<Todo> dataList;
    private Activity context;
    private TodoSessionDatabase db;

    //2
    //constructor
    public TodoAdapter(Activity context,List<Todo> dataList){
        this.context = context;
        this.dataList = dataList;
        db = TodoSessionDatabase.getInstance(context);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.todo_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        int itemPosition = holder.getAdapterPosition();
        Todo todo = dataList.get(position);
        holder.todoText.setText(todo.getTodo_text());
//        Log.d(TAG, "onBindViewHolder: "+todo.getTodo_text()+" "+todo.getTime_spent());
        holder.todoTimeSpent.setText(MessageFormat.format("Time spent: {0} mins", todo.getTime_spent()));




    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView todoText;
        TextView todoTimeSpent;
        ImageView editButton;
        ImageView deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todoText = itemView.findViewById(R.id.textView_item_todo_text);
            todoTimeSpent = itemView.findViewById(R.id.textView_item_todo_timeSpent);
            editButton = itemView.findViewById(R.id.imageView_todoItem_update);
            deleteButton = itemView.findViewById(R.id.imageView_todoItem_delete);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()){
                case R.id.imageView_todoItem_update:
                    updateTodo(position);
                    break;
                case R.id.imageView_todoItem_delete:
                    position = getAdapterPosition();
                    int todoId = dataList.get(position).getTodo_id();
                    db.todoSessionDao().deleteTodoByTodoId(todoId);
                    dataList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

            }
        }

        private void updateTodo(int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.pop_up_update_todo,null);
            Button updateBtn = view.findViewById(R.id.button_update_todo);
            EditText enterTodoEditText = view.findViewById(R.id.edit_text_update_todo);

            Todo todo = dataList.get(position);
            enterTodoEditText.setText(todo.getTodo_text());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();

            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(enterTodoEditText.getText()!=null){
                        String todoText = String.valueOf(enterTodoEditText.getText());
                        todo.setTodo_text(todoText);

                        db.todoSessionDao().updateTodo(todo);
                        notifyItemChanged(position);
                        dialog.dismiss();

                    }
                }
            });
        }
    }


}
