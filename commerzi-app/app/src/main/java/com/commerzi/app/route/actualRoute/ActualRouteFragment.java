package com.commerzi.app.route.actualRoute;

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
import com.commerzi.app.communication.responses.ActualRouteResponse;
import com.commerzi.app.communication.responses.CommunicatorCallback;

import java.util.ArrayList;

public class ActualRouteFragment extends Fragment {

    public static ActualRouteFragment newInstance() {
        ActualRouteFragment fragment = new ActualRouteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.actual_route_list, container, false);
        loadRoutes();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRoutes();
    }


    private void loadRoutes() {
        if (getActivity() == null) return;

        Communicator communicator = Communicator.getInstance(getActivity());
        communicator.getActualRoutes(new CommunicatorCallback<ActualRouteResponse>(
                response -> {
                    Log.d("RouteFragment", "loadRoutes: " + response.routes.size());
                    displayRoutes(response.routes);
                },
                error -> {
                    Toast.makeText(getActivity(), error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    private void displayRoutes(ArrayList<ActualRoute> routeList) {
    View view = getView();
    if (view == null) return;

    RecyclerView recyclerView = view.findViewById(R.id.recyclerViewActualRoute);
    TextView noActualRouteMessage = view.findViewById(R.id.tvNoActualRoute);

    if (routeList.isEmpty()) {
        recyclerView.setVisibility(View.GONE);
        noActualRouteMessage.setVisibility(View.VISIBLE);
    } else {
        recyclerView.setVisibility(View.VISIBLE);
        noActualRouteMessage.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ActualRouteAdapter adapter = new ActualRouteAdapter(routeList, this.getContext());
        recyclerView.setAdapter(adapter);
    }
}


}
