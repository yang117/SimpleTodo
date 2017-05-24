package com.yangcao.simpletodo;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yangcao.simpletodo.models.Todo;
import com.yangcao.simpletodo.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI(mockData());
    }

    private void setupUI(@NonNull List<Todo> todos) {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.crappy_list);
        linearLayout.removeAllViews();

        for(Todo todo : todos) {
            View view = getListItemView(todo);
            linearLayout.addView(view);
        }

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "fab clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    @NonNull
    private View getListItemView(@NonNull Todo todo) {
        View view = getLayoutInflater().inflate(R.layout.main_list_item, null);
        ((TextView)view.findViewById(R.id.main_list_item_text)).setText(todo.text);
        return view;
    }

    @NonNull
    private List<Todo> mockData() {
        List<Todo> list = new ArrayList<>();
        for (int i = 0; i < 1000; ++i) {
            list.add(new Todo("todo " + i, DateUtils.stringToDate("2017 3 21 2:22")));
        }
        return list;
    }
}
