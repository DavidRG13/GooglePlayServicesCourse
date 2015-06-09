package com.googleplayservicescourse.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int STATE_SIGNED_IN = 2;
    private static final int STATE_IN_PROGRESS = 1;
    private static final int STATE_SIGN_IN = 0;
    private static final int RC_SIGN_IN = 123;
    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;
    private TextView status;
    private GoogleApiClient googleApiClient;
    private View signInBtn;
    private View signOutBtn;
    private View revokeAccessBtn;
    private int actualState;
    private PendingIntent resolution;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInBtn = findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(this);
        signOutBtn = findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(this);
        revokeAccessBtn = findViewById(R.id.revokeAccess);
        revokeAccessBtn.setOnClickListener(this);

        status = ((TextView) findViewById(R.id.status));

        googleApiClient = buildGoogleAPIClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        if (!googleApiClient.isConnecting()) {
            if (id == R.id.signInBtn) {
                status.setText("Signing in");
                resolveSignError();
            } else if (id == R.id.signOutBtn) {
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                googleApiClient.disconnect();
                googleApiClient.connect();
            } else if (id == R.id.revokeAccess) {
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient);
                googleApiClient = buildGoogleAPIClient();
                googleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(final Bundle bundle) {
        Log.i("AQUII", "Connected");

        signInBtn.setEnabled(false);
        signOutBtn.setEnabled(true);
        revokeAccessBtn.setEnabled(true);

        actualState = STATE_SIGNED_IN;

        String accountName = Plus.AccountApi.getAccountName(googleApiClient);
        status.setText(String.format("Signed In to G+ as %s", accountName));
    }

    @Override
    public void onConnectionSuspended(final int cause) {
        googleApiClient.connect();
        Log.i("AQUII", "OnConnectionSuspended: " + cause);
    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        Log.i("AQUII", "onConnectionFailed: (errorCode) " + connectionResult.getErrorCode());

        if (actualState != STATE_IN_PROGRESS) {
            resolution = connectionResult.getResolution();

            if (actualState == STATE_SIGN_IN) {
                resolveSignError();
            }
        }

        onSignedOut();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                actualState = STATE_SIGN_IN;
            } else {
                actualState = STATE_SIGNED_IN;
            }

            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }

    private void resolveSignError() {
        if (resolution != null) {
            try {
                actualState = STATE_IN_PROGRESS;
                startIntentSenderForResult(resolution.getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i("AQUII", "Sign in intent can not be launched: " + e.getMessage());

                actualState = STATE_SIGN_IN;
                googleApiClient.connect();
            }
        } else {
            showDialog(DIALOG_PLAY_SERVICES_ERROR);
        }
    }

    private void onSignedOut() {
        signInBtn.setEnabled(true);
        signOutBtn.setEnabled(false);
        revokeAccessBtn.setEnabled(false);

        status.setText("Signed out");
    }

    private GoogleApiClient buildGoogleAPIClient() {
        return new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Plus.API, Plus.PlusOptions.builder().build())
            .addScope(new Scope("email"))
            .build();
    }
}
