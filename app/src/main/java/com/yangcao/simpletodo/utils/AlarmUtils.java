package com.yangcao.simpletodo.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.yangcao.simpletodo.AlarmReceiver;
import com.yangcao.simpletodo.TodoEditActivity;
import com.yangcao.simpletodo.models.Todo;

import java.util.Calendar;
import java.util.Date;

public class AlarmUtils {
    public static void setAlarm(@NonNull Context context, @NonNull Todo todo) {
        Calendar c = Calendar.getInstance(); //get current time
        if (todo.remindDate.compareTo(c.getTime()) < 0) { //if date is smaller than current time
            return; // only fire alarm when date is in the future
        }

        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        //把一个todo对象传进来便于显示通知时使用其内容以及点击通知后显示在编辑页面上
        intent.putExtra(TodoEditActivity.KEY_TODO, todo);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
                                                               0,
                                                               intent,
                                                               PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, //will wake up the device
                     todo.remindDate.getTime(), //time
                     alarmIntent);  //event
    }
}
