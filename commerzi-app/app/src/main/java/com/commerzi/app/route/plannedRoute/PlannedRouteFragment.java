package com.commerzi.app.route.plannedRoute;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

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

public class PlannedRouteFragment extends Fragment implements View.OnClickListener {
    
    Button btnAddRoute;

    public static PlannedRouteFragment newInstance() {
        PlannedRouteFragment fragment = new PlannedRouteFragment();
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
        checkCustomerExistence();
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

    private void checkCustomerExistence(){
        Communicator communicator = Communicator.getInstance(getActivity());
        communicator.getCustomers(new CommunicatorCallback<>(
                response -> {
                    if (!response.customers.isEmpty()) btnAddRoute.setVisibility(View.VISIBLE);
                },
                error -> {
                    Toast.makeText(getActivity(), error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    private void loadRoutes() {
        if (getActivity() == null) return;

        Communicator communicator = Communicator.getInstance(getActivity());
        communicator.getRoutes(new CommunicatorCallback<>(
                response -> {
                    Log.d("RouteFragment", "loadRoutes: " + response.routes.size());
                    displayRoutes(response.routes);
                },
                error -> {
                    Toast.makeText(getActivity(), error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    private void displayRoutes(ArrayList<PlannedRoute> routeList) {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewRoute);
        TextView noPlannedRouteMessage = getView().findViewById(R.id.tvNoPlannedRoute);

        if (routeList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noPlannedRouteMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noPlannedRouteMessage.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            PlannedRouteAdapter adapter = new PlannedRouteAdapter(routeList, this.getContext());
            recyclerView.setAdapter(adapter);
        }

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                for (PlannedRoute route : routeList) {
                    if (route.getTotalDistance() < 0) {
                        loadRoutes();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
