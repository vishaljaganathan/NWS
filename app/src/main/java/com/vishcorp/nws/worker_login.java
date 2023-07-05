package com.vishcorp.nws;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class worker_login extends AppCompatActivity {
    public static final String Username = "name";
    public static final String Password = "password";
    TextInputLayout worker_workername, worker_password;
    Button workerlogin;
    private DatabaseReference databaseRef;
    TextView workersignup;
    String workerid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);
        workersignup = (TextView) findViewById(R.id.workersignup);
        workerlogin = (Button) findViewById(R.id.login);
        //auth = FirebaseAuth.getInstance();
        worker_workername = (TextInputLayout) findViewById(R.id.txtInLayoutworkername);
        worker_password = (TextInputLayout) findViewById(R.id.txtInLayoutworkerPassword);

        databaseRef = FirebaseDatabase.getInstance().getReference("worker");

        workersignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(worker_login.this, worker_signup.class);
                startActivity(intent);

            }
        });

        workerlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateworkername() | !validateworkerPassword()) {
                } else {
                    workerloginpage();
                }

            }
        });


        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String name = sp.getString("name", "noname");
        String password=sp.getString("password","nopassword");
        if (name != "noname") {
            SharedPreferences sp2=getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putString(Username,name );
            Ed.putString(Password,password);
            Ed.commit();
            Intent intent = new Intent(worker_login.this, worker_profile.class);
            intent.putExtra("password", workerid);
            startActivity(intent);
        }

    }

    private boolean validateworkerPassword() {
        String val = worker_password.getEditText().getText().toString();
        if (val.isEmpty()) {
            worker_password.setError("Password cannot be empty");
            return false;
        } else {
            worker_password.setError(null);
            return true;
        }
    }

    public Boolean validateworkername() {
        String val = worker_workername.getEditText().getText().toString();
        if (val.isEmpty()) {
            worker_workername.setError("Username cannot be empty");
            return false;
        } else {
            worker_workername.setError(null);
            return true;
        }
    }

    private void workerloginpage() {

        final String username_data = worker_workername.getEditText().getText().toString();
        final String password_data = worker_password.getEditText().getText().toString();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {

                    String name = datasnapshot.child("name").getValue().toString();
                    String password = datasnapshot.child("password").getValue().toString();

                    if (username_data.equals(name)) {
                        if (password_data.equals(password)) {

                            SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor Ed = sp.edit();
                            Ed.putString(Username, name);
                            Ed.putString(Password, password);
                            Ed.commit();
                            workerid=datasnapshot.child("password").getValue().toString();
                            Intent intent = new Intent(worker_login.this, worker_profile.class);
                            intent.putExtra("password", workerid);
                            startActivity(intent);

                            Toast.makeText(worker_login.this, "success", Toast.LENGTH_SHORT).show();
                        } else {
                            worker_password.setError("Invalid Credentials");
                            worker_password.requestFocus();
                        }
                    } else {
                        worker_workername.setError("User does not exist");
                        worker_workername.requestFocus();
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
        worker_workername.getEditText().setText(name);
        worker_password.getEditText().setText(password);

    }

    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("name", worker_workername.getEditText().getText().toString());
        myEdit.putString("password", worker_password.getEditText().getText().toString());
        myEdit.apply();
    }
}