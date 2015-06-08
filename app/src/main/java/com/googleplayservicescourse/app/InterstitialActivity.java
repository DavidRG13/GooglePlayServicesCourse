package com.googleplayservicescourse.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class InterstitialActivity extends AppCompatActivity implements View.OnClickListener {

    private Button showButton;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        findViewById(R.id.load_interstitial).setOnClickListener(this);
        showButton = ((Button) findViewById(R.id.show_interstitial));
        showButton.setOnClickListener(this);
        showButton.setEnabled(false);
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        if (id == R.id.load_interstitial) {
            showButton.setEnabled(false);
            showButton.setText(R.string.loading_interstitial);

            interstitialAd = new InterstitialAd(this);
            interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
            interstitialAd.setAdListener(new ToastAdListener(this) {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    showButton.setText("Show interstitial");
                    showButton.setEnabled(true);
                }

                @Override
                public void onAdFailedToLoad(final int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    showButton.setText(getErrorReason(errorCode));
                }
            });

            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        } else if (id == R.id.show_interstitial) {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            }

            showButton.setText("Interstitial not ready");
            showButton.setEnabled(false);
        }
    }
}
