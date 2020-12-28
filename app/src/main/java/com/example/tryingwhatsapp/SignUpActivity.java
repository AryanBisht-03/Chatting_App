package com.example.tryingwhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tryingwhatsapp.Models.UserModel;
import com.example.tryingwhatsapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase dataBase;
    ProgressDialog progress;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance();
        progress = new ProgressDialog(SignUpActivity.this);



        progress.setTitle("Signing Up");
        progress.setMessage("User details are verifying ");

        binding.SignUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String emailTxt = binding.EmailText.getText().toString();
                String  passwordTxt = binding.PasswordText.getText().toString();
                String userNameTxt = binding.NameTxt.getText().toString();
//                Log.d("Aryan","values are "+emailTxt+" "+passwordTxt+" "+userNameTxt);
                if(TextUtils.isEmpty(emailTxt)||TextUtils.isEmpty(passwordTxt)||TextUtils.isEmpty(userNameTxt))
                {
//                    Log.d("Aryan","Inside if");
                    Toast.makeText(SignUpActivity.this, "Some Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    Log.d("Aryan","Inside else...");
                    progress.show();
                    auth.createUserWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                UserModel user = new UserModel(emailTxt,passwordTxt,userNameTxt);

                                String id = task.getResult().getUser().getUid();
                                reference.child("Users").child(id).setValue(user);

                                Toast.makeText(SignUpActivity.this, "FireBase Created User", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(SignUpActivity.this, "Something went Wrong"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progress.dismiss();
                        }
                    });
                }

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


        binding.alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));

            }
        });

        binding.GmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                signIn();
            }
        });

        if(auth.getCurrentUser() != null)
        {
            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            finish();
        }
    }

    int RC_SIGN_IN = 45;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Aryan", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Aryan", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Aryan", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            UserModel usersByMe = new UserModel();
                            usersByMe.setUserId(user.getUid());
                            usersByMe.setUserName(user.getDisplayName());
                            usersByMe.setProfilpic(user.getPhotoUrl().toString());
                            reference = dataBase.getReference();
                            reference.child("Users").child(user.getUid()).setValue(usersByMe);

                            progress.dismiss();
                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            finish();

                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Aryan", "signInWithCredential:failure", task.getException());
                            Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

}