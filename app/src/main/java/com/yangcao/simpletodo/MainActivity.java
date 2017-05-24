package com.yangcao.simpletodo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.yangcao.simpletodo.models.Todo;
import com.yangcao.simpletodo.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void setupUI(@NonNull List<Todo> todos) {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.crappy_list);
    }

    private List<Todo> mockData() {
        List<Todo> list = new ArrayList<>();
        for (int i = 0; i < 1000; ++i) {
            list.add(new Todo("todo" + i, DateUtils.stringToDate("2017 3 21 2:22")));
        }
        return list;
    }
}
