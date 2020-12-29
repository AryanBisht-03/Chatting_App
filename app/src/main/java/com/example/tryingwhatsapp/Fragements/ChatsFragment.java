package com.example.tryingwhatsapp.Fragements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tryingwhatsapp.Adapter.UsersAdapter;
import com.example.tryingwhatsapp.Models.UserModel;
import com.example.tryingwhatsapp.R;
import com.example.tryingwhatsapp.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {


    public ChatsFragment() {
        // Required empty public constructor
    }

    ArrayList<UserModel> list =  new ArrayList<>();
    FirebaseDatabase database;
    FragmentChatsBinding binding;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);

        UsersAdapter adapter = new UsersAdapter(list,getContext());

        binding.ChatRecyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.ChatRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    user.setUserId(dataSnapshot.getKey());

                    Log.d("Aryan",dataSnapshot.getKey());
                    Log.d("Aryan","auth1 " + auth.getUid());
//                    Log.d("Aryan","auth2 "+ auth.getCurrentUser().toString() );

                    if(dataSnapshot.getKey().equals(auth.getUid()))
                    {
                        Log.d("Aryan","Inside same user");
                    }
                    else
                        list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}