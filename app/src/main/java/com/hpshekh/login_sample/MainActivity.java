package com.hpshekh.login_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.hpshekh.login_sample.databinding.ActivityMainBinding;
import com.hpshekh.login_sample.loginWithGoogle.LoginWithGoogleActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.googleLoginBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LoginWithGoogleActivity.class));
        });
    }
}