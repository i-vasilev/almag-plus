package com.elamed.almag.view;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.elamed.almag.R;
import com.elamed.almag.ToolbarSizer;
import com.elamed.almag.data.Timetable.Timetable;
import com.elamed.almag.data.Timetable.TimetableAdapter;
import com.elamed.almag.data.UpdaterData;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {
    private TimetableAdapter timetableAdapter;
    MenuItem menuDelete;

    public static int position = -1;

    public DetailsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ListView timetableList = findViewById(R.id.list_item);
        UpdaterData.updateAdapters();
        timetableAdapter = new TimetableAdapter(getApplicationContext(), R.layout.list_item_disease, UpdaterData.timetables);
        UpdaterData.setTimetableAdapter(timetableAdapter);
        UpdaterData.selectAllDataFromDB();
        timetableList.setAdapter(timetableAdapter);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ToolbarSizer.setAppBarHeight(appBarLayout, getResources());

        final LinearLayout layout = findViewById(R.id.layout_toolbar);
        final ViewGroup.LayoutParams params = layout.getLayoutParams();
        final Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        params.width = size.x;
        params.height = layout.getResources().getDimensionPixelSize(R.dimen.heightToolbar);
        layout.setLayoutParams(params);

        FloatingActionButton addNewTimetable = findViewById(R.id.addNewTimetable);
        View.OnClickListener addNewTimetableListener = v -> {
            Intent addNewTimetable12 = new Intent(getApplicationContext(), NewTimetableActivity.class);
            startActivityForResult(addNewTimetable12, 1);
        };

        FloatingActionButton edit = findViewById(R.id.edit);
        edit.setOnClickListener(v -> {
            if (position != -1) {
                Timetable timetable = timetableAdapter.getItem(position);

                Intent addNewTimetable1 = new Intent(getApplicationContext(), NewTimetableActivity.class);
                addNewTimetable1.putExtra("timetable", timetable);
                startActivityForResult(addNewTimetable1, 1);
            }
        });

        addNewTimetable.setOnClickListener(addNewTimetableListener);
        timetableList.setClickable(true);

        registerForContextMenu(timetableList);
        timetableList.setOnItemClickListener((parent, view, position, id) -> {
            DetailsActivity.position = position;
            timetableAdapter.isCheckedByPosition = true;
            timetableAdapter.setChecked(position);
            timetableAdapter.isCheckedByPosition = false;
        });
        findViewById(R.id.back).setOnClickListener(v -> finish());
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // MenuInflater inflater = getMenuInflater();
//        findViewById(R.id.lineEditing).setVisibility(View.VISIBLE);
//        findViewById(R.id.list_item).setEnabled(false);
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        position = info.position;
        //inflater.inflate(R.menu.context_menu_details, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        menuDelete = menu.findItem(R.id.action_delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } else {
            if (item.equals(menuDelete)) {
                if (position != -1)
                    UpdaterData.deleteTimetable(Objects.requireNonNull(timetableAdapter.getItem(position)), getApplicationContext());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                Timetable timetable = timetableAdapter.getItem(info.position);

                Intent addNewTimetable = new Intent(getApplicationContext(), NewTimetableActivity.class);
                addNewTimetable.putExtra("timetable", timetable);
                startActivityForResult(addNewTimetable, 1);
                return true;

            case R.id.delete:
                UpdaterData.deleteTimetable(Objects.requireNonNull(timetableAdapter.getItem(info.position)), getApplicationContext());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
