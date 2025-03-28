package com.odilonvidal.exam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.odilonvidal.exam.R;
import com.odilonvidal.exam.model.Sol;

import java.util.List;
import java.util.Map;

public class SolAdapter extends ArrayAdapter<String> {
  private final Map<String, Sol> solsMap;

  public SolAdapter(Context context, List<String> solKeys, Map<String, Sol> solsMap) {
    super(context, 0, solKeys);
    this.solsMap = solsMap;
  }

  @Override
  public View getView(int position, View convertView, @NonNull ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sol, parent, false);
    }

    String solKey = getItem(position);
    Sol sol = solsMap.get(solKey);

    TextView solNumberTextView = convertView.findViewById(R.id.solNumberTextView);
    TextView temperatureTextView = convertView.findViewById(R.id.temperatureTextView);
    TextView pressureTextView = convertView.findViewById(R.id.pressureTextView);

    solNumberTextView.setText("Sol n°" + solKey);

    if (sol != null && sol.getTemperature() != null) {
      temperatureTextView.setText("Température " + String.format("%.2f", sol.getTemperature().getAverage()));
    }

    if (sol != null && sol.getPressure() != null) {
      pressureTextView.setText("Pression " + String.format("%.2f", sol.getPressure().getAverage()));
    }

    return convertView;
  }
}