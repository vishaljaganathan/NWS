package com.vishcorp.nws;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class workerFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_CALL = 1;
    TextView workername, workerjob, workermail, workerphonenumber, workeraddress, workerabout;

    private final String CHANNEL_ID = "Notification";

    private final int NOTIFICATION_ID = 01;
    LinearLayout hire, call, chat;
    ImageView back_btn;
    String ename, ejob, eimage, eaddress, eabout, ephone, eemail;
    private double Longitude, Latitude;
    public String number = ephone;
    private MapView mapView;
    private GoogleMap googleMap;

    public workerFragment(String ename, String ejob, String eimage, String eaddress, String eabout, String ephone, String eemail, double longitude, double latitude) {
        this.ename = ename;
        this.ejob = ejob;
        this.eimage = eimage;
        this.eaddress = eaddress;
        this.eabout = eabout;
        this.ephone = ephone;
        this.eemail = eemail;
        Longitude = longitude;
        Latitude = latitude;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_worker, container, false);

        ImageView workerimage = view.findViewById(R.id.workerimage);
        workername = view.findViewById(R.id.workername);
        workerjob = view.findViewById(R.id.workerjob);
        workermail = view.findViewById(R.id.workermail);
        workerphonenumber = view.findViewById(R.id.workerphonenumber);
        workeraddress = view.findViewById(R.id.workeraddress);
        workerabout = view.findViewById(R.id.workerabout);
        call = view.findViewById(R.id.call);
        chat = view.findViewById(R.id.chat);
        hire = view.findViewById(R.id.hire);


        back_btn = view.findViewById(R.id.back_btn);
        Glide.with(getContext()).load(eimage).into(workerimage);
        workername.setText(ename);
        workerjob.setText(ejob);
        workeraddress.setText(eaddress);
        workerabout.setText(eabout);
        workerphonenumber.setText(ephone);
        workermail.setText(eemail);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                startActivity(intent);

            }
        });

        setupOnBackPressed();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = ephone;
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = ephone;
                try {
                    String trimToNumner = "91" + number; //10 digit number
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://wa.me/" + trimToNumner + "/?text=" + ""));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotificationChannel();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                        .setContentTitle(ename)
                        .setContentText("Thanks for using Nearby workers. Your Request for the service ("+ ejob +") is successfully registered. For any further assistance please contact us on nearbyworkers@gmail.com")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions

                    return;
                }
                notificationManager.notify(NOTIFICATION_ID, builder.build());

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        LatLng location = new LatLng(Longitude, Latitude); // Replace with your desired location coordinates
        googleMap.addMarker(new MarkerOptions().position(location).title("address"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
    private void setupOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback((new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(isEnabled()){
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }

            }
        }));
    }
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Notification";
            String description = "Simple Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
}






