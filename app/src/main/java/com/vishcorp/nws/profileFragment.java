package com.vishcorp.nws;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vishcorp.nws.databinding.FragmentProfileBinding;


public class profileFragment extends Fragment {
    DatabaseReference reference;
    ImageView uimage;
    Button userLogout;
    TextView uname,umail,uphnumber,uaddress;
    private FragmentProfileBinding binding;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        SharedPreferences sp1=getActivity().getSharedPreferences("Login", MODE_PRIVATE);
        String unm=sp1.getString("name", "");
        String pass = sp1.getString("password", "");

        uimage=(ImageView)view.findViewById(R.id.userimage);
        userLogout=(Button)view.findViewById(R.id.userLogout);
        uname=(TextView)view.findViewById(R.id.username);
        umail=(TextView)view.findViewById(R.id.usermail);
        uphnumber=(TextView)view.findViewById(R.id.userphonenumber);
        uaddress=(TextView)view.findViewById(R.id.useraddress);

        reference = FirebaseDatabase.getInstance().getReference("User").child(pass);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = String.valueOf(dataSnapshot.child("name").getValue());
                String usermail = String.valueOf(dataSnapshot.child("email").getValue());
                String userphonenumber = String.valueOf(dataSnapshot.child("phoneNo").getValue());
                String useraddress = String.valueOf(dataSnapshot.child("address").getValue());
                String userimage = String.valueOf(dataSnapshot.child("pimage").getValue());

                Picasso.get().load(userimage).into(uimage);
                uname.setText(username);
                umail.setText(usermail);
                uphnumber.setText(userphonenumber);
                uaddress.setText(useraddress);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(),user_login.class);
                startActivity(intent);
                Toast.makeText(getActivity(),"logout",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
