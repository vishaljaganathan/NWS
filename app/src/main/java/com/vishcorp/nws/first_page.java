package com.vishcorp.nws;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class first_page extends AppCompatActivity {
    Button btn;
    TextView work;
    private DrawerLayout drawerLayout;

    int DrawerLayout;
    float v=0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        work=(TextView) findViewById(R.id.gotoworkerlogin) ;
        work.setPaintFlags(work.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        btn = (Button) findViewById(R.id.btn_next);
        drawerLayout = findViewById(R.id.drawer_layout);
        //btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent intent=new Intent(first_page.this,user_login.class);
                    startActivity(intent);
                }
                catch(Exception e){

                    Toast.makeText(first_page.this, "1", Toast.LENGTH_SHORT).show();
                }

            }
        });


        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(first_page.this, worker_login.class);
                startActivity(intent);
            }
        });
    }
}
