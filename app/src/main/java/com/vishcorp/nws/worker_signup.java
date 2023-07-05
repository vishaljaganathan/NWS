package com.vishcorp.nws;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
//import com.vishcorp.nearbyworkers.Adclass.User;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Random;
//
public class  worker_signup extends AppCompatActivity implements LocationListener{
    TextInputLayout empName, empUsername, empEmail, empPhoneNo, empPassword ,empAbout;
    AutoCompleteTextView empJobname,empEducation;
    TextView empAddress;
    private  String name,username,email,phoneNo,password,address,jobname,education,about;

    private double lat,lon;
    Button empBtn;
    ImageView empphote;
    Uri filepath;
    Bitmap bitmap;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseDatabase database;
    DatabaseReference reference;

    LocationManager locationManager;
    boolean isAllFieldsChecked = false;


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_signup);
        empName =findViewById(R.id.emp_name);
        empUsername = findViewById(R.id.emp_username);
        empEmail = findViewById(R.id.emp_email);
        empPhoneNo = findViewById(R.id.emp_phoneNo);
        empPassword = findViewById(R.id.emp_password);

        empphote=(ImageView) findViewById(R.id.getworkerimageinimageviews);
        empAddress = findViewById(R.id.emp_address);
        empJobname = findViewById(R.id.emp_jobname);
        empEducation=findViewById(R.id.emp_education);
        empAbout =findViewById(R.id.emp_about);
        empBtn = findViewById(R.id.emp_btn);

        choosejob();
        chooseEdu();
        locationpermissin();

        empBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    //https://www.youtube.com/watch?v=9bZRMn6mZWc&ab_channel=MdJamal
                    name = empName.getEditText().getText().toString();
                    username = empUsername.getEditText().getText().toString();
                    email = empEmail.getEditText().getText().toString();
                    phoneNo = empPhoneNo.getEditText().getText().toString();
                    password = empPassword.getEditText().getText().toString();

                    address =empAddress.getText().toString();
                    jobname =empJobname.getText().toString();
                    education =empEducation.getText().toString();
                    about = empAbout.getEditText().getText().toString();
                    empphote.setImageResource(R.drawable.user_icon);

                    //firebase data upload
                    //addDatatoFirebase(name, username, email, phoneNo, password, address,jobname,education,about,lat,lon);

                    uploadedatafb();

                }
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
                        Toast.makeText(worker_signup.this, "data added", Toast.LENGTH_SHORT).show();

                         Intent intent=new Intent(worker_signup.this, worker_doc_page.class);
                       //  putExtraData();

                         startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(worker_signup.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        empphote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadeimage();
            }
        });


    }



    private void uploadedatafb() {
        if(filepath==null)
        {
            Toast.makeText(getApplicationContext(),"Image is Empty",Toast.LENGTH_SHORT).show();
        }else {
            final ProgressDialog pd=new ProgressDialog(this);
                    pd.setTitle("Uploade data...");
                    pd.show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            database = FirebaseDatabase.getInstance();
            StorageReference uploader = storage.getReference("Image" + new Random().nextInt(50));
            uploader.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pd.dismiss();
                                    FirebaseDatabase  firebaseDatabase1= FirebaseDatabase.getInstance();
                                    DatabaseReference reference = firebaseDatabase1.getReference("worker");

                                    worker worker = new worker(name, username, email, phoneNo, password, address,
                                            jobname, education, about, uri.toString(), lat, lon);


                                    reference.child(password).setValue(worker);
                                    Toast.makeText(worker_signup.this, "data uploaded", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(worker_signup.this, worker_doc_page.class);

                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            pd.setMessage("Uploaded :" + (int) percent + "%");

                            Toast.makeText(worker_signup.this, "next page", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }

    private void uploadeimage() {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent=new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            filepath=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream((inputStream));
                empphote.setImageBitmap(bitmap);
            }catch (Exception ex){
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private boolean CheckAllFields() {

        if(empName.getEditText().getText().length() == 0) {
            empName.setError("This field is required");
            return false;
        } else{empName.setError(null);}

        if (empUsername.getEditText().getText().length()==0) {
            empUsername.setError("This field is required");
            return false;
        }else{empUsername.setError(null);}

        if (empEmail.getEditText().getText().length()==0) {
            empEmail.setError("Email is required");
            return false;//https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
        }else{empEmail.setError(null);}

        if (empPhoneNo.getEditText().getText().toString().isEmpty()) {
            empPhoneNo.setError("Number is required");
            return false;
        }else{empPhoneNo.setError(null);}

        if (empAddress.length()==0) {
            empAddress.setError("Address is required");
            return false;
        }else {empAddress.setError(null);}
//        if (empEducation.getEditText().getText().toString().isEmpty()) {
//            empEducation.setError("Education quality is required");
//            return false;
//        }else {empEducation.setError(null);}
        if (empAbout.getEditText().getText().toString().isEmpty()) {
            empAbout.setError("This field is required");
            return false;
        }else {empAbout.setError(null);}

        if (empPassword.getEditText().getText().toString().length() < 8) {
            empPassword.setError("Password must be minimum 8 characters");
            return false;
        }else {empPassword.setError(null);}

        return true;
    }
    private void locationpermissin() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
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

            empAddress.setText(addresses.get(0).getAddressLine(0));

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

    private void choosejob() {

        String[] Subjects = new String[]{""};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, Subjects);
        empJobname.setAdapter(adapter);

        empJobname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + empJobname.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseEdu() { String[] Subjects = new String[]{"Preschool Education", "Primary Education", "Secondary Education","Vocational Education","Tertiary Education","Continuing Education"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, Subjects);
        empEducation.setAdapter(adapter);

        empEducation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + empEducation.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
