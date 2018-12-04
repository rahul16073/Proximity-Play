package com.rah.mcpro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class view_profile extends AppCompatActivity implements dialog.dialoglistener{
    FirebaseDatabase firebaseDatabase;
    TextView textView5,textView6,textView7;
    Button rate,report;
    Boolean bool=true;
    float rating,prevrate,currrate=0;
    ratings rr = null;
    String tid,x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        firebaseDatabase=FirebaseDatabase.getInstance();
        textView5=(TextView) findViewById(R.id.textView5);
        textView6=(TextView)findViewById(R.id.textView6) ;
        textView7=(TextView)findViewById(R.id.textView7);
        rate=(Button)findViewById(R.id.rate);
        report=(Button) findViewById(R.id.report);
        Typeface tf1;
        tf1 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Quicksand-Bold.otf");
        textView5.setTypeface(tf1);
        textView6.setTypeface(tf1);
        textView7.setTypeface(tf1);
        Intent intent=getIntent();
        final String username=intent.getStringExtra("username");
//        Toast.makeText(getApplicationContext(),username+"",Toast.LENGTH_SHORT).show();
        DatabaseReference myref=firebaseDatabase.getReference("users");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot usershot:dataSnapshot.getChildren()){
                    users user=usershot.getValue(users.class);
                    if(user.username.equals(username)){
                        textView5.setText(username);
                        textView6.setText(user.phone);
                        textView7.setText(user.rating);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final Context con = this;
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dil  = new Dialog(con);
                dil.setContentView(R.layout.rate_user);

                dil.setCancelable(true);
                dil.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final RatingBar rate = dil.findViewById(R.id.ratingBar);
                Button button = (Button) dil.findViewById(R.id.button4);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        System.out.print("yayayyayayayayyayayayayya");
                        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        final DatabaseReference reference=firebaseDatabase.getReference("users");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            String p;
                            users user;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot usershot:dataSnapshot.getChildren()){
                                    user=usershot.getValue(users.class);
                                    if(username.equals(user.username)){
                                        p=user.rating;
                                        break;
                                    }
                                }
                                x = "not rated";
                                rating = rate.getRating();
//                                System.out.print(p+" alphabeta "+bool+" "+rating);
                                if(p.equals("not rated")){
                                    x ="Average Rating: "+rating+" Rated By: "+1+" User";
                                    DatabaseReference ref=firebaseDatabase.getReference("ratings");
                                    database d=new database(getApplicationContext());
                                    String critic=d.select(0);
                                    String id=ref.push().getKey();
                                    ratings rat=new ratings(critic,username,Float.toString(rate.getRating()),id);
                                    ref.child(id).setValue(rat);
                                    currrate=rating;
//                                    Toast.makeText(getApplicationContext(),"dddddddddddd",Toast.LENGTH_SHORT).show();
//                                    System.out.print("ddddddddddddddddd");
                                    users user1=new users(user.username,user.password,user.phone,user.blocked, x);
                                    reference.child(user.username).setValue(user1);


                                }
                                else{
                                    prevrate=0;
                                    database d=new database(getApplicationContext());
                                    final String critic=d.select(0);
                                    final DatabaseReference ref=firebaseDatabase.getReference("ratings");
                                    final Boolean[] done = {false};
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot usershot:dataSnapshot.getChildren()){
                                                rr=usershot.getValue(ratings.class);
                                                tid = usershot.getKey();
//                                                Toast.makeText(getApplicationContext(),"e"+username+" "+rr.user,Toast.LENGTH_SHORT).show();
                                                if((username.equals(rr.user))&&(critic.equals(rr.critic))){
                                                    prevrate=Float.parseFloat(rr.rate);
//                                                    Toast.makeText(getApplicationContext(),"ALpha"+prevrate,Toast.LENGTH_SHORT).show();
                                                    break;
                                                }
                                            }
//                                                Toast.makeText(getApplicationContext(),""+prevrate,Toast.LENGTH_SHORT).show();
                                                /*if(done[0])
                                                    Toast.makeText(getApplicationContext(),"zzzzzzzzzzzzzzzzzzzz",Toast.LENGTH_SHORT).show();
                                                else */if(prevrate==0){
//                                                    Toast.makeText(getApplicationContext(),"sssssssssssss",Toast.LENGTH_SHORT).show();
//                                                    System.out.print("sssssssssssssssssssssssssss");
                                                    String[] y=p.split(" ");
                                                    float  r=Float.parseFloat(y[2]);
                                                    int u=Integer.parseInt(y[5]);
                                                    rating=((r*u)+rate.getRating())/(u+1);
                                                    x ="Average Rating: "+rating+" Rated By: "+(u+1)+" User";
                                                    String id=ref.push().getKey();
                                                    ratings rat=new ratings(critic,username,Float.toString(rate.getRating()),id);
                                                    ref.child(id).setValue(rat);
                                                    currrate=rating;
                                                }
                                                else{
//                                                    Toast.makeText(getApplicationContext(),"dddddddddddd222222222",Toast.LENGTH_SHORT).show();
//                                                    System.out.print("kkkkkkkkkkk");
                                                    String[] y=p.split(" ");
                                                    float  r=Float.parseFloat(y[2]);
                                                    int u=Integer.parseInt(y[5]);
                                                    rating=((r*u)+rate.getRating()-prevrate)/u;
                                                    x ="Average Rating: "+rating+" Rated By: "+(u)+" User";
//                                        String id=ref.push().getKey();
                                                    ratings rat=new ratings(critic,username,Float.toString(rate.getRating()),tid);
                                                    ref.child(tid).setValue(rat);
                                                    currrate=rating;
                                                }
                                            users user1=new users(user.username,user.password,user.phone,user.blocked, x);
                                            reference.child(user.username).setValue(user1);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }

                                    });

                                }

                                bool=false;
//                                textView7.setText(x[0]);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        dil.dismiss();
                    }
                });
                dil.show();
//                opendialog();
            }
        });
    }
    public void opendialog(){
        dialog d=new dialog();
        d.show(((AppCompatActivity)this).getSupportFragmentManager(),"Enter Room Password");
    }
    @Override
    public void applyTexts(float pass) {
            rating=pass;
        }
}
