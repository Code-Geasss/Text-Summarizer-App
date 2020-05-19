package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity
{
private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
<<<<<<< HEAD
        SystemClock.sleep(10000);
=======
        SystemClock.sleep(2000);
>>>>>>> 3628820e47e7539ab3160ea55b84aa2ea099e8b7
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if( currentUser == null){
            Intent registerIntent = new Intent(SplashActivity.this,Register.class);
            startActivity(registerIntent);
            finish();
        }
        else{
            Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(mainIntent);
            finish();
        }

    }
}
