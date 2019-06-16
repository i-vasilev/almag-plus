package com.elamed.almag.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;


import com.elamed.almag.data.PlanApp.ParcelablePlanDetalization;
import com.elamed.almag.data.Procedure.Procedure;
import com.elamed.almag.view.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String PROCEDURE_EXTRA_NAME = "procedure";
    public static final String PLAN_DETALIZATION_EXTRA_NAME = "planDetalization";

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public void setPlanDetalization(ParcelablePlanDetalization planDetalization) {
        this.planDetalization = planDetalization;
    }

    private Procedure procedure;
    private ParcelablePlanDetalization planDetalization;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "almag:alarmReceiver");
        Intent alarmActivity = new Intent(context, AlarmActivity.class);
        wl.acquire();
        Procedure procedureExtras;
        ParcelablePlanDetalization procedureExtras2;

        Bundle extras = intent.getBundleExtra("bundle");
        procedureExtras = extras.getParcelable(PROCEDURE_EXTRA_NAME);
        procedureExtras2 = extras.getParcelable(PLAN_DETALIZATION_EXTRA_NAME);


        wl.release();
        alarmActivity.putExtra(PROCEDURE_EXTRA_NAME, procedureExtras);
        alarmActivity.putExtra(PLAN_DETALIZATION_EXTRA_NAME, procedureExtras2);
        context.startActivity(alarmActivity);
    }

    public void setAlarm(Context context) {
        setAlarm(context, procedure, planDetalization);
    }

    public void setAlarm(Context context, Procedure procedure, ParcelablePlanDetalization planDetalization) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROCEDURE_EXTRA_NAME, procedure);
        bundle.putParcelable(PLAN_DETALIZATION_EXTRA_NAME, planDetalization);
        intent.putExtra("bundle", bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, procedure.getId(), intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, procedure.getTime(), 0, pendingIntent);
    }

    public void cancelAlarm(Context context, Procedure procedure) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        //intent.setAction(String.valueOf(timetable.getId()));
        PendingIntent sender = PendingIntent.getBroadcast(context, procedure.getId(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);//Отменяем будильник, связанный с интентом данного класса
    }
}
