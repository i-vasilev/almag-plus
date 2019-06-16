package com.elamed.almag.view;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.elamed.almag.R;
import com.elamed.almag.ToolbarSizer;
import com.elamed.almag.data.Timetable.RemindBefore;
import com.elamed.almag.data.Timetable.Timetable;
import com.elamed.almag.data.UpdaterData;

import java.util.Calendar;
import java.util.List;

public class NewTimetableActivity extends AppCompatActivity {

    private MenuItem okMenuItem;
    private boolean isChanging;
    private Timetable timetable;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        okMenuItem = menu.findItem(R.id.action_ok);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_timetable);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ToolbarSizer.setAppBarHeight(appBarLayout, getResources());
        appBarLayout.setClickable(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayout layout = findViewById(R.id.layout_toolbar);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = layout.getResources().getDimensionPixelSize(R.dimen.widthToolbat);
        params.height = layout.getResources().getDimensionPixelSize(R.dimen.heightToolbar);
        layout.setLayoutParams(params);

        TimePicker picker = findViewById(R.id.timeNewTimetable);
        picker.setIs24HourView(true);
        Spinner remindBefore = findViewById(R.id.spinner_remindBefore);


        List<String> listPlans = UpdaterData.getStringPlans();
        ArrayAdapter<String> adapterPlans = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, listPlans);
        Spinner plans = findViewById(R.id.plan_name);
        plans.setAdapter(adapterPlans);

        List<String> listNames = UpdaterData.getStringDiseases();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, listNames);
        Spinner treatments = findViewById(R.id.disease_name);
        treatments.setAdapter(adapter);

        timetable = (Timetable) getIntent().getParcelableExtra("timetable");
        isChanging = timetable != null;
        if (isChanging) {
            EditText interval = findViewById(R.id.editTextInterval);
            TimePicker timePicker = findViewById(R.id.timeNewTimetable);
            EditText countOfProcedures = findViewById(R.id.editTextCountOfProcedures);

            treatments.setSelection(listNames.indexOf(timetable.getName()));
            plans.setSelection(listPlans.indexOf(UpdaterData.getDescriptionPlanById(timetable.getIdPlan())));
            interval.setText(String.valueOf(timetable.getInterval()));
            countOfProcedures.setText(String.valueOf(timetable.getDurationOfTreatment()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timetable.getTime());
            calendar = timetable.getRemindBefore().addTime(calendar);

            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                if (item.equals(okMenuItem)) {
                    Spinner plans = findViewById(R.id.plan_name);
                    Spinner treatment = findViewById(R.id.disease_name);
                    EditText interval = findViewById(R.id.editTextInterval);
                    TimePicker timePicker = findViewById(R.id.timeNewTimetable);
                    EditText countOfProcedures = findViewById(R.id.editTextCountOfProcedures);
                    Spinner remindBeforeSpinner = findViewById(R.id.spinner_remindBefore);

                    if (!isChanging) {
                        timetable = new Timetable();
                    }
                    timetable.setIdPlan(UpdaterData.getIdByDescriptionPlan(plans.getSelectedItem().toString()));
                    timetable.setName(String.valueOf(treatment.getSelectedItem()));
                    Calendar calendar = Calendar.getInstance();
                    RemindBefore remindBefore = RemindBefore.valueOf(remindBeforeSpinner.getSelectedItemPosition());
                    timetable.setRemindBefore(remindBefore);
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), timePicker.getHour(), timePicker.getMinute(), 0);
                    timetable.setTime(remindBefore.takeTime(calendar).getTimeInMillis());
                    timetable.setDurationOfTreatment(Integer.parseInt(countOfProcedures.getText().toString()));
                    timetable.setInterval(Integer.parseInt(interval.getText().toString()));
                    ;
                    if (isChanging) {
                        UpdaterData.updateTimetable(timetable, getApplicationContext());
                    } else {
                        UpdaterData.insertNewTimetable(timetable, getApplicationContext());
                    }
                    UpdaterData.updateAdapters();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }


}
