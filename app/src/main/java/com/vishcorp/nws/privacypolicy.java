package com.vishcorp.nws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class privacypolicy extends AppCompatActivity {
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy);
        back_btn = (ImageView) findViewById(R.id.backtosettings);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(privacypolicy.this, settingsFragment.class);
                startActivity(intent);
            }
        });
    }
}