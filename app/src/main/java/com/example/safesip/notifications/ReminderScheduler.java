package com.example.safesip.notifications;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ReminderScheduler {

    public static void scheduleDailyReminder(Context context, int hour, int minute) {

        long currentTime = System.currentTimeMillis();

        Calendar now = Calendar.getInstance();
        Calendar next = Calendar.getInstance();

        next.set(Calendar.HOUR_OF_DAY, hour);
        next.set(Calendar.MINUTE, minute);
        next.set(Calendar.SECOND, 0);

        if (next.before(now)) {
            next.add(Calendar.DAY_OF_MONTH, 1);
        }

        long initialDelay = next.getTimeInMillis() - currentTime;

        WorkManager.getInstance(context).cancelUniqueWork("daily_reminder");
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(DailyReminderWorker.class, 24, TimeUnit.HOURS)
                        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                        "daily_reminder",
                        ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                        request
                );
    }
}

