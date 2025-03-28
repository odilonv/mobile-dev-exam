package com.odilonvidal.exam.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.odilonvidal.exam.R;
import com.odilonvidal.exam.model.Sol;
import com.odilonvidal.exam.model.MarsWeather;
import com.odilonvidal.exam.view.WindRoseView;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

  private TextView solTitleTextView;
  private TextView temperatureTextView;
  private TextView pressureTextView;
  private WindRoseView windRoseView;
  private GestureDetectorCompat gestureDetector;
  private MarsWeather weatherData;
  private int currentSolIndex;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    solTitleTextView = findViewById(R.id.solTitleTextView);
    temperatureTextView = findViewById(R.id.temperatureTextView);
    pressureTextView = findViewById(R.id.pressureTextView);
    windRoseView = findViewById(R.id.windRoseView);

    gestureDetector = new GestureDetectorCompat(this, this);

    weatherData = (MarsWeather) getIntent().getSerializableExtra("weather_data");
    String solNumber = getIntent().getStringExtra("sol_number");

    if (weatherData == null || solNumber == null || weatherData.getSolKeys() == null || weatherData.getSols() == null) {
      Toast.makeText(this, "Erreur: Données manquantes", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }

    currentSolIndex = weatherData.getSolKeys().indexOf(solNumber);
    if (currentSolIndex == -1) {
      Toast.makeText(this, "Erreur: Sol non trouvé", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }

    displaySolData(solNumber);
  }

  private void displaySolData(String solNumber) {
    Sol sol = weatherData.getSols().get(solNumber);
    if (sol == null || sol.getTemperature() == null || sol.getPressure() == null) {
      Toast.makeText(this, "Erreur: Données du sol incomplètes", Toast.LENGTH_SHORT).show();
      return;
    }

    solTitleTextView.setText(getString(R.string.sol_number_format, solNumber));

    String tempText = String.format(Locale.getDefault(), getString(R.string.temperature_format),
        sol.getTemperature().getAverage(),
        sol.getTemperature().getMinimum(),
        sol.getTemperature().getMaximum());
    temperatureTextView.setText(tempText);

    String pressureText = String.format(Locale.getDefault(), getString(R.string.pressure_format),
        sol.getPressure().getAverage(),
        sol.getPressure().getMinimum(),
        sol.getPressure().getMaximum());
    pressureTextView.setText(pressureText);

    if (sol.getWind() != null && sol.getWind().getDirections() != null) {
      windRoseView.setWindData(sol.getWind().getDirections());
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (gestureDetector != null) {
      return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }
    return super.onTouchEvent(event);
  }

  @Override
  public boolean onDown(@NonNull MotionEvent e) {
    return true;
  }

  @Override
  public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
    if (e1 == null)
      return false;

    float diffY = e2.getY() - e1.getY();
    if (Math.abs(diffY) > 100) {
      if (diffY > 0) {
        if (currentSolIndex > 0) {
          currentSolIndex--;
          String prevSol = weatherData.getSolKeys().get(currentSolIndex);
          displaySolData(prevSol);
          return true;
        }
      } else {
        if (currentSolIndex < weatherData.getSolKeys().size() - 1) {
          currentSolIndex++;
          String nextSol = weatherData.getSolKeys().get(currentSolIndex);
          displaySolData(nextSol);
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void onShowPress(@NonNull MotionEvent e) {
  }

  @Override
  public boolean onSingleTapUp(@NonNull MotionEvent e) {
    return false;
  }

  @Override
  public boolean onScroll(MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
    return false;
  }

  @Override
  public void onLongPress(@NonNull MotionEvent e) {
  }
}