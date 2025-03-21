package com.commerzi.app.route.actualRoute;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.commerzi.app.R;
import com.commerzi.app.route.visit.Visit;

import java.util.List;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.VisitViewHolder> {

    private List<Visit> visitList;

    public VisitAdapter(List<Visit> visitList) {
        this.visitList = visitList;
    }

    @NonNull
    @Override
    public VisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_item, parent, false);
        return new VisitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitViewHolder holder, int position) {
        Visit visit = visitList.get(position);
        String status = null;

        switch (visit.getStatus()) {
            case NOT_VISITED:
                status = "Non visité";
                break;
            case VISITED:
                status = "Visité";
                break;
            case SKIPPED:
                status = "Passé";
                break;
        }

        holder.tvVisitStatus.setText(status);
        holder.tvVisitCustomerInfo.setText(visit.getCustomer().getName() + ", " + visit.getCustomer().getContact().getCleanInfos());
    }

    @Override
    public int getItemCount() {
        return visitList.size();
    }

    static class VisitViewHolder extends RecyclerView.ViewHolder {
        TextView tvVisitStatus, tvVisitCustomerInfo;

        public VisitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVisitStatus = itemView.findViewById(R.id.tvVisitStatus);
            tvVisitCustomerInfo = itemView.findViewById(R.id.tvVisitCustomerInfo);
        }
    }
}