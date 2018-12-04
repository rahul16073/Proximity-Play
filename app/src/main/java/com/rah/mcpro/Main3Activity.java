package com.rah.mcpro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    Double thislatitude,thislongitude;
    List<Room> list,p;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thislatitude=Double.parseDouble(getIntent().getStringExtra("latitude"));
        thislongitude=Double.parseDouble(getIntent().getStringExtra("longitude"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        firebaseDatabase =FirebaseDatabase.getInstance();
        list=new ArrayList<>();
        p=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recycle);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    protected void onStart() {
        super.onStart();

        database d=new database(this);
        user=d.select(0);
        DatabaseReference myRef = firebaseDatabase.getReference("Rooms");
        internet();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //int i=0;
                list.clear();
                p.clear();
                for (DataSnapshot RoomSnapshot: dataSnapshot.getChildren()){
                    Room room=RoomSnapshot.getValue(Room.class);
                    //Toast.makeText(Main2Activity.this,room.groundName,Toast.LENGTH_SHORT).show();
                    list.add(room);
                    // i++;
                }
                //Log.i("fuck",""+i);
                //adapter.addAll(list);
                adapter adapter=new adapter(Main3Activity.this);
                for(int i=0;i<list.size();i++){
                    String[] pls=list.get(i).players.split(" ");
                    int fg=0;
                    for(int j=0;j<pls.length;j++){
                        if(pls[j].equals(user))
                            fg=1;
                    }
                    if(fg==1) {
                        list.get(i).setDist_in_km(thislatitude, thislongitude);
                        p.add(list.get(i));
                    }
                    else{
                        list.remove(i);
                        i--;
                    }
                }
                adapter.addAll(p);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Main3Activity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean internet(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }
        else {
            Toast.makeText(Main3Activity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}