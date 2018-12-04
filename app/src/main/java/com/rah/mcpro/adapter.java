package com.rah.mcpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.viewholder> implements dialog.dialoglistener{
    FirebaseDatabase firebaseDatabase;
    SortedList<Room> list;
    Context context;
    public String password="";
    public Activity mcontext;
    float rating;

    public adapter(Context context) {
        this.context=context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        list = new SortedList<>(Room.class, new SortedList.Callback<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                int i= o1.getDist_in_km().compareTo(o2.getDist_in_km());
                if(i==0){
                    return -1;
                }
                else {
                    return i;
                }
            }

            @Override
            public boolean areContentsTheSame(Room oldItem, Room newItem) {
                return oldItem.getDist_in_km().equals(newItem.getDist_in_km());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areItemsTheSame(Room item1, Room item2) {
                return item1.getDist_in_km().equals(item2.getDist_in_km());
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }
    public void addAll(List<Room> rooms) {
        list.beginBatchedUpdates();
        for (int i = 0; i < rooms.size(); i++) {
            list.add(rooms.get(i));
        }
        list.endBatchedUpdates();
    }

//    public Room get(int position) {
//        return list.get(position);
//    }
//
//    public void clear() {
//        list.beginBatchedUpdates();
//        //remove items at end, to avoid unnecessary array shifting
//        while (list.size() > 0) {
//            list.removeItemAt(list.size() - 1);
//        }
//        list.endBatchedUpdates();
//    }


    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false);
        return new viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(viewholder holder, @SuppressLint("RecyclerView") final int position) {
//        Log.i("Kshisstij",position+"");
        final Room room = list.get(position);
        holder.textView2.setText(room.getDist_in_km());
        holder.textView.setText(room.getGroundName());
        holder.textView4.setText(room.getPub());
        holder.sportname.setText(room.getSport());

        Typeface tf1;
        tf1 = Typeface.createFromAsset(context.getAssets(), "Quicksand-Bold.otf");
        holder.textView.setTypeface(tf1);
        holder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("name",room.groundName);
                intent.putExtra("EXTRA_SESSION_ID", room.latitude+" "+room.longitude);
                context.startActivity(intent);
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=room.getPub();
                String players = room.getPlayers();
                database d = new database(context);
                String currentUser = d.select(0);
                if(players.contains(currentUser)){
                    fire(room.getRoomId());
                    Intent intent = new Intent(context, Chatrooms.class);
                    intent.putExtra("players", room.getPlayers());
                    intent.putExtra("roomId", room.getId());
                    context.startActivity(intent);
                }
                else{
                if(a.equals("public")) {
                    System.out.println(Integer.parseInt(room.getCurrent())+" "+Integer.parseInt(room.getMax()));

                    if(Integer.parseInt(room.getCurrent())<=Integer.parseInt(room.getMax())) {
                        System.out.print("Eror:"+room.getRoomId());
                        System.out.print("Eror:"+room);
                        fire(room.getRoomId());
                        Intent intent = new Intent(context, Chatrooms.class);
                        intent.putExtra("players", room.getPlayers());
                        intent.putExtra("roomId", room.getId());
                        context.startActivity(intent);
                    }
                    else{
                        Toast.makeText(context,"This room is completely filled",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    final Dialog dil = new Dialog(context);
                    dil.setContentView(R.layout.layout_dialog);
                    dil.setCancelable(true);
                    dil.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    final EditText edit = (EditText)
                            dil.findViewById(R.id.editText);
                    Button button = (Button) dil.findViewById(R.id.button3);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            password = edit.getText().toString();
                            if (password.equals(room.getPassword()) && Integer.parseInt(room.getCurrent()) <= Integer.parseInt(room.getMax())) {
                                fire(room.getRoomId());
                                Intent intent = new Intent(context, Chatrooms.class);
                                intent.putExtra("players", room.getPlayers());
                                intent.putExtra("roomId", room.getId());
                                context.startActivity(intent);
                            } else if (!password.equals(room.getPassword()))
                                Toast.makeText(context, room.getPassword() + " " + password, Toast.LENGTH_LONG).show();
                            else {
                                Toast.makeText(context, "This room is completely filled", Toast.LENGTH_SHORT).show();
                            }
//
//
                            dil.dismiss();
                        }
                    });
                    dil.show();

//
                }}

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        Button button,button2;
        TextView textView,textView2,textView4,sportname;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            button=itemView.findViewById(R.id.join);
            button2=itemView.findViewById(R.id.loc);
            textView=itemView.findViewById(R.id.groundname);
            textView2=itemView.findViewById(R.id.ground_distance);
            textView4=itemView.findViewById(R.id.textView4);
            sportname=itemView.findViewById(R.id.sportname);
        }
    }


    public void opendialog(){
        dialog d=new dialog();
        d.show(((AppCompatActivity)context).getSupportFragmentManager(),"Enter Room Password");
    }

    void fire(final String id){
        database d=new database(context);
        System.out.print(id);
        final String currentuser=d.select(0);
        final DatabaseReference myRef=firebaseDatabase.getReference("Rooms");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot RoomSnapshot: dataSnapshot.getChildren()){
                    Room room=RoomSnapshot.getValue(Room.class);
                    if(room.roomId.equals(id)) {
                        String id = room.roomId;
                        String ground = room.groundName;
                        String sport=room.sport;
                        String distance = room.dist_in_km;
                        String pass = room.password;
                        String pub = room.pub;
                        String players = room.players;
                        String longi = room.longitude;
                        String lat = room.latitude;
                        String admin=room.admin;
                        String max=room.max;
                        String current=room.current;
                        Boolean boo=false;
                        if(players.contains(currentuser)){
                            boo=true;
                        }
                        int a=Integer.parseInt(current);
                        if(boo.equals(false)) {
                            DatabaseReference ref=firebaseDatabase.getReference("Rooms");
                            players+=" "+currentuser;
                            a+=1;
                            String b=Integer.toString(a);
                            Room room1 = new Room(id, ground,sport,  pass, pub, players, lat, longi,admin,max,b);
                            ref.child(id).setValue(room1);
                        }
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void applyTexts(float pass) {
        rating=pass;
    }
}
