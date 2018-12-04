package com.rah.mcpro;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Chatrooms extends AppCompatActivity {
    String id;
    Room room;
    Button EXIT;
    EditText message;
    Button send;
    database d;
    Context context;
    FirebaseDatabase firebaseDatabase;
    ChatMessage chatMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms);
        id = getIntent().getStringExtra("roomId");
        firebaseDatabase = FirebaseDatabase.getInstance();
        EXIT=(Button)findViewById(R.id.profile);
        EXIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database d=new database(getApplicationContext());
                final String user=d.select(0);

                final DatabaseReference ref=firebaseDatabase.getReference("Rooms");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot rooms:dataSnapshot.getChildren()){
                            room=rooms.getValue(Room.class);
                            if(room.getId().equals(id)) {
                                System.out.println("\n"+room.getAdmin()+user);
                                if (room.getAdmin().equals(user)) {
                                    ref.child(id).removeValue();
                                } else {
                                    String x= room.getPlayers();
                                    System.out.println(x+" yyyyy ");
                                    int curr=Integer.parseInt(room.getCurrent());
                                    curr--;
                                    String xx[]=x.split(" ");
                                    String ans="";
                                    for(int i=0;i<xx.length;i++){
                                        if(!user.equals(xx[i])){
                                            ans+=xx[i]+" ";
                                        }

                                    }
                                    ans=ans.substring(0,ans.length()-1);
                                    System.out.println(ans+" xxxxxxxx ");
                                    DatabaseReference ree=firebaseDatabase.getReference("Rooms");
                                    Room r=new Room(room.roomId,room.groundName, room.sport,room.password,room.pub,ans,
                                            room.latitude,room.longitude,room.admin,room.max,Integer.toString(curr));
                                    r.setPlayers(ans);
                                    r.setCurrent(Integer.toString(curr));
                                    System.out.println("DDD\ndddd "+r.getPlayers()+" "+curr);
                                    ref.child(id).removeValue();
                                    ref.child(id).setValue(r);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent=new Intent(Chatrooms.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });


        final String id = getIntent().getStringExtra("roomId");

        Bundle bundle = new Bundle();
        bundle.putString("roomId", id);



        FragmentManager fm=getSupportFragmentManager();
        FragmentMessageList b=new FragmentMessageList();
        b.setArguments(bundle);
        FragmentTransaction ft=fm.beginTransaction();
        ft.add(R.id.fragment2,b);
        ft.commit();

        message = findViewById(R.id.message);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageString = message.getText().toString();
                database d = new database(getApplicationContext());
                String username = d.select(0);

//                final String pos = getIntent().getStringExtra("positionRoom");
//                int position = Integer.parseInt(pos);
//                Toast.makeText(Chatrooms.this, pos, Toast.LENGTH_SHORT).show();
                ChatMessage newMessage = new ChatMessage(messageString, username, id);
                final DatabaseReference myRef = firebaseDatabase.getReference("ChatMessages");
                String id1 = myRef.push().getKey();
                myRef.child(id1).setValue(newMessage);
                message.setText("");
            }
        });

    }

//    private void firecreatechatmessage(String ground, String d,String pass,String pub){
//        room=new Room(ground,d,pass,pub);
//        myRef.child(id).setValue(room);
////        Intent intent=new Intent(Chatrooms.this, Main2Activity.class);
////        startActivity(intent);
//        finish();
//    }


}
