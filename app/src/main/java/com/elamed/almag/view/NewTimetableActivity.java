package com.elamed.almag.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.elamed.almag.R;
import com.elamed.almag.ToolbarSizer;
import com.elamed.almag.data.Disease;
import com.elamed.almag.data.DropdownItemAdapter;
import com.elamed.almag.data.Timetable.RemindBefore;
import com.elamed.almag.data.Timetable.Timetable;
import com.elamed.almag.data.UpdaterData;
import com.elamed.almag.view.views.CustomTimePickerDialog;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

public class NewTimetableActivity extends AppCompatActivity {

    private MenuItem okMenuItem;
    private boolean isChanging;
    private Timetable timetable;
    private TimePicker picker;
    private NumberPicker minutePicker;
    private Boolean goToCalendar = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        String extra = getIntent().getStringExtra("goToCalendar");
        if (extra != null) {
            goToCalendar = extra.equals("yes");
        }
        LinearLayout layout = findViewById(R.id.layout_toolbar);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = layout.getResources().getDimensionPixelSize(R.dimen.widthToolbat);
        params.height = layout.getResources().getDimensionPixelSize(R.dimen.heightToolbar);
        layout.setLayoutParams(params);

        picker = findViewById(R.id.timeNewTimetable);
        picker.setIs24HourView(true);
        setMinutePicker();
        Spinner remindBefore = findViewById(R.id.spinner_remindBefore);

        final TextView c = findViewById(R.id.textViewCountOfProcedures);
        c.measure(0, 0);
        TableLayout table = findViewById(R.id.table);
        table.measure(0, 0);
        int i = table.getMeasuredWidth() - c.getMeasuredWidth() - 20;


        List<String> listPlans = UpdaterData.getStringPlans();
        DropdownItemAdapter adapterPlans = new DropdownItemAdapter(this, R.layout.multiline_spinner_item, listPlans, i);
        adapterPlans.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        Spinner plans = findViewById(R.id.plan_name);
        plans.setAdapter(adapterPlans);


        List<String> listNames = UpdaterData.getStringDiseases();
        DropdownItemAdapter adapter = new DropdownItemAdapter(this, R.layout.multiline_spinner_item, listNames, i);
        adapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);

        Disease disease = (Disease) getIntent().getSerializableExtra("disease");
        final Spinner treatments = findViewById(R.id.disease_name);
        treatments.setAdapter(adapter);
        if (disease != null) {
            treatments.setSelection(listNames.indexOf(disease.getName()));
        } else {
            timetable = getIntent().getParcelableExtra("timetable");
            isChanging = timetable != null;
            if (isChanging) {
                EditText interval = findViewById(R.id.editTextInterval);
                EditText countOfProcedures = findViewById(R.id.editTextCountOfProcedures);

                treatments.setSelection(listNames.indexOf(timetable.getName()));
                plans.setSelection(listPlans.indexOf(UpdaterData.getDescriptionPlanById(timetable.getIdPlan())));
                interval.setText(String.valueOf(timetable.getInterval()));
                countOfProcedures.setText(String.valueOf(timetable.getDurationOfTreatment()));
                remindBefore.setSelection(timetable.getRemindBefore().getValue());

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timetable.getTime());
                calendar = timetable.getRemindBefore().addTime(calendar);

                picker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                setMinute(calendar.get(Calendar.MINUTE));
            }

        }
        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.ok).setOnClickListener(v -> {
            Spinner plans1 = findViewById(R.id.plan_name);
            Spinner treatment = findViewById(R.id.disease_name);
            TimePicker timePicker = findViewById(R.id.timeNewTimetable);
            EditText countOfProcedures = findViewById(R.id.editTextCountOfProcedures);
            Spinner remindBeforeSpinner = findViewById(R.id.spinner_remindBefore);

            if (!isChanging) {
                timetable = new Timetable();
            }
            timetable.setIdPlan(UpdaterData.getIdByDescriptionPlan(plans1.getSelectedItem().toString()));
            timetable.setName(String.valueOf(treatment.getSelectedItem()));
            Calendar calendar = Calendar.getInstance();
            RemindBefore remindBefore1 = RemindBefore.valueOf(remindBeforeSpinner.getSelectedItemPosition());
            timetable.setRemindBefore(remindBefore1);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), timePicker.getHour(), getMinute(), 0);
            timetable.setTime(remindBefore1.takeTime(calendar).getTimeInMillis());
            timetable.setDurationOfTreatment(Integer.parseInt(countOfProcedures.getText().toString()));
            timetable.setInterval(1);

            if (isChanging) {
                UpdaterData.updateTimetable(timetable, getApplicationContext());
            } else {
                UpdaterData.insertNewTimetable(timetable, getApplicationContext());
            }
            UpdaterData.updateAdapters();
            finish();
            if (goToCalendar) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        plans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EditText countProcedures = findViewById(R.id.editTextCountOfProcedures);
                countProcedures.setText(String.valueOf(UpdaterData.getPlans().get(position).getPlanDetalizationList().size()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        treatments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String imageDisease = UpdaterData.getImageDisease(position);
                ImageView imageView = findViewById(R.id.imageTreatment);
                try {
                    GifDrawable gifFromAssets = new GifDrawable(getAssets(), "images/" + imageDisease);
                    gifFromAssets.setLoopCount(0);
                    imageView.setBackground(gifFromAssets);
                } catch (IOException e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private static final DecimalFormat FORMATTER = new DecimalFormat("00");
    private static final int INTERVAL = 5;

    public void setMinutePicker() {
        int numValues = 60 / INTERVAL;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL);
        }

        View minute = picker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minute != null) && (minute instanceof NumberPicker)) {
            minutePicker = (NumberPicker) minute;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(numValues - 1);
            minutePicker.setDisplayedValues(displayedValues);
        }
    }

    public int getMinute() {
        if (minutePicker != null) {
            return (minutePicker.getValue() * INTERVAL);
        } else {
            return picker.getCurrentMinute();
        }
    }

    public void setMinute(int minute) {
        if (minutePicker != null) {
            minutePicker.setValue(minute / INTERVAL);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
