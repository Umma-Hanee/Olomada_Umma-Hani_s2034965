package com.example.olomada_umma_hani_s2034965;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder> {

    private List<Earthquake> mainEarthquakes;
    private List<Earthquake> filteredEarthquakes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Earthquake earthquake);
    }

    public EarthquakeAdapter(List<Earthquake> earthquakes, OnItemClickListener listener) {
        this.mainEarthquakes = new ArrayList<>(earthquakes);
        this.filteredEarthquakes = new ArrayList<>(earthquakes);
        this.listener = listener;
    }


    @Override
    public EarthquakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.earthquake_list_item, parent, false);
        return new EarthquakeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EarthquakeViewHolder holder, int position) {
        Earthquake earthquake = filteredEarthquakes.get(position);
        holder.titleTextView.setText(earthquake.getTitle());
        holder.descriptionTextView.setText(earthquake.getDescription());

            // Set the color of the list item based on the strength of the earthquake
            int color = getColorForEarthquakeStrength(earthquake.getMagnitude());
            holder.itemView.setBackgroundColor(color);
        }

        private int getColorForEarthquakeStrength(double magnitude) {
            int color = Color.GREEN;
            if (magnitude >= 7.0) {
                color = Color.YELLOW;
            } else if (magnitude >= 5.0) {
                color = Color.BLUE;
            } else if (magnitude >= 3.0) {
                color = Color.RED;
            }
            return color;
        }


    @Override
    public int getItemCount() {
        return filteredEarthquakes.size();
    }

    public void resetData() {
        filteredEarthquakes.clear();
        filteredEarthquakes.addAll(mainEarthquakes);
        notifyDataSetChanged();
    }



    public class EarthquakeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleTextView;
        public TextView descriptionTextView;

        public EarthquakeViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(filteredEarthquakes.get(position));
            }
        }
    }
}