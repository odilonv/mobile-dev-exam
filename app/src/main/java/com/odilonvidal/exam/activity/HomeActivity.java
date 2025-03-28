package com.odilonvidal.exam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.odilonvidal.exam.R;
import com.odilonvidal.exam.model.MarsWeather;
import com.odilonvidal.exam.adapter.SolAdapter;

public class HomeActivity extends AppCompatActivity {
    private MarsWeather weatherData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

      TextView weatherTextView = findViewById(R.id.weatherTextView);
      ListView solsListView = findViewById(R.id.solsListView);

    int nbSols = getIntent().getIntExtra("nb_sols", 0);
    weatherTextView.setText(String.valueOf(nbSols));

    weatherData = (MarsWeather) getIntent().getSerializableExtra("weather_data");
    if (weatherData != null && !weatherData.getSolKeys().isEmpty()) {
      SolAdapter adapter = new SolAdapter(this, weatherData.getSolKeys(), weatherData.getSols());
      solsListView.setAdapter(adapter);

      solsListView.setOnItemClickListener((parent, view, position, id) -> {
        String solNumber = weatherData.getSolKeys().get(position);

        Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
        intent.putExtra("sol_number", solNumber);
        intent.putExtra("weather_data", weatherData);
        startActivity(intent);
      });
    }
  }
}
