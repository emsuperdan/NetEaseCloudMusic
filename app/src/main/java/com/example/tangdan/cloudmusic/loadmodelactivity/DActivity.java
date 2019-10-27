package com.example.tangdan.cloudmusic.loadmodelactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tangdan.cloudmusic.R;

import static com.example.tangdan.cloudmusic.utils.Constants.PUBLIC_TAG;

public class DActivity extends AppCompatActivity {
    public static final String TAG = "DActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d);
        Log.d(PUBLIC_TAG,TAG+"-------onCreate");

        Button standardButton = findViewById(R.id.standard);
        standardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DActivity.this, AActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(PUBLIC_TAG,TAG+"-------onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(PUBLIC_TAG,TAG+"-------onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(PUBLIC_TAG,TAG+"-------onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(PUBLIC_TAG,TAG+"-------onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(PUBLIC_TAG,TAG+"-------onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(PUBLIC_TAG,TAG+"-------onDestroy");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(PUBLIC_TAG,TAG+"-------onNewIntent");
    }
}
