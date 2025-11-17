package com.example.safesip;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;

class MidnightScheduler {
    public static void scheduleNextMidnight(Context context) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        if (c.getTimeInMillis() <= System.currentTimeMillis()) {
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        Intent intent = new Intent(context, MidnightReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    c.getTimeInMillis(),
                    pi
            );
        }
    }
}
public class MidnightReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences dataBase = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataBase.edit();
        editor.putString("alreadyDrinkedToday", "0");
        int strike = Integer.parseInt(dataBase.getString("strike", "0"));
        String alcoolByDay = dataBase.getString("alcoolByDay", "a");
        String[] alcoolByDayArray = alcoolByDay.split(",");
        if(alcoolByDayArray.length >= 2 && (Float.parseFloat(alcoolByDayArray[alcoolByDayArray.length-1]) < Float.parseFloat(alcoolByDayArray[alcoolByDayArray.length-2]))){
            strike++;
        }
        else{
            strike = 0;
        }
        editor.putString("strike", String.valueOf(strike));
        editor.putString("times", "");
        editor.putString("alcool", "");
        editor.apply();
        MidnightScheduler.scheduleNextMidnight(context);
    }
}

