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

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String API_URL = "http://57.128.220.88:8080/api/auth/";
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
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("email", email);
            requestBody.put("password", password);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = !jsonResponse.getString("userId").isEmpty();

                                if (success) {
                                    Intent intention = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intention);
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.credentials_error, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, R.string.response_parsing_error, Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, R.string.connection_error + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                public byte[] getBody() {
                    return requestBody.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.request_creation_error, Toast.LENGTH_LONG).show();
        }
    }

    public void handleSignupButtonClicked(View view) {
        Intent intention = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intention);
    }
}