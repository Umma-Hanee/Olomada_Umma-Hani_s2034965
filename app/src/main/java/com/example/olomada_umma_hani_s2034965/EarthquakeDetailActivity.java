package com.example.olomada_umma_hani_s2034965;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EarthquakeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_detail);

        // Get the title and description extras from the Intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");


        // Set the title and description TextViews
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        // Enable the back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                // Finish the activity and return to the previous one
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}