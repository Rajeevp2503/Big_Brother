package com.example.bigbrother;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.bigbrother.databinding.ActivityLoginBinding;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class loginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String email, password;
    FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // hide the action bar
        // for hiding of action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        auth = FirebaseAuth.getInstance();
        db  = FirebaseFirestore.getInstance();


        progressDialogs();
        signInWithEmail();
        googlesignin();
        signup();

    }

    private void progressDialogs() {
        progressDialog = new ProgressDialog(loginActivity.this);
        progressDialog.setTitle("Big Brother Login");
        progressDialog.setMessage("Wait......");
    }


    private void signInWithEmail() {

        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = binding.inemail.getText().toString().trim();
                password = binding.inpassword.getText().toString();
                // checking for empty email
                if (email.isEmpty()) {
                    binding.inemail.setError("Email is required");
                    binding.inemail.requestFocus();
                    return;
                }
                // for valid email
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.inemail.setError("Email is invalid");
                    binding.inemail.requestFocus();
                    return;
                }
                // then authenticate it via email and password

                authenticationWithEmailAndPassword();

            }
        });
    }
    private void authenticationWithEmailAndPassword() {
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "successfully Login", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                        } else {
                            Log.w("rajj", "createUserWithEmail:failure", task.getException());
                            Log.e("rajeeev", "authorization nahi  hai sign in with email ");
                        }

                    }
                });
    }



    private void signup() {
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this,singupActivity.class));
            }
        });
    }


    private void googlesignin() {
        auth = FirebaseAuth.getInstance();
        binding.googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("704309249131-oel3mu83q3v75b061lhmgmtfggpkvdbu.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(loginActivity.this, gso);
                progressDialog.show();

                // calling sign in for intent and getting result from intent
                signIn();
            }
        });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,101);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                // calling the fuction firebase auth with google
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG1", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String id = firebaseUser.getUid();


                            // crreating object of user of model section for storing data in firestore
                            User u2 = new User();
                            u2.setName(firebaseUser.getDisplayName());
                            u2.setPic(firebaseUser.getPhotoUrl().toString());


                            db.collection("Users").document(id).set(u2);


                            //intent for main activity
                            Intent i = new Intent(loginActivity.this,MainActivity.class);
                            i.putExtra("name",firebaseUser.getDisplayName());
                            i.putExtra("purl",firebaseUser.getPhotoUrl().toString());
                            startActivity(i);                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //  updateUI(null);
                        }
                    }
                });
    }



}

