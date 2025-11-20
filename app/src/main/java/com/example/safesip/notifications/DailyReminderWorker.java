package com.example.safesip.notifications;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.safesip.MotivQuoteActivity;
import com.example.safesip.R;

public class DailyReminderWorker extends Worker {
    public DailyReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String PERSONAL_DATA_FILE = "personal-data";
        String PERSONAL_DATA_SET_KEY = "personal-data-set";
        SharedPreferences prefs = getApplicationContext()
                .getSharedPreferences(PERSONAL_DATA_FILE, Context.MODE_PRIVATE);

        // Do nothing if there's no sign up information
        boolean enabled = prefs.getBoolean(PERSONAL_DATA_SET_KEY, true);
        if (!enabled)
            return Result.success();

        showNotification();
        return Result.success();
    }

    private void showNotification() {
        Context context = getApplicationContext();

        String channelId = "daily_reminder_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        Intent intent = new Intent(context, MotivQuoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent openAppIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Action button
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.mipmap.safesip_foreground,
                "Add Drink",
                openAppIntent
        ).build();

        NotificationChannel channel = new NotificationChannel(
                channelId,
                "Daily Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        CharSequence contentText = "Don't forget to track how much you drank today!" + "🍺" + "🍷";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.safesip)
                .setContentTitle("Daily Reminder")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(action)
                .setContentIntent(openAppIntent);

        NotificationManagerCompat.from(context).notify(1001, builder.build());
    }
}
