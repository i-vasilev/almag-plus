package com.elamed.almag.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.elamed.almag.R;
import com.elamed.almag.ToolbarSizer;
import com.elamed.almag.data.Disease;

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_view);

        WebView webView = findViewById(R.id.webViewDescription);
        final Disease d = (Disease) getIntent().getSerializableExtra("disease");
        String article = (String) getIntent().getSerializableExtra("article");

        if (d != null) {
            webView.loadDataWithBaseURL("file:///android_asset/", d.getDescription(), "text/html", "utf-8", null);
            findViewById(R.id.fabWrapperTreat).setVisibility(View.VISIBLE);
        } else if (article != null) {
            webView.loadDataWithBaseURL("file:///android_asset/", article, "text/html", "utf-8", null);
        }
        webView.setOnTouchListener((v, event) -> (event.getAction() == MotionEvent.ACTION_MOVE));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ToolbarSizer.setAppBarHeight(appBarLayout, getResources());

        LinearLayout layout = findViewById(R.id.layout_toolbar);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = layout.getResources().getDimensionPixelSize(R.dimen.widthToolbat);
        params.height = layout.getResources().getDimensionPixelSize(R.dimen.heightToolbar);
        layout.setLayoutParams(params);


        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.treat).setOnClickListener(v -> {
            if (d != null) {
                Intent intent = new Intent(getApplicationContext(), NewTimetableActivity.class);
                intent.putExtra("disease", d);
                startActivity(intent);
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
