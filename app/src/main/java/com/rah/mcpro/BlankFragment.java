package com.rah.mcpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BlankFragment extends Fragment {

    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    EditText username,password;
    FirebaseDatabase firebaseDatabase;
    ImageButton login;
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;
    public BlankFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_blank, container, false);
        username=(EditText)view.findViewById(R.id.rusername);
        password=(EditText)view.findViewById(R.id.password);
        login=(ImageButton)view.findViewById(R.id.submit);
        firebaseDatabase =FirebaseDatabase.getInstance();

        Typeface tf1;
        tf1 = Typeface.createFromAsset(getActivity().getAssets(), "Quicksand-Bold.otf");
        username.setTypeface(tf1);
        password.setTypeface(tf1);
        saveLoginCheckBox = (CheckBox)view.findViewById(R.id.saveLoginCheckBox);
        /*loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        final String user=username.getText().toString();
                        final String pass=password.getText().toString();
                        class async extends AsyncTask<Void,Void,Void> {
                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                            }

                            @Override
                            protected Void doInBackground(Void... voids) {
                                firelogin(user,pass);
                                return null;
                            }
                        }
                        if(internet()==true){
                            new async().doInBackground();
                            login.setEnabled(false);
                            username.setEnabled(false);
                            password.setEnabled(false);
                        }
            }
        });

        Button register=(Button)view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm=getFragmentManager();
                Fragment fragment=fm.findFragmentById(R.id.fragment);
                FragmentTransaction ft=fm.beginTransaction();
                BlankFragment2 blankFragment2=new BlankFragment2();
                ft.addToBackStack(null);
                ft.replace(R.id.fragment,blankFragment2);
                ft.commit();
            }
        });
        return view;
    }
    public void firelogin(final String user,final String pass){
        DatabaseReference myRef = firebaseDatabase.getReference("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int u=0,p=0;
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    users userr=userSnapshot.getValue(users.class);
                    if(userr.username.equals(user)){u=1;
                        if(userr.password.equals(pass)){p=1;
                        database d=new database(getActivity());
                        d.add(userr.username,userr.phone);
                        d.update("1",userr.username,userr.phone);
                            Intent intent = new Intent(getActivity(), Main2Activity.class);
                            String username = userr.username;
                            intent.putExtra("username", username);
                            startActivity(intent);
                            Toast.makeText(getActivity(),"Login Successful",Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                        break;
                    }
                }
                if(u==0 && p==0){
                    Toast.makeText(getActivity(),"username doesn't exist",Toast.LENGTH_SHORT).show();
                    login.setEnabled(true);
                    username.setEnabled(true);
                    password.setEnabled(true);
                }
                else if(u==1 && p==0){
                    Toast.makeText(getActivity(),"Wrong Password",Toast.LENGTH_SHORT).show();
                    login.setEnabled(true);
                    username.setEnabled(true);
                    password.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                   Toast.makeText(getActivity(),"Error reading Database",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        if(saveLoginCheckBox.isChecked())
           savePreferences();

    }
    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }
    private void savePreferences() {
        SharedPreferences settings =
                getContext().getSharedPreferences(PREFS_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = username.getText().toString();
        PasswordValue = password.getText().toString();
        System.out.println("onPause save name: " + UnameValue);
        System.out.println("onPause save password: " + PasswordValue);
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.commit();
    }
    private void loadPreferences() {

        SharedPreferences settings =
                getContext().getSharedPreferences(PREFS_NAME,
                        Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        username.setText(UnameValue);
        password.setText(PasswordValue);
        System.out.println("onResume load name: " + UnameValue);
        System.out.println("onResume load password: " + PasswordValue);
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

}

