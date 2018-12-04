package com.rah.mcpro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class creatroom extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    String ground, pub = "public", pass;
    EditText editText1, editText2, sport, max;
    CheckBox checkBox;
    Button create;


    private GoogleMap mMap;
    double latitude = 0, longitude = 0;
    double[] glocation = new double[2];
    creatroom th = this;
    OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(th, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
            mMap.setMyLocationEnabled(true);
            // Add a marker in Sydney and move the camera
            LatLng zero = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zero,12));
            mMap.clear();
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {

                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                    glocation[0]=latLng.latitude;
                    glocation[1]=latLng.longitude;

                    // Clears the previously touched position
                    mMap.clear();

                    // Animating to the touched position
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Placing a marker on the touched position
                    mMap.addMarker(markerOptions);
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        latitude=Double.parseDouble(getIntent().getStringExtra("latitude"));
        longitude=Double.parseDouble(getIntent().getStringExtra("longitude"));
        System.out.println("latitude:"+latitude+" :longitude:"+longitude);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatroom);
        glocation[0]=200;
        glocation[1]=200;
        firebaseDatabase=FirebaseDatabase.getInstance();
        editText1=(EditText)findViewById(R.id.editText3);
        editText2=(EditText)findViewById(R.id.editText5);
        checkBox=(CheckBox)findViewById(R.id.checkBox2);
        sport = (EditText)findViewById(R.id.editText2);
        max = (EditText)findViewById(R.id.editText4);
        create=(Button)findViewById(R.id.button2);
        Typeface tf1;
        tf1 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Quicksand-Bold.otf");
        editText1.setTypeface(tf1);
        editText2.setTypeface(tf1);
        sport.setTypeface(tf1)
        ;
        max.setTypeface(tf1);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ground=editText1.getText().toString();
                String game = sport.getText().toString();
                String players = max.getText().toString();
                String password=editText2.getText().toString();
                if(checkBox.isChecked()){
                    if(password.length()<8){
                        Toast.makeText(getApplicationContext(),"password should consist of atleast 8 characters",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(glocation[0]==200 || glocation[1]==200){
                            Toast.makeText(getApplicationContext(),"Choose Location on Map",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            pub = "private";
                            fireacreaterooms(ground,game,  password, pub, players);
                            Toast.makeText(getApplicationContext(),"HECK",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    if(glocation[0]==200 || glocation[1]==200){
                        Toast.makeText(getApplicationContext(),"Choose Location on Map",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        fireacreaterooms(ground, game, password, pub, players);
                        Toast.makeText(getApplicationContext(),"HEC2K",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(onMapReadyCallback);

    }
    private void fireacreaterooms(final String ground, final String game, final String pass, final String pub, final String max){
        final DatabaseReference myRef = firebaseDatabase.getReference("Rooms");
        final String id = myRef.push().getKey();

        int count = 0;
        Toast.makeText(getApplicationContext(),"HEC3K"+id,Toast.LENGTH_SHORT).show();
//        myRef.addChildEventListener(new ChildEventListener() {
//
//
//
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(getApplicationContext(),"HEC4K",Toast.LENGTH_SHORT).show();

                database d = new database(getApplicationContext());
                String player = d.select(0);

//                Log.i("fuckyou", dataSnapshot.getChildrenCount() + "");
//                Toast.makeText(g, dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                Room room=new Room(id,ground,game,pass,pub, player, Double.toString(glocation[0]),Double.toString(glocation[1]),player, max, "1");
                System.out.print("CHECK:"+room);
//                Room room=new Room(id, ground,d,pass,pub);
                myRef.child(id).setValue(room);

                Intent intent=new Intent(creatroom.this,Main2Activity.class);
//        intent.putExtra("message_id", id);
                startActivity(intent);
                finish();

//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });




    }
}
