package com.odilonvidal.exam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.odilonvidal.exam.R;
import com.odilonvidal.exam.viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

  private static final String API_URL = "https://api.nasa.gov/insight_weather/?api_key=Gnbki7vQLhxRUqhRAMRHXBOI9nuCgyqGhDDkqQFA&feedtype=json&ver=1.0";
  private WeatherViewModel weatherViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

    weatherViewModel.getWeatherData().observe(this, weatherData -> {
      if (weatherData != null && !weatherData.getSolKeys().isEmpty()) {
        int nbSols = weatherData.getSolKeys().size();
        Toast.makeText(MainActivity.this, "Données météo récupérées pour " + nbSols + " sols",
            Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("nb_sols", nbSols);
        intent.putExtra("weather_data", weatherData);
        startActivity(intent);
      }
    });

    weatherViewModel.getErrorData().observe(this, error -> {
      if (error != null) {
        Toast.makeText(MainActivity.this, "Erreur: " + error, Toast.LENGTH_SHORT).show();
      }
    });

    Button buttonHistory = findViewById(R.id.button_history);
    buttonHistory.setOnClickListener(v -> weatherViewModel.fetchWeatherData(API_URL));

    Button buttonControl = findViewById(R.id.button_control);
    buttonControl.setOnClickListener(v -> {
      Intent intent = new Intent(MainActivity.this, RobotControlActivity.class);
      startActivity(intent);
    });
  }
}
