package com.commerzi.app.route.plannedRoute;

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
import com.commerzi.app.customers.Customer;
import com.commerzi.app.route.actualRoute.NavigationActivity;

import java.util.ArrayList;

public class PlannedRouteAdapter extends RecyclerView.Adapter<PlannedRouteAdapter.RouteViewHolder> {

    private ArrayList<PlannedRoute> routeList;
    private Context context;

    public PlannedRouteAdapter(ArrayList<PlannedRoute> routeList, Context context) {
        this.routeList = routeList;
        this.context = context;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        PlannedRoute route = routeList.get(position);

        holder.tvRouteName.setText(route.getName());

        double distanceKM = route.getTotalDistance() / 1000;
        // round to 2 decimal places
        distanceKM = Math.round(distanceKM * 100.0) / 100.0;

        if (route.getTotalDistance() < 0) {
            holder.tvDistance.setText("Distance : Calculating...");

        } else {
            holder.tvDistance.setText("Distance : " + distanceKM + " km");
        }

        // add a list of customers to the route view
        StringBuilder customers = new StringBuilder();
        for (Customer customer : route.getCustomers()) {
            customers.append(customer.getName()).append(", ").append(customer.getContact().getCleanInfos()).append("\n");
        }

        holder.tvCustomers.setText(customers.toString());

        // Logique d'affichage/dépliage
        holder.itemView.setOnClickListener(v -> {
            if (holder.detailsContainer.getVisibility() == View.GONE) {
                holder.detailsContainer.setVisibility(View.VISIBLE);
                holder.tvRouteName.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_dropdown),
                        null,
                        null ,
                        null);
            } else {
                holder.detailsContainer.setVisibility(View.GONE);
                holder.tvRouteName.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_dropdown_closed),
                        null,
                        null ,
                        null);
            }
        });

        holder.btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(context, NavigationActivity.class);
            intent.putExtra("route", route);
            context.startActivity(intent);
        });

       holder.btnDelete.setOnClickListener(v -> deleteRoute(position));
       holder.btnEdit.setOnClickListener(v -> {
           Intent intent = new Intent(context, UpdatePlannedRouteActivity.class);
           intent.putExtra("route", route);
           context.startActivity(intent);
       });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView tvRouteName, tvCustomers, tvDistance;
        LinearLayout detailsContainer;
        Button btnDelete;
        Button btnEdit;
        Button btnStart;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvCustomers = itemView.findViewById(R.id.tvCustomers);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            detailsContainer = itemView.findViewById(R.id.detailsContainer);
            btnDelete = itemView.findViewById(R.id.btnDeleteRoute);
            btnEdit = itemView.findViewById(R.id.btnEditRoute);
            btnStart = itemView.findViewById(R.id.startRoute);
        }
    }

     private void deleteRoute(int position) {
       Log.d("Planned route", "deleteRoute: " + position);

       // delete the route from server and local list

       PlannedRoute route = routeList.get(position);

         routeList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, routeList.size());


       Communicator communicator = Communicator.getInstance(context);
       communicator.deletePlannedRoute(route, new CommunicatorCallback<>(
               response -> {
                   Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show();

                   //
               },
               error -> {
                   Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show();
               }
               ));

    }
}
