package com.rah.mcpro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.AuthProvider;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static android.support.constraint.Constraints.TAG;


public class BlankFragment2 extends Fragment {
    int type=0;
    private FirebaseAuth mAuth;
    private String mS;
    private PhoneAuthProvider.ForceResendingToken mforceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    EditText rusername,rpassword,rphone,otp;
    FirebaseDatabase firebaseDatabase;
    Button register;


    public BlankFragment2() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_blank_fragment2, container, false);
        otp=(EditText)view.findViewById(R.id.otp);
        otp.setVisibility(View.INVISIBLE);
        rusername=(EditText)view.findViewById(R.id.rusername);
        rpassword=(EditText) view.findViewById(R.id.rpassword);
        rphone=(EditText) view.findViewById(R.id.rphone);
        firebaseDatabase =FirebaseDatabase.getInstance();
        register=(Button)view.findViewById(R.id.rregister);


        Typeface tf1;
        tf1 = Typeface.createFromAsset(getActivity().getAssets(), "Quicksand-Bold.otf");
        rusername.setTypeface(tf1);
        rpassword.setTypeface(tf1);
        rphone.setTypeface(tf1);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user=rusername.getText().toString();
                final String pass=rpassword.getText().toString();
                final String phone=rphone.getText().toString();
                if(user.length()<5){
                    Toast.makeText(getActivity(),"username length should be greater than 4",Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<8){
                    Toast.makeText(getActivity(),"password length should be greater than 7",Toast.LENGTH_SHORT).show();
                }
               else if(phone.length()<10){
                    Toast.makeText(getActivity(),"phone no. should contain greater than or equal to 10 digits",Toast.LENGTH_SHORT).show();
                }
                else {
                    //add(database,user, pass, phone, "false");

                    if(internet()==true) {
                            //if(type==0) {
                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        phone,
                                        60,
                                        TimeUnit.SECONDS,
                                        getActivity(),
                                        mCallbacks);
                            //}
                            /*else{
                                String OTP=otp.getText().toString();
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mS, OTP);
                                signInWithPhoneAuthCredential(credential);
                            }*/

                    }

                }



            }

        }
        );

        class async extends AsyncTask<Void,Void,Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                String ruser=rusername.getText().toString();
                String rpass=rpassword.getText().toString();
                String phone=rphone.getText().toString();
                fireadd(ruser,rpass,phone,"false");
                return null;
            }
        }
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                //otp.setVisibility(View.VISIBLE);
                new async().doInBackground();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                register.setEnabled(true);
                rphone.setEnabled(true);
                rpassword.setEnabled(true);
                rusername.setEnabled(true);
                register.setText("REGISTER");
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                Toast.makeText(getActivity(),"OTP timeout",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mS=s;
                type=1;
                Toast.makeText(getActivity(),"OTP sent",Toast.LENGTH_SHORT).show();
                register.setText("OTP verification in process..");
                register.setEnabled(false);
                rphone.setEnabled(false);
                rpassword.setEnabled(false);
                rusername.setEnabled(false);
                //register.setText("verify OTP");
            }
        };
        return view;
    }

    private void fireadd(String user, String password,String phone, String blocked){
        DatabaseReference myRef = firebaseDatabase.getReference("users");
        final String use=user;
        final String pass=password;
        final String phon=phone;
        final String block=blocked;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String u=use;
                int a=0,b=0;
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    users userr=userSnapshot.getValue(users.class);
                    if(userr.username.equals(u)){
                        a=1;
                        Toast.makeText(getActivity(),"username exists:Enter new username",Toast.LENGTH_SHORT).show();
                        b=1;
                        register.setText("Register with new username");
                        register.setEnabled(true);
                        rphone.setEnabled(true);
                        rpassword.setEnabled(true);
                        rusername.setEnabled(true);
                        break;
                    }

                }
                if(a==0 && b==0){
                    register(use,pass,phon,block);

                    Intent intent = new Intent(getActivity(), Main2Activity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Error in reading database",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void register(String user,String password,String phone,String blocked){
        DatabaseReference myRef = firebaseDatabase.getReference("users");
        //String id = myRef.push().getKey();
        users userr = new users( user, password, phone, blocked,"not rated");
        myRef.child(user).setValue(userr);
        database d=new database(getActivity());
        d.add(userr.username,userr.phone);
        d.update("1",userr.username,userr.phone);
        Toast.makeText(getActivity(),"You are registered",Toast.LENGTH_SHORT).show();
    }

    public boolean internet(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }
        else {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    /*private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            class async extends AsyncTask<Void,Void,Void> {
                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                }

                                @Override
                                protected Void doInBackground(Void... voids) {

                                    String ruser=rusername.getText().toString();
                                    String rpass=rpassword.getText().toString();
                                    String phone=rphone.getText().toString();
                                    fireadd(ruser,rpass,phone,"false");
                                    return null;
                                }
                            }
                            new async().doInBackground();

                        }
                        else {
                            // Sig n in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getActivity(),"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }*/
}
