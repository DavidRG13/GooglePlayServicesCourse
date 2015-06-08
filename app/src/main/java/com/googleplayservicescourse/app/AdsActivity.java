package com.googleplayservicescourse.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AdsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        findViewById(R.id.launch_banner_activity).setOnClickListener(this);
        findViewById(R.id.launch_interstitial_activity).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        if (id == R.id.launch_banner_activity) {
            startActivity(new Intent(this, BannerActivity.class));
        } else if (id == R.id.launch_interstitial_activity) {
            startActivity(new Intent(this, InterstitialActivity.class));
        }
    }
}
