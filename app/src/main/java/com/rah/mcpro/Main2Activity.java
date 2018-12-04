package com.rah.mcpro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements LocationListener{
    static double thislatitude=-1, thislongitude=-1;
    RecyclerView recyclerView;
    Button createchatroom,rooms,profil;
    List<Room> list;
    Location loc;
    LocationManager locationManager;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    String ground,pub="p",pass;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseDatabase =FirebaseDatabase.getInstance();
        createchatroom= (Button)findViewById(R.id.button);
        while (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        list=new ArrayList<>();
        Location p=getLocation();
        if(p==null){
            thislatitude = 28.543641666666666;
            thislongitude = 77.27252833333334;
        }
        else {
            thislatitude = p.getLatitude();
            thislongitude = p.getLongitude();
//            System.out.println(thislatitude+" : "+thislongitude);
        }
        recyclerView=(RecyclerView)findViewById(R.id.recycle);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        createchatroom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*if(internet()) {
                        opendialog();
                        if(!pub.equals("p") && !ground.equals("")) {
                            fireacreaterooms(ground, "distance", pass, pub);
                        }
                    }*/

//                    String message_id = getIntent().getStringExtra("message_id");
//                    Log.i("message_id", message_id);
//                    Toast.makeText(Main2Activity.this, message_id, Toast.LENGTH_LONG).show();
                   Intent intent=new Intent(Main2Activity.this,creatroom.class);
                   intent.putExtra("latitude",Double.toString(thislatitude));
                   intent.putExtra("longitude",Double.toString(thislongitude));
                   startActivity(intent);
//                   finish();
                }
        });
        rooms=(Button)findViewById(R.id.rooms);
        profil=(Button)findViewById(R.id.pro);
        rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
                intent.putExtra("latitude",Double.toString(thislatitude));
                intent.putExtra("longitude",Double.toString(thislongitude));
                startActivity(intent);

            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,Profile.class);
                startActivity(intent);
            }
        });


          ///float x=0;
          //Location targetLocation = new Location("Lotus");//provider name is unnecessary
//        targetLocation.setLatitude(28.553325);//your coords of course
//        targetLocation.setLongitude(28.553325);
         // x=targetLocation.distanceTo(getLocation());
//        Room r=new Room("ground7", x);
//        r.setLocation(targetLocation);
//        list.add(r);

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference myRef = firebaseDatabase.getReference("Rooms");
        internet();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //int i=0;
                list.clear();
                for (DataSnapshot RoomSnapshot: dataSnapshot.getChildren()){
                    Room room=RoomSnapshot.getValue(Room.class);
                    //Toast.makeText(Main2Activity.this,room.groundName,Toast.LENGTH_SHORT).show();
                    list.add(room);
                   // i++;
                }
                //Log.i("fuck",""+i);
                //adapter.addAll(list);
                for(int i=0;i<list.size();i++){

                    list.get(i).setDist_in_km(thislatitude,thislongitude);
                }
                adapter adapter=new adapter(Main2Activity.this);
                adapter.addAll(list);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Main2Activity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

      }

    Location getLocation() {

        try {
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

            // get GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // get network provider status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(this, "No Service Provider is available", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS) {

                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                            thislatitude = loc.getLatitude();
                            thislongitude = loc.getLongitude();
                        }
                    }


                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
    }

//    void getLocation() {
//        try {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
//        }
//        catch(SecurityException e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public void onLocationChanged(Location location) {
        thislatitude=location.getLatitude();
        thislongitude=location.getLongitude();
//        Toast.makeText(Main2Activity.this, "Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
//        thislocation=location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(Main2Activity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }

    /*private void fireacreaterooms(final String ground, final String d,String pass,String pub){
        final DatabaseReference myRef = firebaseDatabase.getReference("Rooms");
        String id = myRef.push().getKey();
        Room room=new Room(ground,d,pass,pub);
        myRef.child(id).setValue(room);
    }*/
    /*private void fireaddrooms(){
        final DatabaseReference myRef = firebaseDatabase.getReference("Rooms");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                list.clear();
                for (DataSnapshot RoomSnapshot: dataSnapshot.getChildren()){
                    Room room=RoomSnapshot.getValue(Room.class);
                    Toast.makeText(Main2Activity.this,room.groundName,Toast.LENGTH_SHORT).show();
                    list.add(room);
                    i++;
                }
                Log.i("fuck",""+i);
                adapter.addAll(list);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Main2Activity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }*/
    public boolean internet(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }
        else {
            Toast.makeText(Main2Activity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
   /*public void opendialog(){
        dialog d=new dialog();
        d.show(getSupportFragmentManager(),"create room");
    }
     @Override
    public void applyTexts(String a,String b,String c){
        pub=a;
        ground=b;
        pass=c;
    }*/

}
