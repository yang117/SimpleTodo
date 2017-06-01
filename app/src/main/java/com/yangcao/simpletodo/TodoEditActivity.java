package com.yangcao.simpletodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.test.suitebuilder.TestMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yangcao.simpletodo.models.Todo;
import com.yangcao.simpletodo.utils.AlarmUtils;
import com.yangcao.simpletodo.utils.DateUtils;
import com.yangcao.simpletodo.utils.UIUtils;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("ConstantConditions")
public class TodoEditActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{

    public static final String KEY_TODO = "todo";
    public static final String KEY_TODO_ID = "todo_id";
    public static final String KEY_NOTIFICATION_ID = "notification_id";

    private EditText todoEdit;
    private TextView dateTv;
    private TextView timeTv;
    private CheckBox completeCb;

    private Todo todo;
    private Date remindDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todo = getIntent().getParcelableExtra(KEY_TODO);
        remindDate = todo != null
                ? todo.remindDate
                : null;  // if this activity is for creating a new todo_item, set remindDate to null

        setupUI();
        cancelNotificationIfNeeded();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelNotificationIfNeeded() {
        int notificationId = getIntent().getIntExtra(KEY_NOTIFICATION_ID, -1);
        if (notificationId != -1) {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notificationId);
        }
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        setTitle(null);
    }

    private void setupUI() {
        setContentView(R.layout.activity_edit_todo);
        setupActionBar();

        todoEdit = (EditText)findViewById(R.id.todo_detail_todo_edit);
        dateTv = (TextView)findViewById(R.id.todo_detail_date);
        timeTv = (TextView)findViewById(R.id.todo_detail_time);
        completeCb = (CheckBox)findViewById(R.id.todo_detail_complete);

        //initialize saved data，设置text和checkbox
        if (todo != null) {
            todoEdit.setText(todo.text);
            UIUtils.setTextViewStrikeThrough(todoEdit, todo.done); //设置删除线
            completeCb.setChecked(todo.done);

            findViewById(R.id.todo_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete();
                }
            });
        } else {
            findViewById(R.id.todo_delete).setVisibility(View.GONE);
        }

        if (remindDate != null) { //设置提醒时间
            dateTv.setText(DateUtils.dateToStringDate(remindDate));
            timeTv.setText(DateUtils.dateToStringTime(remindDate));
        } else {
            dateTv.setText(R.string.set_date);
            timeTv.setText(R.string.set_time);
        }

        setupDatePicker();
        setupCheckbox();
        setupSaveButton();
    }

    private void setupDatePicker() {  //时间设置对话框
        dateTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar c = getCalendarFromRemindDate();
                Dialog dialog = new DatePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this, //callback
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar c = getCalendarFromRemindDate();
                Dialog dialog = new TimePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true);
                dialog.show();
            }
        });
    }

    private void setupCheckbox() {
        completeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UIUtils.setTextViewStrikeThrough(todoEdit, isChecked);
                todoEdit.setTextColor(isChecked? Color.GRAY : Color.WHITE);
            }
        });

        // use this wrapper to make it possible for users to click on the entire row to change the checkbox
        View completeWrapper = findViewById(R.id.todo_detail_complete_wrapper);
        completeWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeCb.setChecked(!completeCb.isChecked()); //更改checkbox
            }
        });
    }

    private void setupSaveButton() {
        //this fab act as a save button, no need of a menu item
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.todo_detail_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndExit();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // this method will be called after user has chosen date from the DatePickerDialog
        Calendar c = getCalendarFromRemindDate();
        c.set(year, month, dayOfMonth);

        remindDate = c.getTime();
        dateTv.setText(DateUtils.dateToStringDate(remindDate));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // this method will be called after user has chosen time from the TimePickerDialog
        Calendar c = getCalendarFromRemindDate();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        remindDate = c.getTime();
        timeTv.setText(DateUtils.dateToStringTime(remindDate));
    }

    private void saveAndExit() {
        //save current data to object
        if (todo == null) { //create
            todo = new Todo(todoEdit.getText().toString(), remindDate);
        } else { //edit
            todo.text = todoEdit.getText().toString();
            todo.remindDate = remindDate;
        }

        todo.done = completeCb.isChecked();

        if (remindDate != null) { //date可能没有设置
            // set alarm when saving the todo_item，保存的时候才需要去调用设置alarm
            AlarmUtils.setAlarm(this, todo);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_TODO, todo);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void delete() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_TODO_ID, todo.id);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private Calendar getCalendarFromRemindDate() {
        Calendar c = Calendar.getInstance();
        if (remindDate != null) {
            c.setTime(remindDate);
        }
        return c;
    }
}


