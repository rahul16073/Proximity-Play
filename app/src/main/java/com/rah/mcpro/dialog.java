package com.rah.mcpro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class dialog extends AppCompatDialogFragment  {

    RatingBar ratingBar;
    private dialoglistener listener;

    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.rate_user,null);
        builder.setView(view);
        builder.setMessage("")
                .setTitle("Rate User")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        float rating=(float) ratingBar.getRating();
                        if(listener!=null) {
                            listener.applyTexts(rating);
                        }
                        //Toast.makeText(getActivity(),pass,Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (dialoglistener) context;

        }
        catch (ClassCastException e){
            //throw new ClassCastException(context.toString()+"implement dialoglistener");
        }
    }

    public interface dialoglistener{
        void applyTexts(float pass);
    }
}
