package com.yangcao.simpletodo;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.yangcao.simpletodo.models.Todo;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final int notificationId = 100; // this will be used to cancel the notification
        Todo todo = intent.getParcelableExtra(TodoEditActivity.KEY_TODO);

        //设置通知的小图标/标题/内容
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setContentTitle(todo.text)
                .setContentText(todo.text);

        // create an explicit intent for an activity
        Intent resultIntent = new Intent(context, TodoEditActivity.class); //将启动todoEditActivity
        resultIntent.putExtra(TodoEditActivity.KEY_TODO, todo); //传入初值
        resultIntent.putExtra(TodoEditActivity.KEY_NOTIFICATION_ID, notificationId);
        //在launch todoEditActivity之后需要用id来clear notification

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                                                                      0,
                                                                      resultIntent,
                                                                      PendingIntent.FLAG_UPDATE_CURRENT);
        //设置点击通知后操作（跳转Activity，打开Service，或发送广播）
        builder.setContentIntent(resultPendingIntent);

        NotificationManager nm = (NotificationManager)context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        //notificationId allows you to update the notification later on, like canceling it
        nm.notify(notificationId, builder.build());
    }
}
