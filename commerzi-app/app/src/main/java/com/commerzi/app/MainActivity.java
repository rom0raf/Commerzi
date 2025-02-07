package com.commerzi.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText txtEmail;
    EditText txtPassword;
    Button btnValidate;
    Button btnSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        txtEmail = findViewById(R.id.emailLoginField);
        txtPassword = findViewById(R.id.passwordLoginField);
        btnValidate = findViewById(R.id.btnValidateLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnValidate.setOnClickListener(this::handleLoginButtonClicked);
        btnSignup.setOnClickListener(this::handleSignupButtonClicked);
    }

    public void handleLoginButtonClicked(View view) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.email_password_empty, Toast.LENGTH_LONG).show();
            return;
        }

        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.login(email, password, new CommunicatorCallback<>(
                response ->  {
                    Session.setUser(response.user);
                    Intent intention = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intention);
                },
                error -> {
                    Toast.makeText(MainActivity.this, error.message, Toast.LENGTH_LONG).show();
                }
        ));
    }

    public void handleSignupButtonClicked(View view) {
        Intent intention = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intention);
    }
}