package com.sen.audio.record;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.sen.audio.record.utils.GlobalContext;

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
