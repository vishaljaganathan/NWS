package com.vishcorp.nws;
import android.Manifest;
import android.annotation.SuppressLint;
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
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class user_signup extends AppCompatActivity implements LocationListener{
    TextInputLayout userName, userUsername, userEmail, userPhoneNo, userPassword ;
    TextView userAddress,usersignup;
    private  String name,username,email,phoneNo,password,address;
    private double lat,lon;
    Button userBtn;
    ImageView userphote;
    Uri filepath;
    Bitmap bitmap;

    LocationManager locationManager;
    boolean isAllFieldsChecked = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);
        userName =findViewById(R.id.user_name);
        userUsername = findViewById(R.id.user_username);
        userEmail = findViewById(R.id.user_email);
        userPhoneNo = findViewById(R.id.user_phoneNo);
        userPassword = findViewById(R.id.user_password);
        userphote=(ImageView) findViewById(R.id.getuserimageinimageviews);
        userAddress = findViewById(R.id.user_address);
        userBtn = findViewById(R.id.usersign_btn);
        usersignup = (TextView) findViewById(R.id.user_sign);
        locationpermissin();
        usersignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(user_signup.this,user_login.class);
                startActivity(intent);
            }
        });
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    //https://www.youtube.com/watch?v=9bZRMn6mZWc&ab_channel=MdJamal
                    name = userName.getEditText().getText().toString();
                    username = userUsername.getEditText().getText().toString();
                    email = userEmail.getEditText().getText().toString();
                    phoneNo = userPhoneNo.getEditText().getText().toString();
                    password = userPassword.getEditText().getText().toString();
                    address =userAddress.getText().toString();
                    userphote.setImageResource(R.drawable.user_icon);

                    //firebase data upload
                    uploadedatafb();
                }
            }
        });
        userphote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadeimage();
            }
        });
    }

    private void uploadedatafb() {
        if(filepath==null)
        {
            Toast.makeText(getApplicationContext(),"Image is userty",Toast.LENGTH_SHORT).show();
        }else {
            final ProgressDialog pd=new ProgressDialog(this);
            pd.setTitle("Uploade data...");
            pd.show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
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
                                    DatabaseReference reference = firebaseDatabase1.getReference("User");

                                    User user = new User(name, username, email, phoneNo, password, address,
                                            uri.toString(), lat, lon);

                                    reference.child(password).setValue(user);
                                    Toast.makeText(user_signup.this, "data uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            pd.setMessage("Uploaded :" + (int) percent + "%");

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
                userphote.setImageBitmap(bitmap);
            }catch (Exception ex){
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private boolean CheckAllFields() {

        if(userName.getEditText().getText().length() == 0) {
            userName.setError("This field is required");
            return false;
        } else{userName.setError(null);}

        if (userUsername.getEditText().getText().length()==0) {
            userUsername.setError("This field is required");
            return false;
        }else{userUsername.setError(null);}

        if (userEmail.getEditText().getText().length()==0) {
            userEmail.setError("Email is required");
            return false;
        }else{userEmail.setError(null);}

        if (userPhoneNo.getEditText().getText().toString().isEmpty()) {
            userPhoneNo.setError("Number is required");
            return false;
        }else{userPhoneNo.setError(null);}

        if (userAddress.length()==0) {
            userAddress.setError("Address is required");
            return false;
        }else {userAddress.setError(null);}

        if (userPassword.getEditText().getText().toString().length() < 8) {
            userPassword.setError("Password must be minimum 8 characters");
            return false;
        }else {userPassword.setError(null);}

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

            userAddress.setText(addresses.get(0).getAddressLine(0));

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
