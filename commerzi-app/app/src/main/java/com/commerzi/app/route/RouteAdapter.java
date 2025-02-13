package com.commerzi.app.route;

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

import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;
import com.commerzi.app.customers.Customer;
import com.commerzi.app.customers.UpdateCustomerActivity;
import com.commerzi.app.route.plannedRoute.PlannedRoute;

import java.util.ArrayList;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private ArrayList<PlannedRoute> routeList;
    private Context context;

    public RouteAdapter(ArrayList<PlannedRoute> routeList, Context context) {
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
        holder.tvDistance.setText("Distance : " + route.getTotalDistance());

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

//        holder.btnDelete.setOnClickListener(v -> deleteRoute(route, position));
//        holder.btnEdit.setOnClickListener(v -> {
//            Intent intent = new Intent(context, UpdateRouteActivity.class);
//            intent.putExtra("route", route);
//            context.startActivity(intent);
//        });
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

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvCustomers = itemView.findViewById(R.id.tvCustomers);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            detailsContainer = itemView.findViewById(R.id.detailsContainer);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

//    private void deleteRoute(PlannedRoute route, int position) {
//        Communicator communicator = Communicator.getInstance(context);
//        communicator.deleteRoute(route, new CommunicatorCallback<>(
//                response -> {
//                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show();
//                    routeList.remove(position);
//                    notifyItemRemoved(position);
//                },
//                error -> {
//                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show();
//                }
//        ));
//    }
}
