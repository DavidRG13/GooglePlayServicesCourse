package com.googleplayservicescourse.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.google.android.gms.tagmanager.ContainerHolder;

public class DailySpecialActivity extends AppCompatActivity{

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_special);

        TextView textView = (TextView) findViewById(R.id.daily_sentence);
        ContainerHolder containerHolder = ((MyApplication) getApplication()).getContainerHolder();
        String dailySentence = containerHolder.getContainer().getString("daily-sentence");
        textView.setText(dailySentence);
    }
}
