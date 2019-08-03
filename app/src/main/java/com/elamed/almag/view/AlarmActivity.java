package com.elamed.almag.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elamed.almag.R;
import com.elamed.almag.ToolbarSizer;
import com.elamed.almag.data.AlarmReceiver;
import com.elamed.almag.data.DBHelper;
import com.elamed.almag.data.PlanApp.ParcelablePlanDetalization;
import com.elamed.almag.data.Procedure.Procedure;
import com.elamed.almag.data.UpdaterData;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {
    private Procedure procedure;
    ParcelablePlanDetalization planDetalization;
    private Vibrator vibrator;
    private Ringtone ringtone;
    protected boolean isButtonPressed = false;
    private static String ACTION_CLOSE_ALARM = "com.elamed.almag.CLOSE_ALARM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        DBHelper.setContext(getApplicationContext());

        final Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        procedure = getIntent().getParcelableExtra(AlarmReceiver.PROCEDURE_EXTRA_NAME);
        planDetalization = getIntent().getParcelableExtra(AlarmReceiver.PLAN_DETALIZATION_EXTRA_NAME);

        final TextView diseaseName = findViewById(R.id.disease_name);
        diseaseName.setText(UpdaterData.getNameTimetableById(procedure.getTimetable()));
        final TextView diseaseDescription1 = findViewById(R.id.disease_description1);
        diseaseDescription1.setText(getResources().getText(R.string.durationOfProcedureNewTimetable) + ": " + planDetalization.getDuration());
        final TextView diseaseDescription2 = findViewById(R.id.disease_description2);
        diseaseDescription2.setText(getResources().getText(R.string.mode) + ": " + String.valueOf(planDetalization.getMode()));

        final ImageButton okButton = findViewById(R.id.ok_alarm);
        okButton.setOnClickListener(this);
        final ImageButton repeatButton = findViewById(R.id.repeat_alarm);
        repeatButton.setOnClickListener(this);
        final ImageButton moveButton = findViewById(R.id.move_alarm);
        moveButton.setOnClickListener(this);

        final Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
        if (ringtone != null) {
            ringtone.setLooping(true);
            ringtone.play();
        }
        createWaveFormVibrationUsingVibrationEffectAndAmplitude();
        final IntentFilter filter = new IntentFilter(ACTION_CLOSE_ALARM);
        final CloseAlarmReceiver closeAlarmReceiver = new CloseAlarmReceiver();
        registerReceiver(closeAlarmReceiver, filter);
        final AsyncRequest ar = new AsyncRequest();
        ar.execute("");
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ToolbarSizer.setAppBarHeight(appBarLayout, getResources());

        final LinearLayout layout = findViewById(R.id.layout_toolbar);
        final ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = layout.getResources().getDimensionPixelSize(R.dimen.widthToolbat);
        params.height = layout.getResources().getDimensionPixelSize(R.dimen.heightToolbar);
        layout.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        switch (view.getId()) {
            case R.id.ok_alarm:
                UpdaterData.updateProcedure(procedure, true, getApplicationContext());
                intent.putExtra("goToCalendar", "yes");
                break;
            case R.id.repeat_alarm:
                repeatAlarm();
                break;
            case R.id.move_alarm:
                intent = moveAlarm();
                break;
        }
        isButtonPressed = true;
        closeActivity();
        startActivityForResult(intent, 0);
    }

    private Intent moveAlarm() {
        final Intent intent = new Intent(getApplicationContext(), NewTimetableActivity.class);
        intent.putExtra("timetable", UpdaterData.getTimetableById(procedure.getTimetable()));
        return intent;
    }

    private void repeatAlarm() {
        final AlarmReceiver am = new AlarmReceiver();
        am.setProcedure(procedure);
        java.util.Calendar cal_alarm = java.util.Calendar.getInstance();
        cal_alarm.setTimeInMillis(procedure.getTime());
        cal_alarm.add(Calendar.MINUTE, 10);
        procedure.setTime(cal_alarm.getTimeInMillis());
        am.setAlarm(getApplicationContext(), procedure, planDetalization);
    }

    private void createWaveFormVibrationUsingVibrationEffectAndAmplitude() {
        long[] pattern = {0, 500, 300, 400, 200};
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(pattern, 2);
        }
    }

    protected void closeActivity() {
        vibrator.cancel();
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
        android.os.Process.killProcess(android.os.Process.myUid());
        finish();
    }

    class AsyncRequest extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(60 * 1000);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            final Intent intent = new Intent(ACTION_CLOSE_ALARM);
            sendBroadcast(intent);
        }
    }

    class CloseAlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_CLOSE_ALARM) && !isButtonPressed) {
                repeatAlarm();
                closeActivity();
            }
        }
    }

}
