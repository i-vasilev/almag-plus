package com.elamed.almag.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.elamed.almag.R;
import com.elamed.almag.ToolbarSizer;
import com.elamed.almag.data.Timetable.Timetable;
import com.elamed.almag.data.Timetable.TimetableAdapter;
import com.elamed.almag.data.UpdaterData;

public class DetailsActivity extends AppCompatActivity {
    private ListView timetableList;
    private TimetableAdapter timetableAdapter;

    public static int position = -1;

    public DetailsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        timetableList = findViewById(R.id.list_item);
        UpdaterData.updateAdapters();
        timetableAdapter = new TimetableAdapter(getApplicationContext(), R.layout.list_item_disease, UpdaterData.timetables);
        UpdaterData.setTimetableAdapter(timetableAdapter);
        UpdaterData.selectAllDataFromDB();
        timetableList.setAdapter(timetableAdapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ToolbarSizer.setAppBarHeight(appBarLayout, getResources());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayout layout = findViewById(R.id.layout_toolbar);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = layout.getResources().getDimensionPixelSize(R.dimen.widthToolbat);
        params.height = layout.getResources().getDimensionPixelSize(R.dimen.heightToolbar);
        layout.setLayoutParams(params);
//        findViewById(R.id.lineEditing).setVisibility(View.INVISIBLE);
//        findViewById(R.id.list_item).setEnabled(true);

        FloatingActionButton addNewTimetable = findViewById(R.id.addNewTimetable);
        View.OnClickListener addNewTimetableListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewTimetable = new Intent(getApplicationContext(), NewTimetableActivity.class);
                startActivityForResult(addNewTimetable, 1);
            }
        };

        Button remove = findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != -1)
                    UpdaterData.deleteTimetable(timetableAdapter.getItem(position), getApplicationContext());
//                findViewById(R.id.lineEditing).setVisibility(View.INVISIBLE);
//                findViewById(R.id.list_item).setEnabled(true);
            }
        });

        Button edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != -1) {
                    Timetable timetable = timetableAdapter.getItem(position);

                    Intent addNewTimetable = new Intent(getApplicationContext(), NewTimetableActivity.class);
                    addNewTimetable.putExtra("timetable", timetable);
                    startActivityForResult(addNewTimetable, 1);
                }
            }
        });

        addNewTimetable.setOnClickListener(addNewTimetableListener);
        timetableList.setClickable(true);

        registerForContextMenu(timetableList);
        timetableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailsActivity.this.position = position;
                timetableAdapter.setChecked(position);
            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // MenuInflater inflater = getMenuInflater();
//        findViewById(R.id.lineEditing).setVisibility(View.VISIBLE);
//        findViewById(R.id.list_item).setEnabled(false);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        position = info.position;
        //inflater.inflate(R.menu.context_menu_details, menu);
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
                UpdaterData.deleteTimetable(timetableAdapter.getItem(info.position), getApplicationContext());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
