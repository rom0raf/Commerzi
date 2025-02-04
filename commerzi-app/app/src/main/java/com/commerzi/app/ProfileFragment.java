package com.commerzi.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView tvUserFullName;
    TextView tvUserEmail;
    TextView tvUserAddress;
    Button btnEditProfile;


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        tvUserFullName = view.findViewById(R.id.userFullName);
        tvUserEmail = view.findViewById(R.id.userEmail);
        tvUserAddress = view.findViewById(R.id.userAddress);

        User sessionUser = Session.getUser();

        tvUserFullName.setText("Nom : " + sessionUser.getFirstName() + " " + sessionUser.getLastName());
        tvUserEmail.setText("Email : " + sessionUser.getEmail());
        tvUserAddress.setText("Adresse : " + sessionUser.getAddress() + ", " + sessionUser.getCity());

        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        User sessionUser = Session.getUser();
        tvUserFullName.setText("Nom : " + sessionUser.getFirstName() + " " + sessionUser.getLastName());
        tvUserEmail.setText("Email : " + sessionUser.getEmail());
        tvUserAddress.setText("Adresse : " + sessionUser.getAddress() + ", " + sessionUser.getCity());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnEditProfile) {
            Intent intention = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intention);
        }
    }
}
