package com.example.tryingwhatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tryingwhatsapp.Models.UserModel;
import com.example.tryingwhatsapp.databinding.ActivitySettingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("Aryan","Inside setting");

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

//        Log.d("Aryan","Setting everything");
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Uploading Image");
//        Log.d("Aryan","Title is set");
        progressDialog.setMessage("Brother please wait uploading your image take time and quality of image is high so wait otherwise I will kick your ass");

//        Log.d("Aryan","progressDialog is finished");
        binding.addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);

            }
        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel users = snapshot.getValue(UserModel.class);

                Picasso.get().load(users.getProfilpic()).placeholder(R.drawable.ic_man).into(binding.userImageSetting);

                binding.statusSetting.setText(users.getStatus());
                binding.userNameSetting.setText(users.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.backBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,MainActivity.class));
            }
        });

        binding.aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "Soon This Feature will come.So wait till then :-)", Toast.LENGTH_SHORT).show();
            }
        });

        binding.inviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "Soon This Feature will come.So wait till then :-)", Toast.LENGTH_SHORT).show();
            }
        });

        binding.policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "Soon This Feature will come.So wait till then :-)", Toast.LENGTH_SHORT).show();
            }
        });

        binding.needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "Soon This Feature will come.So wait till then :-)", Toast.LENGTH_SHORT).show();
            }
        });

        binding.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "Soon This Feature will come.So wait till then :-)", Toast.LENGTH_SHORT).show();
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = binding.userNameSetting.getText().toString();
                String status = binding.statusSetting.getText().toString();
                if(TextUtils.isEmpty(username))
                {
                    Toast.makeText(SettingActivity.this, "Please Type your name first", Toast.LENGTH_SHORT).show();
                }
                else{
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("userName",username);
                    map.put("status",status);

                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(map);
                    Toast.makeText(SettingActivity.this, "I got it...:_-)", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null)
        {
            progressDialog.show();
            Uri sFile = data.getData();
            binding.userImageSetting.setImageURI(sFile);

            final StorageReference reference = storage.getReference().child("profilpic").child(FirebaseAuth.getInstance().getUid());
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("profilpic").setValue(uri.toString());

                        Toast.makeText(SettingActivity.this, "I got your photo :-)", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        }
                    });
                }
            });
        }
    }
    }