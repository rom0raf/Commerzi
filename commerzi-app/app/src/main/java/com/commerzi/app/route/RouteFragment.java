package com.commerzi.app.route;

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
import com.commerzi.app.customers.CreateCustomerActivity;
import com.commerzi.app.customers.Customer;
import com.commerzi.app.customers.CustomerAdapter;
import com.commerzi.app.route.plannedRoute.CreateRouteActivity;
import com.commerzi.app.route.plannedRoute.PlannedRoute;

import java.util.ArrayList;

public class RouteFragment extends Fragment implements View.OnClickListener {
    
    Button btnAddRoute;

    public static RouteFragment newInstance() {
        RouteFragment fragment = new RouteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.route_list, container, false);
        btnAddRoute = fragmentView.findViewById(R.id.btnGoToCreateRoute);
        btnAddRoute.setOnClickListener(this);
        loadRoutes();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRoutes();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnGoToCreateRoute) {
            Intent intention = new Intent(getActivity(), CreateRouteActivity.class);
            startActivity(intention);
        }
    }

    private void loadRoutes() {
        if (getActivity() == null) return;
        
        ArrayList<Customer> customers = new ArrayList<>();

        Communicator communicator = Communicator.getInstance(getActivity());
        communicator.getRoutes(new CommunicatorCallback<>(
                response -> {
                },
                error -> {
                    Toast.makeText(getActivity(), error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    private void displayRoutes(ArrayList<PlannedRoute> routeList) {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewRoute);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        com.commerzi.app.route.RouteAdapter adapter = new RouteAdapter(routeList, this.getContext());
        recyclerView.setAdapter(adapter);
    }

}
