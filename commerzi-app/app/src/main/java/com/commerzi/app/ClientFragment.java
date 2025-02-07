package com.commerzi.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientFragment extends Fragment implements View.OnClickListener {
    Button btnAddClient;

    public static ClientFragment newInstance() {
        ClientFragment fragment = new ClientFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.client_list, container, false);
        btnAddClient = fragmentView.findViewById(R.id.btnAddClient);
        btnAddClient.setOnClickListener(this);

        loadClients();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadClients();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnAddClient) {
            Intent intention = new Intent(getActivity(), AddClientActivity.class);
            startActivity(intention);
        }
    }

    private void loadClients() {
        if (getActivity() == null) return;

        Communicator communicator = Communicator.getInstance(getActivity());
        communicator.getClients(new CommunicatorCallback<>(
                response -> {
                    displayClients(response.clients);
                },
                error -> {
                    Toast.makeText(getActivity(), error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    private void displayClients(ArrayList<Client> clientList) {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ClientAdapter adapter = new ClientAdapter(clientList, this.getContext());
        recyclerView.setAdapter(adapter);
    }

}

