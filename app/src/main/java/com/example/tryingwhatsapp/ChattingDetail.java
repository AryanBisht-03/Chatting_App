package com.example.tryingwhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.tryingwhatsapp.Adapter.ChatAdapter;
import com.example.tryingwhatsapp.Models.MessagesModel;
import com.example.tryingwhatsapp.databinding.ActivityChattingDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChattingDetail extends AppCompatActivity {

    ActivityChattingDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChattingDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String recieveId = getIntent().getStringExtra("UserId");
        String userName = getIntent().getStringExtra("UserName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userNameChatting.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_man).into(binding.profileImageChatting);

        binding.BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChattingDetail.this,MainActivity.class));

            }
        });

        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messagesModels,this);
        binding.RecyclerViewChatting.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.RecyclerViewChatting.setLayoutManager(layoutManager);
        final String senderRoom = senderId + recieveId;
        final String receiverRoom = recieveId + senderId;


        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    MessagesModel model = dataSnapshot.getValue(MessagesModel.class);

                    Log.d("Times",model.getTime().toString());
                    messagesModels.add(model);

                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = binding.messageBox.getText().toString();
                if(TextUtils.isEmpty(message)){
                    Log.d("Aryan","Empty String");
                }
                else {
                    final MessagesModel model = new MessagesModel(senderId,message);

                    model.setTime(new Date().getTime());
//                    Log.d("Aryan",model.getTime().toString());

                    binding.messageBox.setText("");

                    database.getReference().child("Chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            database.getReference().child("Chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d("Aryan","Database values are sets..");
                                }
                            });
                        }
                    });

                }
            }
        });




    }
}