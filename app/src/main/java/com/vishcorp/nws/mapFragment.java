package com.vishcorp.nws;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class mapFragment extends Fragment implements OnMapReadyCallback {

    public mapFragment() {
        // require a empty public constructor
    }

    //https://www.youtube.com/watch?v=Mfq62_pZ4RA&ab_channel=Codewithapp https://www.youtube.com/watch?v=4qBLNr6dGhk&ab_channel=FelipeHern%C3%A1ndezPalafox
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fmap);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("worker");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String longitude = child.child("longitude").getValue().toString();
                    String latitude = child.child("latitude").getValue().toString();
                    String name = child.child("name").getValue().toString();
                    String job = child.child("jobname").getValue().toString();
                    //https://stackoverflow.com/questions/28022256/how-to-display-image-on-circular-image-on-google-map-in-android
                    BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                    double longit = Double.parseDouble(longitude);
                    double latit = Double.parseDouble(latitude);
                    LatLng loc = new LatLng(longit, latit);
                    googleMap.addMarker(new MarkerOptions().position(loc).title(name).snippet(job));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        //googleMap.setPadding( 0,  0,  30,  30);
    }
}
