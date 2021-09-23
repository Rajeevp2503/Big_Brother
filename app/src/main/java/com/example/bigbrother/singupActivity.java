package com.example.bigbrother;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.bigbrother.databinding.ActivityLoginBinding;
import com.example.bigbrother.databinding.ActivitySingupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class singupActivity extends AppCompatActivity {
    ActivitySingupBinding binding;
    ProgressDialog progressDialog;
    String email, password,name;

    FirebaseAuth auth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // hide the action bar
        // for hiding of action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        progressDialog = new ProgressDialog(singupActivity.this);
        progressDialog.setTitle("creating account");
        progressDialog.setMessage("Loading.....");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        singUpWithEmail();
    }

    private void singUpWithEmail() {

        binding.upsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = binding.upemail.getText().toString().trim();
                name = binding.upname.getText().toString().trim();
                password = binding.uppassword.getText().toString();
                // checking for empty email
                if (email.isEmpty()) {
                    binding.upemail.setError("Email is required");
                    binding.upemail.requestFocus();
                    return;
                }
                // for valid email
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.upemail.setError("Email is invalid");
                    binding.upemail.requestFocus();
                    return;
                }
                // then create and authenticate it via email and password

                authenticationWithEmailAndPassword();

            }
        });
    }

    private void authenticationWithEmailAndPassword() {
        progressDialog.show();

        //now main authentication will be done
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(singupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.e("raj", "authorization ho gaya hai");
                            // taking uid refreence in string id
                            String id = task.getResult().getUser().getUid();
                            // creating object of user and storing user in fire store
                           User u1 = new User(name, email, password);
                            db.collection("Users").document(id).set(u1);

                            Intent i = new Intent(singupActivity.this,MainActivity.class);
                            i.putExtra("name",name);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "Registerd", Toast.LENGTH_LONG).show();

                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Log.e("raje", "authorization nahi hai");

                            Toast.makeText(getApplicationContext(), "ERRRRRORR", Toast.LENGTH_LONG).show();
                        }
                    }
    });
    }


}
