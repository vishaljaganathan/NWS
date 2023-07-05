package com.vishcorp.nws;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    DatabaseReference reference;
    TextView uname,uemail;
    ImageView uimageview;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // If you need to set image to navigation header image or setText for header textView follow the  code below

        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String unm=sp1.getString("name", "");
        String pass = sp1.getString("password", "");

        View headerView = navigationView.getHeaderView(0);
         uname = headerView.findViewById(R.id.uname);
         uemail = headerView.findViewById(R.id.uemail);
         uimageview = headerView.findViewById(R.id.uimageView);
        reference = FirebaseDatabase.getInstance().getReference("User").child(pass);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = String.valueOf(dataSnapshot.child("name").getValue());
                String usermail = String.valueOf(dataSnapshot.child("email").getValue());
                String userimage = String.valueOf(dataSnapshot.child("pimage").getValue());

                Picasso.get().load(userimage).into(uimageview);
                uname.setText(username);
                uemail.setText(usermail);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_view);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,
                                new HomeFragment()).commit();
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,
                                new profileFragment()).commit();
                        Toast.makeText(getApplicationContext(),"Profile",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,
                    new settingsFragment()).commit();
            Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_map) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,
                    new mapFragment()).commit();
            Toast.makeText(getApplicationContext(),"map",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_feature) {
            Intent intent = new Intent(MainActivity.this,about_application.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Application features",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_logout) {
            SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(MainActivity.this,user_login.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        drawer.closeDrawer(GravityCompat. START ) ;
        return true;

    }

    @Override
    public void onBackPressed() {
        // Show a dialog to confirm the back press action
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit");
        builder.setMessage("Are you sure you want to exit the app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If the user confirms, exit the app
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If the user cancels, dismiss the dialog
                dialog.dismiss();
            }
        });
        builder.show();
    }
}



