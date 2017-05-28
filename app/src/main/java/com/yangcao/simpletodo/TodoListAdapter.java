package com.yangcao.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yangcao.simpletodo.models.Todo;
import com.yangcao.simpletodo.utils.UIUtils;

import java.util.List;

public class TodoListAdapter extends BaseAdapter {

    private MainActivity activity;
    private List<Todo> data;

    public TodoListAdapter(MainActivity activity, List<Todo> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.main_list_item, parent, false);
//            Log.d("yangcao_todo", "new view for position: " + position);

            vh = new ViewHolder();
            //we only find once
            vh.todoText = (TextView)convertView.findViewById(R.id.main_list_item_text);
            vh.doneCheckbox = (CheckBox)convertView.findViewById(R.id.main_list_item_check);
            convertView.setTag(vh); // cache the view holder
        } else {
            //convertView is not null, which means it contains a cached view holder
            vh = (ViewHolder)convertView.getTag();
        }

        final Todo todo = (Todo)getItem(position);
        vh.todoText.setText(todo.text);
        vh.doneCheckbox.setChecked(todo.done);
        UIUtils.setTextViewStrikeThrough(vh.todoText, todo.done);
//        Log.d("yangcao_todo", "position: " + position);

        //在主页勾选并同时更新sp
        vh.doneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.updateTodo(position, isChecked);
            }
        });

        //点击某条item的view就进入编辑页面
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TodoEditActivity.class);
                intent.putExtra(TodoEditActivity.KEY_TODO, todo);
                activity.startActivityForResult(intent, MainActivity.REQ_CODE_TODO_EDIT);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView todoText;
        CheckBox doneCheckbox;
    }
}
