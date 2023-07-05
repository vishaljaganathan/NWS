package com.vishcorp.nws;

import static android.icu.text.ListFormatter.Type.OR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class worker_profile extends AppCompatActivity {
    private DatabaseReference reference;
    Button workerLogout;
    ImageView wimage,editpro,notification;
    TextView wname,wjob,wmail,wphnumber,waddress,wabout;
   public String workerid;
    String image,name,username,phoneNo,password,email,about,address,jobname,eduction;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        workerid = getIntent().getStringExtra("password");
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String unm=sp1.getString("name", "");
         workerid  = sp1.getString("password", "");
        workerLogout=(Button)findViewById(R.id.workerLogout);
        reference = FirebaseDatabase.getInstance().getReference("worker").child(workerid);
        notification = (ImageView)findViewById(R.id.notification);
        editpro=(ImageView)findViewById(R.id.editworkerprfile);
        wimage=(ImageView)findViewById(R.id.workerprofileimage);
        wname=(TextView)findViewById(R.id.workerprofilename);
        wjob=(TextView)findViewById(R.id.workerprofilejob);
        wmail=(TextView)findViewById(R.id.workerprofilemail);
        wphnumber=(TextView)findViewById(R.id.workerprofilephonenumber);
        waddress=(TextView)findViewById(R.id.workerprofileaddress);
        wabout=(TextView)findViewById(R.id.workerprofileabout);

        editpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent=new Intent(getApplicationContext(),edit_worker_profile.class);
//                intent.putExtra("name",name);
//                intent.putExtra("username",username);
//                intent.putExtra("about",about);
//                intent.putExtra("email",email);
//                intent.putExtra("phone",phoneNo);
//                intent.putExtra("address",address);
//                intent.putExtra("eduction",eduction);
//                intent.putExtra("jobname",jobname);
//                intent.putExtra("password",password);
//                intent.putExtra("image",image);
//                startActivity(intent);

            }
        });

        workerLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = worker_profile.this.getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                    Intent intent = new Intent(worker_profile.this,worker_login.class);
                startActivity(intent);
                Toast.makeText(worker_profile.this,"logout",Toast.LENGTH_SHORT).show();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(worker_profile.this,job_notification.class);
                startActivity(intent);
            }
        });

        editpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(worker_profile.this,edit_worker_profile.class);
                startActivity(intent);
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 image=dataSnapshot.child("pimage").getValue().toString();
                 name = dataSnapshot.child("name").getValue().toString();
                 username = dataSnapshot.child("username").getValue().toString();
                 email = dataSnapshot.child("email").getValue().toString();
                 phoneNo = dataSnapshot.child("phoneNo").getValue().toString();
                 password = dataSnapshot.child("password").getValue().toString();
                 address = dataSnapshot.child("address").getValue().toString();
                 jobname = dataSnapshot.child("jobname").getValue().toString();
                 eduction = dataSnapshot.child("education").getValue().toString();
                 about = dataSnapshot.child("about").getValue().toString();

                wname.setText(name);
                wmail.setText(email);
                wphnumber.setText(phoneNo);
                waddress.setText(address);
                wjob.setText(jobname);
                wabout.setText(about);
                Picasso.get().load(image).into(wimage);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}