package com.example.bigbrother;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.bigbrother.databinding.ActivityLoginBinding;
import com.example.bigbrother.databinding.ActivityMainBinding;
import com.example.bigbrother.databinding.ActivitySingupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setviews();




    }

    private void setviews() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("name");
            binding.myname.setText("Hii, " + value);
            String photo = extras.getString("purl");
            Glide.with(MainActivity.this)
                    .load(photo)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.profile)
                    .into(binding.myphoto);
        }

    }


}