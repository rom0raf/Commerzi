package com.commerzi.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    Button btnLogin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::onLoginButtonClicked);
    }

    public void onLoginButtonClicked(View view) {
        Intent intention = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intention);
    }
}
