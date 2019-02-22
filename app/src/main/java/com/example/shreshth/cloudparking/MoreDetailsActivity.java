package com.example.shreshth.cloudparking;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MoreDetailsActivity extends AppCompatActivity {

    FirebaseFirestore db;
    ImageButton navigateButton;
    Button parkingBtn;
    Switch aSwitch;
    TextView naviagteTo;
    TextView peopleApproaching;
    TextView occupied;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");
        db = FirebaseFirestore.getInstance();
        parkingBtn = (Button) findViewById(R.id.parking_btn);
        aSwitch = (Switch) findViewById(R.id.aSwitch);
        final DocumentReference docRef = db.collection("sensors").document(id);
        navigateButton = (ImageButton) findViewById(R.id.navigate_btn);
        naviagteTo = (TextView) findViewById(R.id.navigate_text);
        peopleApproaching = (TextView) findViewById(R.id.people_appro);
        occupied = (TextView) findViewById(R.id.occupied);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("testing", "Listen failed.", e);
                    return;
                }

                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (snapshot != null && snapshot.exists()) {
                    Log.d("testing", source + " data: " + snapshot.getData());
                    final GeoPoint geoPoint = snapshot.getGeoPoint("geopoint");
                    final String title = snapshot.getString("title");
                    final Number approaching = snapshot.getDouble("approaching");
                    int percentage=(approaching.intValue()/8)*100;
                    parkingBtn.setText(title);
                    peopleApproaching.setText(String.valueOf(approaching.intValue()));
                    occupied.setText(String.valueOf(snapshot.getDouble("priority").intValue()));
                    aSwitch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (aSwitch.isChecked()) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("approaching", approaching.intValue() + 1);

                                docRef.set(data, SetOptions.merge());
                                navigateButton.setVisibility(View.VISIBLE);
                                naviagteTo.setVisibility(View.VISIBLE);

                            } else {
                                Map<String, Object> data = new HashMap<>();
                                data.put("approaching", approaching.intValue() - 1);
                                navigateButton.setVisibility(View.INVISIBLE);
                                naviagteTo.setVisibility(View.INVISIBLE);

                                docRef.set(data, SetOptions.merge());

                            }
                        }
                    });
                    navigateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String strUri = "http://maps.google.com/maps?q=loc:" + geoPoint.getLatitude() + "," + geoPoint.getLongitude() + " (" + title + ")";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));

                            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                            startActivity(intent);
                        }
                    });


                } else {
                    Log.d("testing", source + " data: null");
                }
            }
        });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        aSwitch = (Switch) findViewById(R.id.aSwitch);
        if (aSwitch.isChecked()) {
            Intent intent = getIntent();
            String id = intent.getExtras().getString("id");
            final DocumentReference docRef = db.collection("sensors").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        final Number approaching = document.getDouble("approaching");

                        if (document.exists()) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("approaching", approaching.intValue() - 1);

                            docRef.set(data, SetOptions.merge());
                        } else {
                            Log.d("message", "No such document");
                        }
                    } else {
                        Log.d("message", "get failed with ", task.getException());
                    }
                }
            });


        }
    }



}
