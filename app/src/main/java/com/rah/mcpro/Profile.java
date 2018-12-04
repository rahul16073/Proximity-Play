package com.rah.mcpro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    TextView pusername,p_phone;
    EditText opass,npass,cnpass;
    Button change_password,log_out;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseDatabase =FirebaseDatabase.getInstance();
//        a=(TextView)findViewById(R.id.textView8);
//        b=(TextView)findViewById(R.id.textView9);
//        c=(TextView)findViewById(R.id.textView10);
        pusername=(TextView)findViewById(R.id.pusername);
        p_phone=(TextView) findViewById(R.id.phone_no);
        opass=(EditText)findViewById(R.id.opass);
        npass=(EditText)findViewById(R.id.npass);
        cnpass=(EditText)findViewById(R.id.cnpass);
        Typeface tf1;
        tf1 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Quicksand-Bold.otf");
        pusername.setTypeface(tf1);
        p_phone.setTypeface(tf1);
        opass.setTypeface(tf1);
        npass.setTypeface(tf1);
        cnpass.setTypeface(tf1);


       /* a.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);
        c.setVisibility(View.INVISIBLE);
        opass.setVisibility(View.INVISIBLE);
        npass.setVisibility(View.INVISIBLE);
        cnpass.setVisibility(View.INVISIBLE);*/
        change_password=(Button)findViewById(R.id.change_password);
        log_out=(Button)findViewById(R.id.logout);
        final database d=new database(this);
        final String usernam=d.select(0);
        final String phone=d.selectphone(0);
        pusername.setText(usernam);
        p_phone.setText(phone);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*a.setVisibility(View.VISIBLE);
                b.setVisibility(View.VISIBLE);
                c.setVisibility(View.VISIBLE);
                opass.setVisibility(View.VISIBLE);
                npass.setVisibility(View.VISIBLE);
                cnpass.setVisibility(View.VISIBLE);
                change_password.setText("Confirm Password");*/
                if(npass.getText().toString().length()<8 && internet()){
                    Toast.makeText(Profile.this,"password length should be greater than 7",Toast.LENGTH_SHORT).show();
                }
               else if(cnpass.getText().toString().equals(npass.getText().toString()) && internet()) {
                final DatabaseReference ref = firebaseDatabase.getReference("users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int u=0,p=0;
                        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                            users userr=userSnapshot.getValue(users.class);
                            if(userr.username.equals(usernam)){u=1;
                                if(userr.password.equals(opass.getText().toString())){p=1;
                                   // Toast.makeText(getActivity(),"Login Successful",Toast.LENGTH_SHORT).show();
                                    DatabaseReference myRef = firebaseDatabase.getReference("users");
                                    users user = new users(usernam, cnpass.getText().toString(),phone,userr.blocked,userr.rating);
                                    myRef.child(usernam).setValue(user);
                                    Toast.makeText(Profile.this,"password updated",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                        if(p==0){
                            Toast.makeText(Profile.this,"Wrong password",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Profile.this,"Error reading Database",Toast.LENGTH_SHORT).show();
                    }
                });


                }
                else {
                    Toast.makeText(Profile.this,"password didn't matched",Toast.LENGTH_SHORT).show();
                }

            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.update("1","null","null");
                Intent intent=new Intent(Profile.this,MainActivity.class);
                startActivity(intent);
                finish();
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
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
