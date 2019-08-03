package com.elamed.almag.view;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.elamed.almag.R;
import com.elamed.almag.ToolbarSizer;
import com.elamed.almag.data.DBHelper;
import com.elamed.almag.data.Preferences;
import com.elamed.almag.data.Request;
import com.elamed.almag.data.RequestTypes;
import com.elamed.almag.data.Responce;
import com.elamed.almag.data.ResponceType;
import com.elamed.almag.data.UpdaterData;
import com.elamed.almag.net.Client;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String ACTION_CLOSE = "com.elamed.almag.CLOSE_LOADING";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper.setContext(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ToolbarSizer.setAppBarHeight(appBarLayout, getResources());

        LinearLayout layout = findViewById(R.id.layout_toolbar);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        params.width = size.x;
        params.height = layout.getResources().getDimensionPixelSize(R.dimen.heightToolbar);
        layout.setLayoutParams(params);

        final LinearLayout layoutCalendar = findViewById(R.id.layoutCalendar);
        final LinearLayout layoutDiseases = findViewById(R.id.layoutDiseases);
        final LinearLayout layoutCall = findViewById(R.id.layoutFeedbackCall);
        final LinearLayout layoutEmail = findViewById(R.id.layoutFeedbackEmail);
        final LinearLayout treatingLayout = findViewById(R.id.treatingLayout);
        final LinearLayout feedbackLayout = findViewById(R.id.feedbackLayout);

        final LinearLayout textViewTreating = findViewById(R.id.buttonTreating);
        final LinearLayout textViewFeedback = findViewById(R.id.buttonFeedback);

        layoutCalendar.setOnClickListener(v -> {
            UpdaterData.selectAllDataFromDB();
            if (UpdaterData.timetables.size() != 0) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), NewTimetableActivity.class);
                intent.putExtra("goToCalendar", "yes");
                startActivity(intent);
            }
        });
        layoutDiseases.setOnClickListener(v -> openActivityDiseases(""));
        layoutCall.setOnClickListener(v -> callToTechnicalSupport());
        layoutEmail.setOnClickListener(v -> sendEmail());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AppCompatImageView arrow;
                final LinearLayout linearLayout;
                if (v.equals(textViewTreating)) {
                    linearLayout = treatingLayout;
                    arrow = findViewById(R.id.arrowTreating);
                } else {
                    linearLayout = feedbackLayout;
                    arrow = findViewById(R.id.arrowFeedback);
                }
                final int newVisibility;
                final float alpha;
                if (linearLayout.getVisibility() == View.GONE) {
                    newVisibility = View.VISIBLE;
                    alpha = 1f;
                } else {
                    newVisibility = View.GONE;
                    alpha = 0.0f;
                }
                linearLayout.animate()
                        .setDuration(300)
                        .alpha(alpha)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                linearLayout.setVisibility(newVisibility);
                                arrow.setRotation(arrow.getRotation() + 180);
                            }
                        });
            }
        };
        textViewTreating.setOnClickListener(onClickListener);
        textViewFeedback.setOnClickListener(onClickListener);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.help).setOnClickListener(v -> startLearning());

        AsyncRequest a = new AsyncRequest(this);
        Request request = new Request();
        request.setTypeRequest(RequestTypes.Checking);
        request.setDiseases(UpdaterData.getDiseases());
        a.execute(request);

        Preferences.setmSettings(getSharedPreferences(Preferences.APP_PREFERENCES, Context.MODE_PRIVATE));
        if (Preferences.isFirst()) {
            startLearning();
        }

        if (getIntent().getStringExtra("goToCalendar") != null) {
            Intent calendar = new Intent(getApplicationContext(), CalendarActivity.class);
            startActivity(calendar);
        }

        findViewById(R.id.menu).setOnClickListener(v -> {
            final DrawerLayout drawer = findViewById(R.id.main_drawer);
            drawer.openDrawer(GravityCompat.START);
        });
    }

    private void callToTechnicalSupport() {
        int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+78003500413"));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                    10001);
        }
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:doctor@elamed.com"));
        startActivity(intent);
    }

    private void startLearning() {
        ShowIntro("Заболевания", "Чтобы почитать о болезнях, которые лечит Алмаг, нажмите сюда", R.id.layoutDiseases, 2);
    }

    private void ShowIntro(String title, String text, int viewId, final int type) {
        new GuideView.Builder(this)
                .setTitle(title)
                .setContentText(text)
                .setTargetView(findViewById(viewId))
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .setDismissType(GuideView.DismissType.outside)
                .setGuideListener(view -> {
                    if (type == 2) {
                        ShowIntro("Календарь", "Для просмотра хода лечения нажмите сюда", R.id.layoutCalendar, 3);
                    } else if (type == 3) {
                        ScrollView sv = findViewById(R.id.scrollView);
                        sv.scrollTo(0, findViewById(R.id.layoutFeedbackEmail).getBottom());
                        ShowIntro("Звонок в техподдержку", "Чтобы позвонить в техподдержку по поводу работы аппарата Алмаг, нажмите сюда", R.id.layoutFeedbackCall, 4);
                    } else if (type == 4) {
                        ShowIntro("Написать электронное письмо", "Чтобы написать в техподдержку по поводу работы аппарата Алмаг, нажмите сюда", R.id.layoutFeedbackEmail, 5);
                    }
                })
                .build()
                .show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.magnetotherapy) {
            openActivityDiseases(DBHelper.PREF_MAGNETOTHERAPY);
        } else if (id == R.id.almag_plus) {
            openActivityDiseases(DBHelper.PREF_ALMAG);
        } else if (id == R.id.diseases) {
            openActivityDiseases("");
        } else if (id == R.id.otherDevices) {
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setTitle(R.string.confirmToSite);
            ab.setPositiveButton("OK", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://elamed.com/dlya-domashnego-primeneniya/produktsiya"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            });
            ab.show();
        } else if (id == R.id.newPlanOfTreating) {
            Intent intent = new Intent(getApplicationContext(), NewTimetableActivity.class);
            startActivity(intent);
        } else if (id == R.id.feedbackHelp) {
            startLearning();
        } else if (id == R.id.feedbackCall) {
            callToTechnicalSupport();
        } else if (id == R.id.feedbackEmail) {
            sendEmail();
        } else if (id == R.id.details) {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            startActivity(intent);
        } else if (id == R.id.exit) {
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.main_drawer);
        drawer.closeDrawer(GravityCompat.START);

        return super.onOptionsItemSelected(item);
    }

    private void openActivityDiseases(String pref) {
        Intent intent = new Intent(this, ListDiseasesActivity.class);
        intent.putExtra("pref", pref);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

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
                ab.setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
                    startActivityForResult(intent, 0);
                    loadingActivity = new LoadingActivity();


                    AsyncRequest ar = new AsyncRequest(context);
                    ar.loadingActivity = loadingActivity;
                    request.setTypeRequest(RequestTypes.Loading);
                    ar.execute(request);
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
