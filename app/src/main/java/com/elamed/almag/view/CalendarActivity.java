package com.elamed.almag.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elamed.almag.R;
import com.elamed.almag.ToolbarSizer;
import com.elamed.almag.data.Calendar.Calendar;
import com.elamed.almag.data.Calendar.CalendarAdapter;
import com.elamed.almag.data.Disease;
import com.elamed.almag.data.UpdaterData;


public class CalendarActivity extends AppCompatActivity {
    private CalendarAdapter calendarAdapter;
    private RecyclerView listView;
    private RecyclerView.LayoutManager recyclerLayoutManager;


    public CalendarActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_calendar);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ToolbarSizer.setAppBarHeight(appBarLayout, getResources());


        LinearLayout layout = findViewById(R.id.layout_toolbar);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = layout.getResources().getDimensionPixelSize(R.dimen.widthToolbat);
        params.height = layout.getResources().getDimensionPixelSize(R.dimen.heightToolbar);
        layout.setLayoutParams(params);

        UpdaterData.selectAllDataFromDB();
        listView = findViewById(R.id.listCalendar);
        recyclerLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(recyclerLayoutManager);
        calendarAdapter = new CalendarAdapter(this, UpdaterData.calendars);
        calendarAdapter.assetmanager = getAssets();
        UpdaterData.setCalendarAdapter(calendarAdapter);
        listView.setAdapter(calendarAdapter);
        calendarAdapter.notifyDataSetChanged();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendarAdapter.setListener(new CalendarAdapter.onClickListener() {
            @Override
            public void onVariantClick(Calendar calendar) {
                Disease disease = UpdaterData.getDiseaseById(UpdaterData.getIdDisease(calendar.getTimetable().getName()));
                Intent intent = new Intent(CalendarActivity.this, DescriptionActivity.class);
                intent.putExtra("disease", disease);
                startActivityForResult(intent, 1);
            }
        });
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