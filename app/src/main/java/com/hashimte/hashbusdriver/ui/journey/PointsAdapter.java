package com.hashimte.hashbusdriver.ui.journey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.hashimte.hashbusdriver.R;
import com.hashimte.hashbusdriver.model.Point;

import java.util.List;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {

    private List<Point> points;

    public PointsAdapter(List<Point> points) {
        this.points = points;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.point_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Point point = points.get(position);
        holder.pointName.setText(point.getPointName());
        if (position == points.size() - 1) {
            ((ViewGroup) holder.arrow.getParent()).removeView(holder.arrow);
        }
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView pointName;
        CardView arrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pointName = itemView.findViewById(R.id.txt_point_name);
            arrow = itemView.findViewById(R.id.arrow);
        }
    }
}
