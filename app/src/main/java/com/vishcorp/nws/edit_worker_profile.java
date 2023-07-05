package com.vishcorp.nws;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class edit_worker_profile   extends AppCompatActivity implements LocationListener{
//    extends AppCompatActivity implements LocationListener
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText edName, edAbout, edEmail, edPhoneNo;
    TextView edaddress,edjobname;
    Button updatebtn;
    LocationManager locationManager;
    private double lat,lon;
    ShapeableImageView edphote;

    private String name, username, email, phoneNo, password, address, jobname, education, about;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_worker_profile);
        locationpermissin();

        edName = findViewById(R.id.ed_name);
        edAbout = findViewById(R.id.ed_about);
        edEmail = findViewById(R.id.ed_mail);
        edPhoneNo = findViewById(R.id.ed_number);
        edjobname=findViewById(R.id.ed_jobname);
        edaddress=findViewById(R.id.ed_address);
        edphote = findViewById(R.id.ed_image);

        updatebtn = (Button)findViewById(R.id.ed_update);

        edName.setText(getIntent().getExtras().getString("name"));
        edAbout.setText(getIntent().getExtras().getString("about"));
        edEmail.setText(getIntent().getExtras().getString("email"));
        edPhoneNo.setText(getIntent().getExtras().getString("phone"));
        password=getIntent().getExtras().getString("password");
        jobname=getIntent().getExtras().getString("jobname");
        username=getIntent().getExtras().getString("username");
        education=getIntent().getExtras().getString("eduction");
        edjobname.setText(getIntent().getExtras().getString("jobname"));
        edaddress.setText(getIntent().getExtras().getString("address"));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int resId = bundle.getInt("resId");
            edphote.setImageResource(resId);
        }
        name = edName.getText().toString();
        about = edAbout.getText().toString();
        email = edEmail.getText().toString();
        phoneNo = edPhoneNo.getText().toString();

       // addDatatoFirebase(name, username, email, phoneNo, password, address,jobname,education,about,lat,lon);

    }

    private void addDatatoFirebase(String name, String username, String email, String phoneNo,
                                   String password, String address, String jobname, String education, String about, double lat, double lon) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("worker");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                worker worker = new worker(name, username, email, phoneNo, password, address, jobname, education, about, fileList().toString(), lat, lon);
                databaseReference.child(password).setValue(worker);
                Toast.makeText(edit_worker_profile.this, "data added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(edit_worker_profile.this, worker_doc_page.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(edit_worker_profile.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void locationpermissin() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        locationManager = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);
        locationEnabled();
        getLocation();
    }
    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //https://console.cloud.google.com/apis/credentials?project=comvishcorpnws
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            edaddress.setText(addresses.get(0).getAddressLine(0));

            lat=addresses.get(0).getLatitude();
            lon=addresses.get(0).getLongitude();

        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

}