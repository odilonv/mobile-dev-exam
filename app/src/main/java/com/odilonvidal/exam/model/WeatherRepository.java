package com.odilonvidal.exam.model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherRepository {

  public interface WeatherCallback {
    void onWeatherDataFetched(String data);

    void onError(String error);
  }

  public void fetchWeatherData(String apiUrl, WeatherCallback callback) {
    Log.d("WeatherRepository", "Début de la requête avec l'URL: " + apiUrl);
    new FetchWeatherDataTask(apiUrl, callback).execute();
  }

  private static class FetchWeatherDataTask extends AsyncTask<String, Void, String> {

    private final String apiUrl;
    private final WeatherCallback callback;

    FetchWeatherDataTask(String apiUrl, WeatherCallback callback) {
      this.apiUrl = apiUrl;
      this.callback = callback;
    }

    @Override
    protected String doInBackground(String... urls) {
      String result = null;
      HttpURLConnection urlConnection = null;

      try {
        URL url = new URL(apiUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(5000);
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        Log.d("WeatherRepository", "Code de réponse HTTP: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
          String line;
          StringBuilder response = new StringBuilder();
          while ((line = reader.readLine()) != null) {
            response.append(line);
          }
          result = response.toString();
          Log.d("WeatherRepository", "Réponse reçue: " + result);
        } else {
          Log.e("WeatherRepository", "Erreur HTTP: " + responseCode);
          callback.onError("Erreur HTTP: " + responseCode);
        }
      } catch (Exception e) {
        Log.e("WeatherRepository", "Erreur lors de la récupération des données", e);
        callback.onError("Erreur de connexion: " + e.getMessage());
      } finally {
        if (urlConnection != null) {
          urlConnection.disconnect();
        }
      }

      return result;
    }

    @Override
    protected void onPostExecute(String result) {
      if (result != null && !result.isEmpty()) {
        Log.d("WeatherRepository", "Données reçues avec succès");
        callback.onWeatherDataFetched(result);
      } else {
        Log.e("WeatherRepository", "Aucune donnée reçue");
        callback.onError("Aucune donnée reçue");
      }
    }
  }
}
