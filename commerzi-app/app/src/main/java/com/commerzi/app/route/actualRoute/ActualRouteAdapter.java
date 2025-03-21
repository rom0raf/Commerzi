package com.commerzi.app.route.actualRoute;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;
import com.commerzi.app.route.utils.ERouteStatus;
import com.commerzi.app.route.visit.Visit;

import java.util.ArrayList;

/**
 * Adapter class for displaying a list of actual routes in a RecyclerView.
 */
public class ActualRouteAdapter extends RecyclerView.Adapter<ActualRouteAdapter.RouteViewHolder> {

    private ArrayList<ActualRoute> routeList;
    private Context context;

    /**
     * Constructor for ActualRouteAdapter.
     *
     * @param routeList The list of actual routes to display.
     * @param context The context in which the adapter is used.
     */
    public ActualRouteAdapter(ArrayList<ActualRoute> routeList, Context context) {
        this.routeList = routeList;
        this.context = context;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actual_route_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        ActualRoute route = routeList.get(position);
        String status = null;

        // Determine the status of the route
        switch (route.getStatus()) {
            case PAUSED:
                status = "En pause";
                break;
            case IN_PROGRESS:
                status = "En cours";
                break;
            case COMPLETED:
                status = "Terminée";
                break;
            case CANCELLED:
                status = "Annulée";
                break;
        }

        holder.tvActualRouteTitle.setText(route.getDate() + " - " + status);

        // Set up the visit adapter for the RecyclerView
        VisitAdapter visitAdapter = new VisitAdapter(route.getVisits());
        holder.rvVisits.setLayoutManager(new LinearLayoutManager(context));
        holder.rvVisits.setAdapter(visitAdapter);

        // Toggle visibility of details container
        holder.itemView.setOnClickListener(v -> {
            if (holder.detailsContainer.getVisibility() == View.GONE) {
                holder.detailsContainer.setVisibility(View.VISIBLE);
                holder.tvActualRouteTitle.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_dropdown),
                        null,
                        null,
                        null);
            } else {
                holder.detailsContainer.setVisibility(View.GONE);
                holder.tvActualRouteTitle.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_dropdown_closed),
                        null,
                        null,
                        null);
            }
        });

        // Set up the restart button with a long click listener
        holder.btnRestart.setOnLongClickListener(v -> {
            Communicator communicator = Communicator.getInstance(context);
            communicator.resumeRoute(route, new CommunicatorCallback<>(
                    response -> {
                        Log.d("Unpause", "onBindViewHolder: ");
                    },
                    error -> {
                        Toast.makeText(context, "Erreur inatendue: " + error.message, Toast.LENGTH_SHORT).show();
                        Log.d("Unpause", "onBindViewHolder: " + error.message);
                    }
            ));

            Intent intent = new Intent(context, NavigationActivity.class);
            intent.putExtra("route", route);
            context.startActivity(intent);
            return true;
        });

        // Set up the delete button with a click listener
        holder.btnDelete.setOnClickListener(v -> deleteRoute(position));
    }

    /**
     * ViewHolder class for route items.
     */
    static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView tvActualRouteTitle;
        RecyclerView rvVisits;
        LinearLayout detailsContainer;
        Button btnDelete;
        Button btnRestart;

        /**
         * Constructor for RouteViewHolder.
         *
         * @param itemView The view of the route item.
         */
        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvActualRouteTitle = itemView.findViewById(R.id.tvActualRouteTitle);
            rvVisits = itemView.findViewById(R.id.rvVisits);
            detailsContainer = itemView.findViewById(R.id.detailsContainer);
            btnDelete = itemView.findViewById(R.id.btnDeleteActualRoute);
            btnRestart = itemView.findViewById(R.id.btnRestartActualRoute);
        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    /**
     * Deletes a route and updates the RecyclerView.
     *
     * @param position The position of the route in the list.
     */
    private void deleteRoute(int position) {
        ActualRoute route = routeList.get(position);

        routeList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, routeList.size());

        Communicator communicator = Communicator.getInstance(context);
        communicator.deleteActualRoute(route, new CommunicatorCallback<>(
              response -> {
                  Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show();
              },
              error -> {
                  Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show();
              }
        ));
    }
}