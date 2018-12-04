package com.rah.mcpro;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.snapshot.Snapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.viewholder> implements dialog.dialoglistener {

    List<ChatMessage> list;
    Context context;
    float rating;
    FirebaseDatabase firebaseDatabase;

    public AdapterMessage(Context context) {
        this.context = context;
        firebaseDatabase=FirebaseDatabase.getInstance();
        list = new ArrayList<>();
    }


    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(viewType==0)
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message2, parent, false);
        return new viewholder(itemView);
    }
    @Override
    public int getItemViewType(int position)
    {
        database db = new database(context);
        String currentuser = db.select(0);
        if(list.get(position).messageUser.equals(currentuser))
            return 1;
        return 0;
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
        Log.i("Kshitxsxij",position+"");
        final ChatMessage chatMessage = list.get(position);
        holder.message.setText(chatMessage.getMessageText());
        holder.from.setText(chatMessage.getMessageUser());
        final String username = holder.from.getText().toString();

//        holder.textView4.setText(room.getPub());
        if(holder.getItemViewType()==0)
        {
            holder.from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,view_profile.class);
                    intent.putExtra("username",username);
                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<ChatMessage> messages) {
//        list.beginBatchedUpdates();
        list.addAll(messages);
        Log.i("kshitij", list.size() + "");
//        Toast.makeText(context, list.size(),Toast.LENGTH_SHORT).show();
//        list.endBatchedUpdates();
    }

    class viewholder extends RecyclerView.ViewHolder{
//         button;
        TextView from,message,date;
//        CardView cardView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
//            button=itemView.findViewById(R.id.join);
            from=itemView.findViewById(R.id.from);
            message=itemView.findViewById(R.id.message);
//            date=itemView.findViewById(R.id.);
//            cardView=itemView.findViewById(R.id.card);
        }
    }

    @Override
    public void applyTexts(float pass) {
        rating=pass;
    }
}
