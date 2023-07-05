package com.vishcorp.nws;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class user_login extends AppCompatActivity {
    public  static  final String Username="name";
    public  static  final String Password="password";
    Button userloginbtn;
    TextView usersignUp;
    private  DatabaseReference databaseRef;
    TextInputLayout user_password,user_username;
    //https://www.youtube.com/watch?v=n1zL7YLrpvI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        userloginbtn = (Button) findViewById(R.id.userloginbtn);
        usersignUp=(TextView)findViewById(R.id.usersignUp);

        user_username=findViewById(R.id.txtInLayoutUsername);
        user_password=findViewById(R.id.txtInLayoutuserPassword);

        databaseRef= FirebaseDatabase.getInstance().getReference("User");
        userloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateusername() | !validateuserPassword()) {
                } else {
                    userloginpage();
                }
            }
        });


        SharedPreferences sp=getSharedPreferences("Login",MODE_PRIVATE);
        String name=sp.getString("name","noname");
        if(name!="noname"){

            Intent intent = new Intent(user_login.this,MainActivity.class);
            startActivity(intent);

        }



        usersignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(user_login.this,user_signup.class);
                startActivity(intent);
            }
        });
        onBackPressed();
    }
    @Override
    public void onBackPressed() {}

    public Boolean validateusername() {
        String val = user_username.getEditText().getText().toString();
        if (val.isEmpty()) {
            user_username.setError("Username cannot be empty");
            return false;
        } else {
            user_username.setError(null);
            return true;
        }
    }
    public Boolean validateuserPassword(){
        String val = user_password.getEditText().getText().toString();
        if (val.isEmpty()) {
            user_password.setError("Password cannot be empty");
            return false;
        } else {
            user_password.setError(null);
            return true;
        }
    }

    private void userloginpage() {

        final String username_data=user_username.getEditText().getText().toString();
        final String password_data=user_password.getEditText().getText().toString();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot:snapshot.getChildren()){

                    String name = datasnapshot.child("name").getValue().toString();
                    String password = datasnapshot.child("password").getValue().toString();

                    if(username_data.equals(name)){
                        if(password_data.equals(password)){

                            SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor Ed=sp.edit();
                            Ed.putString(Username,name );
                            Ed.putString(Password,password);
                            Ed.commit();
                            Intent intent = new Intent(user_login.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(user_login.this, "success", Toast.LENGTH_SHORT).show();
                        }else {
                            user_password.setError("Invalid Credentials");
                            user_password.requestFocus();
                        }
                        } else {
                        user_username.setError("User does not exist");
                        user_username.requestFocus();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sh = getSharedPreferences("Login", MODE_PRIVATE);
        String name = sh.getString("name", "");
        String password = sh.getString("password", "");


        user_username.getEditText().setText(name);
        user_password.getEditText().setText(password);

    }

    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("name", user_username.getEditText().getText().toString());
        myEdit.putString("password", user_password.getEditText().getText().toString());
        myEdit.apply();
    }

}