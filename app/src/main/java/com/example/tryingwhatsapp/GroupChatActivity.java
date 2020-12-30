package com.example.tryingwhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tryingwhatsapp.Adapter.ChatAdapter;
import com.example.tryingwhatsapp.Models.MessagesModel;
import com.example.tryingwhatsapp.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;
    FirebaseDatabase database;
    ChatAdapter adapter;
    ArrayList<MessagesModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();

        adapter = new ChatAdapter(list, this);
        binding.RecyclerViewGroup.setAdapter(adapter);

        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.userNameChattingGroup.setText("Let's Talk Together");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.RecyclerViewGroup.setLayoutManager(layoutManager);

        binding.VideoCallGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupChatActivity.this, "Soon This Feature will come.So wait till then :-)", Toast.LENGTH_SHORT).show();
            }
        });

        binding.SimpleCallGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupChatActivity.this, "Soon This Feature will come.So wait till then :-)", Toast.LENGTH_SHORT).show();
            }
        });

        binding.OptionChattingGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupChatActivity.this, "Soon This Feature will come.So wait till then :-)", Toast.LENGTH_SHORT).show();
            }
        });
        binding.sendGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.messageBoxGroup.getText().toString();

                if(TextUtils.isEmpty(message))
                {

                }
                else
                {
                    final MessagesModel model = new MessagesModel(senderId,message);
                    model.setTime(new Date().getTime());

                    binding.messageBoxGroup.setText("");
                    database.getReference().child("GroupChat").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Aryan","Group Chats are stored in FireBase");

                        }
                    });

                }
            }
        });

        database.getReference().child("GroupChat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    MessagesModel model = dataSnapshot.getValue(MessagesModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.BackBtnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(GroupChatActivity.this, MainActivity.class));
            }
        });
    }
}