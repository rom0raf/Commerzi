package com.commerzi.app.customers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;
import com.commerzi.app.customers.CustomerAdapter;

import java.util.ArrayList;

public class CustomerFragment extends Fragment implements View.OnClickListener {
    Button btnAddCustomer;

    public static CustomerFragment newInstance() {
        CustomerFragment fragment = new CustomerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.customer_list, container, false);
        btnAddCustomer = fragmentView.findViewById(R.id.btnGoToCreateCustomer);
        btnAddCustomer.setOnClickListener(this);

        loadCustomers();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCustomers();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnGoToCreateCustomer) {
            Intent intention = new Intent(getActivity(), CreateCustomerActivity.class);
            startActivity(intention);
        }
    }

    private void loadCustomers() {
        if (getActivity() == null) return;

        Communicator communicator = Communicator.getInstance(getActivity());
        communicator.getCustomers(new CommunicatorCallback<>(
                response -> {
                    displayCustomers(response.customers);
                },
                error -> {
                    Toast.makeText(getActivity(), error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    private void displayCustomers(ArrayList<Customer> customerList) {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        com.commerzi.app.customers.CustomerAdapter adapter = new CustomerAdapter(customerList, this.getContext());
        recyclerView.setAdapter(adapter);
    }

}

