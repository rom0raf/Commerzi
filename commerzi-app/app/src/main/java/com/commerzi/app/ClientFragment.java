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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientFragment extends Fragment implements View.OnClickListener {

    private final String API_URL = "http://57.128.220.88:8080/api/customers/";

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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = API_URL;

        String apiKey = User.getApiKey();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    ArrayList<Client> clientList = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject client = response.getJSONObject(i);

                            // Récupère les données de l'objet JSON
                            String id = client.optString("id", "Inconnu");
                            String name = client.optString("name", "Inconnu");
                            String address = client.optString("address", "Non spécifié");
                            String city = client.optString("city", "Non spécifié");
                            String description = client.optString("description", "Non spécifiée");
                            String type = client.optString("type", "Inconnu");

                            JSONObject contact = client.optJSONObject("contact");
                            String firstName = contact != null ? contact.optString("firstName", "Inconnu") : "Inconnu";
                            String lastName = contact != null ? contact.optString("lastName", "Inconnu") : "Inconnu";
                            String phoneNumber = contact != null ? contact.optString("phoneNumber", "Non spécifié") : "Non spécifié";

                            // Crée un objet Company et l'ajoute à la liste
                            Client company = new Client(id, name, address, city, description, type, firstName, lastName, phoneNumber);
                            clientList.add(company);
                        }

                        // Passe la liste d'objets Company à displayClients
                        displayClients(clientList);

                    } catch (Exception e) {
                        Log.e("Parsing Error", "Erreur dans le traitement de la réponse", e);
                        Toast.makeText(getActivity(), "Erreur de traitement des données", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Volley Error", "Erreur lors de la requête", error);
                    Toast.makeText(getActivity(), "Impossible de charger les clients", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Commerzi-Auth", apiKey);
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }

    private void displayClients(ArrayList<Client> clientList) {
        // Assurez-vous que le RecyclerView existe dans le layout
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);

        // Configurez le RecyclerView avec un LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        // Créez l'adaptateur avec les données des clients et associez-le au RecyclerView
        ClientAdapter adapter = new ClientAdapter(clientList, this.getContext());
        recyclerView.setAdapter(adapter);
    }

}

