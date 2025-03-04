package com.commerzi.app.customers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<com.commerzi.app.customers.CustomerAdapter.CustomerViewHolder> {

    private ArrayList<Customer> customerList;
    private Context context;

    public CustomerAdapter(ArrayList<Customer> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);

        holder.tvCustomerName.setText(customer.getName());
        holder.tvAddress.setText("Adresse : " + customer.getAddress());
        holder.tvCity.setText("Ville : " + customer.getCity());
        holder.tvDescription.setText("Description : " + customer.getDescription());
        holder.tvContact.setText("Contact : " + customer.getContact().getCleanInfos());

        // Logique d'affichage/dépliage
        holder.itemView.setOnClickListener(v -> {
            if (holder.detailsContainer.getVisibility() == View.GONE) {
                holder.detailsContainer.setVisibility(View.VISIBLE);
                holder.tvCustomerName.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_dropdown),
                        null,
                        null ,
                        null);
            } else {
                holder.detailsContainer.setVisibility(View.GONE);
                holder.tvCustomerName.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_dropdown_closed),
                        null,
                        null ,
                        null);
            }
        });

        holder.btnDelete.setOnClickListener(v -> deleteCustomer(customer, position));
        Log.d("CustomerAdapter", "onBindViewHolder: " + customer.getName());
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateCustomerActivity.class);
            intent.putExtra("customer", customer);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvAddress, tvCity, tvDescription, tvContact;
        LinearLayout detailsContainer;
        Button btnDelete;
        Button btnEdit;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCompanyName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvContact = itemView.findViewById(R.id.tvContact);
            detailsContainer = itemView.findViewById(R.id.detailsContainer);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    private void deleteCustomer(Customer customer, int position) {
        Communicator communicator = Communicator.getInstance(context);
        communicator.deleteCustomer(customer, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show();
                    customerList.remove(position);
                    notifyItemRemoved(position);
                },
                error -> {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }
}
