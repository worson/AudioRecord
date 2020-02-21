package com.app.audio.record;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.lib.common.androidbase.global.GlobalContext;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalContext.set(getApplication());
        Intent intent = new Intent(this,PcmRecorderActivity.class);
        startActivity(intent);
    }
}
