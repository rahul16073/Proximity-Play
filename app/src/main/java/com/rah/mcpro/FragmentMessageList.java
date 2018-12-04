package com.rah.mcpro;

import android.content.Context;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentMessageList extends Fragment {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    String id="0";
    List<ChatMessage> list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        list = new ArrayList<>();
        View view =  inflater.inflate(R.layout.fragment_fragment_message_list, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        recyclerView = view.findViewById(R.id.message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle bundle = getArguments();
        if(bundle!=null)
            id = bundle.getString("roomId");

//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference myRef = firebaseDatabase.getReference("ChatMessages");
//        internet();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //int i=0;
                list.clear();
                for (DataSnapshot chatSnapshot: dataSnapshot.getChildren()){
                    ChatMessage chatMessage=chatSnapshot.getValue(ChatMessage.class);

                    if(id.equals(chatMessage.getRoomId())) {
                        //Toast.makeText(Main2Activity.this,room.groundName,Toast.LENGTH_SHORT).show();
                        list.add(chatMessage);
                    }
                    // i++;
                }
                //Log.i("fuck",""+i);
                //adapter.addAll(list);
                AdapterMessage adapter=new AdapterMessage(getActivity());
                adapter.addAll(list);
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(list.size()-1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
//    public boolean internet(){
//        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
//        if(networkInfo!=null && networkInfo.isConnected()){
//            return true;
//        }
//        else {
//            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    }







}
