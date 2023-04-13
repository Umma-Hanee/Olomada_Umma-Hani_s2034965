package com.example.olomada_umma_hani_s2034965;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button parseButton;
    private Button searchButton;
    private Button clearButton;
    private EditText locationEditText;
    private EditText dateEditText;
    private List<Earthquake> earthquakes;
    private List<Earthquake> filteredEarthquakes = new ArrayList<>();
    private EarthquakeAdapter adapter;

    private Handler filterHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.earthquakeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        parseButton = findViewById(R.id.parseButton);
        parseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        earthquakes = EarthquakeXmlParser.parse("http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml");
                        filteredEarthquakes = new ArrayList<>(earthquakes);
                        Message message = Message.obtain();
                        message.obj = filteredEarthquakes;
                        handler.sendMessage(message);
                    }
                }).start();
                parseButton.setVisibility(View.GONE);
                locationEditText.setVisibility(View.VISIBLE);
                dateEditText.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                clearButton.setVisibility(View.VISIBLE);
            }
        });

        locationEditText = findViewById(R.id.locationEditText);
        dateEditText = findViewById(R.id.dateEditText);

        searchButton = findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterEarthquakes();
               // clearButton.setVisibility(View.VISIBLE);
            }
        });
        clearButton = findViewById(R.id.clear_button);
        clearButton.setVisibility(View.GONE);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSearchQuery();
                clearButton.setVisibility(View.GONE);
                searchButton.setVisibility(View.VISIBLE);
            }

            private void clearSearchQuery() {
                locationEditText.setText("");
                dateEditText.setText("");
                filteredEarthquakes = new ArrayList<>(earthquakes);
                adapter.resetData();
            }

        });

    }

    private void filterEarthquakes() {
        String locationString = locationEditText.getText().toString().trim();
        String dateStringInput = dateEditText.getText().toString().trim();
        // Convert user input date to the yyyy-MM-dd format
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.US);
        String dateString = null;
        try {
            Date date = inputDateFormat.parse(dateStringInput);
            dateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        filteredEarthquakes.clear();
        if ((locationString == null || locationString.isEmpty()) && (dateString == null || dateString.isEmpty())) {
            filteredEarthquakes.addAll(earthquakes);
        } else if (dateString == null || dateString.isEmpty()) {
            // Only date filter applied
            for (Earthquake earthquake : earthquakes){
                if (earthquake.getDescription().contains(locationString)){
                    filteredEarthquakes.add(earthquake);
                }
            }
            //filteredEarthquakes.addAll(earthquakes);
        } else if (locationString == null || locationString.isEmpty()) {
            // Only location filter applied
            for (Earthquake earthquake : earthquakes) {
                if (earthquake.getDate().contains(dateString)) {
                    filteredEarthquakes.add(earthquake);
                }
            }
        } else {
            // Both filters applied
            for (Earthquake earthquake : earthquakes) {
                if (earthquake.getDescription().contains(locationString) &&
                        earthquake.getDate().contains(dateString)) {
                    filteredEarthquakes.add(earthquake);
                }
            }
        }

        if(filteredEarthquakes.size()>0){
            clearButton.setVisibility(View.VISIBLE);
        }
        adapter = new EarthquakeAdapter(filteredEarthquakes, new EarthquakeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Earthquake earthquake) {
                Intent intent = new Intent(MainActivity.this, EarthquakeDetailActivity.class);
                intent.putExtra("title", earthquake.getTitle());
                intent.putExtra("description", earthquake.getDescription());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
        private Runnable filterRunnable = new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
            }
        };

        private Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                filteredEarthquakes = (List<Earthquake>) message.obj;
                adapter = new EarthquakeAdapter(filteredEarthquakes, new EarthquakeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Earthquake earthquake) {
                        Intent intent = new Intent(MainActivity.this, EarthquakeDetailActivity.class);
                        intent.putExtra("title", earthquake.getTitle());
                        intent.putExtra("description", earthquake.getDescription());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                return true;
            }
        });

    }