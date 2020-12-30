package com.example.tryingwhatsapp.Adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tryingwhatsapp.ChattingDetail;
import com.example.tryingwhatsapp.Models.UserModel;
import com.example.tryingwhatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{


    ArrayList<UserModel> list;
    Context context;

    public UsersAdapter(ArrayList<UserModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserModel users = list.get(position);

        Picasso.get().load(users.getProfilpic()).placeholder(R.drawable.ic_man).into(holder.image);

        holder.user.setText(users.getUserName());

        holder.lastMessage.setText("No message yet:-(");

        FirebaseDatabase.getInstance().getReference().child("Chats").child(FirebaseAuth.getInstance().getUid() + users.getUserId()).orderByChild("time").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren())
                {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        holder.lastMessage.setText(dataSnapshot.child("message").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChattingDetail.class);
                intent.putExtra("UserId",users.getUserId());
                intent.putExtra("profilePic",users.getProfilpic());
                intent.putExtra("UserName",users.getUserName());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        int s = list.size();
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView user,lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_imageChatting);
            user = itemView.findViewById(R.id.UserNameRecycler);
            lastMessage = itemView.findViewById(R.id.lastMessage);

        }
    }
}
