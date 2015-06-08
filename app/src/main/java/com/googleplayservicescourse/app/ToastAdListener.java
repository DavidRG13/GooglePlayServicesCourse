package com.googleplayservicescourse.app;

import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

public class ToastAdListener extends AdListener {

    private Context context;

    public ToastAdListener(final Context context) {
        this.context = context;
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();
        showToast("Ad closed");
    }

    @Override
    public void onAdFailedToLoad(final int errorCode) {
        super.onAdFailedToLoad(errorCode);
        showToast(getErrorReason(errorCode));
    }

    @Override
    public void onAdLeftApplication() {
        super.onAdLeftApplication();
        showToast("Left application");
    }

    @Override
    public void onAdOpened() {
        super.onAdOpened();
        showToast("Ad opened");
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        showToast("Ad loaded");
    }

    public String getErrorReason(final int errorCode) {
        String reason = "";
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                reason = "Internal error";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                reason = "Invalid request";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                reason = "Network error";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                reason = "No fill";
                break;
        }
        return reason;
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
