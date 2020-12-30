package com.example.tryingwhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tryingwhatsapp.Models.UserModel;
import com.example.tryingwhatsapp.databinding.ActivitySignInBinding;
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

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progress;
    FirebaseDatabase database;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(SignInActivity.this);
        progress.setTitle("Signing In");
        progress.setMessage("Please Wait");

        binding.SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt = binding.EmailTextSignIn.getText().toString();
                String passwordTxt = binding.PasswordTextSignIn.getText().toString();

                if(TextUtils.isEmpty(emailTxt)||TextUtils.isEmpty(passwordTxt))
                {
                    if(TextUtils.isEmpty(emailTxt))
                    {
                        binding.EmailTextSignIn.setError("This Field is empty");
                        return;
                    }
                    if(TextUtils.isEmpty(passwordTxt))
                    {
                        binding.PasswordTextSignIn.setError("This Field is empty");
                        return;
                    }

                    Toast.makeText(SignInActivity.this, "Some Fields are Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progress.show();
                    auth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(SignInActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this,MainActivity.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SignInActivity.this, "Something went wrong.:-)", Toast.LENGTH_SHORT).show();
                            }
                            progress.dismiss();
                        }
                    });
                }
            }
        });

        binding.NotExistTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        if(auth.getCurrentUser() != null)
        {
            startActivity(new Intent(SignInActivity.this,MainActivity.class));
            finish();
        }

        binding.GmailBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    int RC_SIGN_IN = 65;

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

                            database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference();

                            UserModel usersByMe = new UserModel();
                            usersByMe.setUserId(user.getUid());
                            usersByMe.setUserName(user.getDisplayName());
                            usersByMe.setProfilpic(user.getPhotoUrl().toString());
                            reference = database.getReference();
                            reference.child("Users").child(user.getUid()).setValue(usersByMe);

                            progress.dismiss();
                            startActivity(new Intent(SignInActivity.this,MainActivity.class));
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Aryan", "signInWithCredential:failure", task.getException());
//                            Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

}