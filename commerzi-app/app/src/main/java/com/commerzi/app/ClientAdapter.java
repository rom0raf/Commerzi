package com.commerzi.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private ArrayList<Client> clientList;
    private Context context;

    public ClientAdapter(ArrayList<Client> clientList, Context context) {
        this.clientList = clientList;
        this.context = context;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clientList.get(position);

        holder.tvClientName.setText(client.getName());
        holder.tvAddress.setText("Adresse : " + client.getAddress());
        holder.tvCity.setText("Ville : " + client.getCity());
        holder.tvDescription.setText("Description : " + client.getDescription());
        holder.tvContact.setText("Contact : " + client.getContactInfo());

        // Logique d'affichage/dépliage
        holder.itemView.setOnClickListener(v -> {
            if (holder.detailsContainer.getVisibility() == View.GONE) {
                holder.detailsContainer.setVisibility(View.VISIBLE);
                holder.tvClientName.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_dropdown),
                        null,
                        null ,
                        null);
            } else {
                holder.detailsContainer.setVisibility(View.GONE);
                holder.tvClientName.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_dropdown_closed),
                        null,
                        null ,
                        null);
            }
        });

        holder.btnDelete.setOnClickListener(v -> deleteClient(client, position));
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditClientActivity.class);
            intent.putExtra("client", client);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView tvClientName, tvAddress, tvCity, tvDescription, tvContact;
        LinearLayout detailsContainer;
        Button btnDelete;
        Button btnEdit;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClientName = itemView.findViewById(R.id.tvCompanyName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvContact = itemView.findViewById(R.id.tvContact);
            detailsContainer = itemView.findViewById(R.id.detailsContainer);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    private void deleteClient(Client client, int position) {
        String url = "http://57.128.220.88:8080/api/customers/" + client.getId();

        RequestQueue queue = Volley.newRequestQueue(context);
        String apiKey = Session.getInstance().getApiKey();

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                    Toast.makeText(context, "Entreprise supprimée", Toast.LENGTH_SHORT).show();
                    clientList.remove(position);
                    notifyItemRemoved(position);
                },
                error -> {
                    Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Commerzi-Auth", apiKey);
                return headers;
            }
        };

        queue.add(deleteRequest);
    }
}
