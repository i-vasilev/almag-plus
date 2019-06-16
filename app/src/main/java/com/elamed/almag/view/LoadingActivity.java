package com.elamed.almag.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.elamed.almag.R;
import com.victor.loading.rotate.RotateLoading;

public class LoadingActivity extends Activity {
    private RotateLoading rotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        rotateLoading = findViewById(R.id.rotateLoading);
        rotateLoading.start();
        IntentFilter filter = new IntentFilter(MainActivity.ACTION_CLOSE);
        CloseLoadingReceiver closeLoadingReceiver = new CloseLoadingReceiver();
        registerReceiver(closeLoadingReceiver, filter);
    }


    @Override
    public void onBackPressed()
    {
    }

    class CloseLoadingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MainActivity.ACTION_CLOSE)) {
                LoadingActivity.this.finish();
            }
        }
    }
}
