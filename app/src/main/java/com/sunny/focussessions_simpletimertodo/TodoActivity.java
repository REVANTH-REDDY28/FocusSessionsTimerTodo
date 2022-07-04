package com.sunny.focussessions_simpletimertodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sunny.focussessions_simpletimertodo.data.TodoSessionDao;
import com.sunny.focussessions_simpletimertodo.data.TodoSessionDatabase;
import com.sunny.focussessions_simpletimertodo.data.entities.Todo;

import java.util.List;

public class TodoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TodoSessionDatabase todoSessionDatabase;
    TodoSessionDao todoSessionDao;
    List<Todo> dataList;
    RecyclerView recyclerView;
    TodoAdapter todoAdapter;

    //
    Button addTodoButton;
    EditText addTodoEditText;

//    private static final String TAG = "TodoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        addTodoButton = findViewById(R.id.button_add_todo);
        addTodoEditText = findViewById(R.id.editText_add_todo);



        //for todo recycler view
        todoSessionDatabase = TodoSessionDatabase.getInstance(this);
        todoSessionDao = todoSessionDatabase.todoSessionDao();
        dataList = todoSessionDao.getAllTodos();
//        Log.d(TAG, "onCreate: "+dataList.size());
        recyclerView = findViewById(R.id.recyclerView_todos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        todoAdapter = new TodoAdapter(this,dataList);
        recyclerView.setAdapter(todoAdapter);

        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoText = addTodoEditText.getText().toString().trim();
                if(todoText.length()>0){
                    Todo todo = new Todo(todoText);
                    todoSessionDao.insertTodo(todo);
                    dataList.add(todo);
                    todoAdapter.notifyItemInserted(todoAdapter.getItemCount());
                    addTodoEditText.setText("");
                }
            }
        });


        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.todo);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.focus:
                        Intent intent = new Intent(TodoActivity.this,FocusActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.history:
                        Intent intent2 = new Intent(TodoActivity.this,HistoryActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        finish();
                }
                return false;
            }
        });




    }
}