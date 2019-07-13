package com.elamed.almag.view;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.elamed.almag.R;
import com.elamed.almag.ToolbarSizer;
import com.elamed.almag.data.DBHelper;
import com.elamed.almag.data.Disease;
import com.elamed.almag.data.Diseases.StringAdapter;
import com.elamed.almag.data.UpdaterData;

import java.util.List;

public class ListDiseasesActivity extends AppCompatActivity {

    List<String> listNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_diseases);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String pref = (String) getIntent().getSerializableExtra("pref");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ToolbarSizer.setAppBarHeight(appBarLayout, getResources());
        LinearLayout layout = findViewById(R.id.layout_toolbar);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = layout.getResources().getDimensionPixelSize(R.dimen.widthToolbat);
        params.height = layout.getResources().getDimensionPixelSize(R.dimen.heightToolbar);
        layout.setLayoutParams(params);
        if (pref.equals("")) {
            listNames = UpdaterData.getStringDiseases();
        } else {
            listNames = UpdaterData.getArticlesByPrefix(pref);
        }
        StringAdapter adapter = new StringAdapter(this, R.layout.drawer_list_item, listNames);
        ListView listView = findViewById(R.id.list_item_diseases);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListDiseasesActivity.this, DescriptionActivity.class);
                if (pref.equals("")) {
                    intent.putExtra("disease", UpdaterData.getDiseaseById(UpdaterData.getDiseases().get(position).getId()));
                } else {
                    intent.putExtra("article", UpdaterData.getArticleByName(listNames.get(position)));
                }
                startActivityForResult(intent, 1);
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
