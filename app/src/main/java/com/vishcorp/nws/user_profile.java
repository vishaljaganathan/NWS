package com.vishcorp.nws;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class user_profile extends AppCompatActivity {

    DatabaseReference reference;
    ImageView uimage;
    TextView uname,umail,uphnumber,uaddress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        uimage=(ImageView)findViewById(R.id.userimage);
        uname=(TextView)findViewById(R.id.username);
        umail=(TextView)findViewById(R.id.usermail);
        uphnumber=(TextView)findViewById(R.id.userphonenumber);
        uaddress=(TextView)findViewById(R.id.useraddress);
        reference = FirebaseDatabase.getInstance().getReference("User").child("tharun123");

        String username="";
        if (!username.isEmpty()){
            readdata(username);
        }

    }
    private void readdata(String username) {

        reference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        Toast.makeText(user_profile.this,"Successfully Read",Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();

                        String username = String.valueOf(dataSnapshot.child("name").getValue());
                        String usermail = String.valueOf(dataSnapshot.child("email").getValue());
                        String userphonenumber = String.valueOf(dataSnapshot.child("phnumber").getValue());
                        String useraddress = String.valueOf(dataSnapshot.child("address").getValue());
                        String userimage = String.valueOf(dataSnapshot.child("pimage").getValue());

                        Picasso.get().load(userimage).into(uimage);
                        uname.setText(username);
                        umail.setText(usermail);
                        uphnumber.setText(userphonenumber);
                        uaddress.setText(useraddress);
                        // userimage.setImageResource(userimage);

                    }else {
                        Toast.makeText(user_profile.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(user_profile.this,"Failed to read",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}