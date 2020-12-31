package com.example.tryingwhatsapp.Fragements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tryingwhatsapp.Adapter.StatusAdapter;
import com.example.tryingwhatsapp.Models.StatusModel;
import com.example.tryingwhatsapp.Models.UserModel;
import com.example.tryingwhatsapp.R;
import com.example.tryingwhatsapp.databinding.FragmentStatusFragmentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class StatusFragments extends Fragment {


    public StatusFragments() {
        // Required empty public constructor
    }

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ArrayList<StatusModel> list = new ArrayList<>();
    FragmentStatusFragmentsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatusFragmentsBinding.inflate(inflater,container,false);

        StatusAdapter adapter = new StatusAdapter(list,getContext());

        binding.recyclerViewStatus.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewStatus.setLayoutManager(layoutManager);

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel model = snapshot.getValue(UserModel.class);
                binding.userStatus.setText(model.getStatus());

                Picasso.get().load(model.getProfilpic()).placeholder(R.drawable.ic_man).into(binding.senderImageStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    StatusModel model = dataSnapshot.getValue(StatusModel.class);

                    if(!dataSnapshot.getKey().equals(auth.getUid()))
                    {
                        list.add(model);
                    }
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