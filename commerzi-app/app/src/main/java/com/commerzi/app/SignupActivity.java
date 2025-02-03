package com.commerzi.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private final String API_URL = "http://57.128.220.88:8080/api/user/";
    Button btnLogin;
    Button btnSignup;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtPwdConfirmation;
    EditText txtFirstname;
    EditText txtLastname;
    EditText txtAddress;
    EditText txtCity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        txtEmail = findViewById(R.id.emailSignUpField);
        txtFirstname = findViewById(R.id.firstnameSignUpField);
        txtLastname = findViewById(R.id.lastnameSignUpField);
        txtPassword = findViewById(R.id.passwordSignUpField);
        txtPwdConfirmation = findViewById(R.id.confirmationSignUpField);
        txtAddress = findViewById(R.id.addressSignUpField);
        txtCity = findViewById(R.id.citySignUpField);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::onLoginButtonClicked);

        btnSignup = findViewById(R.id.btnValidateSignUp);
        btnSignup.setOnClickListener(this::handleSignupButtonClicked);
    }

    public void handleSignupButtonClicked(View view) {
        String email = txtEmail.getText().toString();
        String firstname = txtFirstname.getText().toString();
        String lastname = txtLastname.getText().toString();
        String address = txtAddress.getText().toString();
        String password = txtPassword.getText().toString();
        String city = txtCity.getText().toString();
        String pwdConfirmation = txtPwdConfirmation.getText().toString();

        if (email.isEmpty() || password.isEmpty()
                || firstname.isEmpty() || lastname.isEmpty()
                || address.isEmpty() || pwdConfirmation.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_field, Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(pwdConfirmation))  {
            Toast.makeText(this, R.string.error_invalid_password, Toast.LENGTH_LONG).show();
            return;
        }

        signupUser(email, password, firstname, lastname, address, city);
    }

    private void signupUser(String email, String password, String firstname, String lastname, String address, String city) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("firstName", firstname);
            requestBody.put("lastName", lastname);
            requestBody.put("address", address);
            requestBody.put("city", city);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(SignupActivity.this, R.string.account_creation_success, Toast.LENGTH_LONG).show();
                            Intent intention = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intention);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null && error.networkResponse != null) {
                                String response = new String(error.networkResponse.data);
                                Toast.makeText(SignupActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SignupActivity.this, R.string.unexpected_error, Toast.LENGTH_LONG).show();                            }
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

    public void onLoginButtonClicked(View view) {
        Intent intention = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intention);
    }
}
