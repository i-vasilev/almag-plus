package com.elamed.almag.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.elamed.almag.R;
import com.elamed.almag.ToolbarSizer;
import com.elamed.almag.data.DBHelper;
import com.elamed.almag.data.Request;
import com.elamed.almag.data.RequestTypes;
import com.elamed.almag.data.Responce;
import com.elamed.almag.data.ResponceType;
import com.elamed.almag.data.UpdaterData;
import com.elamed.almag.net.Client;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String ACTION_CLOSE = "com.elamed.almag.CLOSE_LOADING";


    private void setOnCheckedOnDetails(boolean isChecked) {
        final LinearLayout layoutDetails = findViewById(R.id.layoutDetails);
        final LinearLayout layoutCalendar = findViewById(R.id.layoutCalendar);
        final CheckBox checkBoxDetails = findViewById(R.id.checkboxDetails);
        final CheckBox checkBoxCalendar = findViewById(R.id.checkboxCalendar);

        checkBoxDetails.setChecked(isChecked);
        checkBoxCalendar.setChecked(!isChecked);
        if (isChecked) {
            layoutDetails.setBackgroundColor(getResources().getColor(R.color.white));
            layoutCalendar.setBackgroundColor(getResources().getColor(R.color.uncheckedItem));
        } else {
            layoutDetails.setBackgroundColor(getResources().getColor(R.color.uncheckedItem));
            layoutCalendar.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper.setContext(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ToolbarSizer.setAppBarHeight(appBarLayout, getResources());

        LinearLayout layout = findViewById(R.id.layout_toolbar);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = layout.getResources().getDimensionPixelSize(R.dimen.widthToolbat);
        params.height = layout.getResources().getDimensionPixelSize(R.dimen.heightToolbar);
        layout.setLayoutParams(params);


        final LinearLayout layoutDetails = findViewById(R.id.layoutDetails);
        final LinearLayout layoutCalendar = findViewById(R.id.layoutCalendar);
        final CheckBox checkBoxDetails = findViewById(R.id.checkboxDetails);
        final CheckBox checkBoxCalendar = findViewById(R.id.checkboxCalendar);

        layoutDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = !checkBoxDetails.isChecked();
                if (isChecked) {
                    setOnCheckedOnDetails(isChecked);
                }
            }
        });
        layoutCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = !checkBoxCalendar.isChecked();
                if (isChecked) {
                    setOnCheckedOnDetails(!isChecked);
                }
            }
        });

        checkBoxDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isChecked_ = !checkBoxDetails.isChecked();
                if (isChecked) {
                    setOnCheckedOnDetails(isChecked);
                }
            }
        });

        checkBoxCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isChecked_ = !checkBoxCalendar.isChecked();
                if (isChecked) {
                    setOnCheckedOnDetails(!isChecked);
                }
            }
        });


        FloatingActionButton buttonOk = findViewById(R.id.floatingActionButtonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxDetails.isChecked()) {

                    Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                    startActivity(intent);
                }
            }
        });

        FloatingActionButton buttonCancel = findViewById(R.id.floatingActionButtonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        AsyncRequest a = new AsyncRequest(this);
        Request request = new Request();
        request.setTypeRequest(RequestTypes.Checking);
        request.setDiseases(UpdaterData.getDiseases());
        a.execute(request);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.magnetotherapy) {
            Intent intent = new Intent(this, DescriptionActivity.class);
            String article = UpdaterData.getArticleByPrefix(DBHelper.PREF_MAGNETOTHERAPY);
            intent.putExtra("article", article);
            startActivity(intent);
        } else if (id == R.id.almag_plus) {
            Intent intent = new Intent(this, DescriptionActivity.class);
            String article = UpdaterData.getArticleByPrefix(DBHelper.PREF_ALMAG);
            intent.putExtra("article", article);
            startActivity(intent);
        } else if (id == R.id.diseases) {
            Intent intent = new Intent(this, ListDiseasesActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify loadingActivity parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_settings).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }


    public class AsyncRequest extends AsyncTask<Request, Integer, Responce> {
        Exception exception;
        Request request;

        Activity loadingActivity;
        private Context context;

        public AsyncRequest(Context context) {
            this.context = context;
        }

        @Override
        protected Responce doInBackground(Request... arg) {
            exception = null;
            request = arg[0];
            Responce responce = new Responce();

            try {
                responce = Client.loadDecisions(request, "212.26.233.60", 8888);
            } catch (Exception e) {
                exception = e;
                return responce;
            }
            return responce;
        }


        @Override
        protected void onPostExecute(Responce s) {
            super.onPostExecute(s);
            if (exception != null) {
                return;
            }
            if (request.getTypeRequest() == RequestTypes.Checking && s.getType() == ResponceType.CheckDifference) {
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                ab.setTitle(R.string.NeedUpdateBase);
                ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
                        startActivityForResult(intent, 0);
                        loadingActivity = new LoadingActivity();


                        AsyncRequest ar = new AsyncRequest(context);
                        ar.loadingActivity = loadingActivity;
                        request.setTypeRequest(RequestTypes.Loading);
                        ar.execute(request);
                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ab.show();
            }
            if (request.getTypeRequest() == RequestTypes.Loading && s.getType() == ResponceType.CheckDifference) {
                UpdaterData.updateDiseases(s.getDiseases());
                if (loadingActivity != null) {
                    Intent intent = new Intent(ACTION_CLOSE);
                    sendBroadcast(intent);
                }
            }
        }
    }


}