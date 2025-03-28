package com.odilonvidal.exam.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherDataParser {

  public static MarsWeather parse(String jsonData) {
    try {
      Gson gson = new Gson();
      JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

      MarsWeather marsWeather = new MarsWeather();

      List<String> solKeys = new ArrayList<>();
      for (JsonElement element : jsonObject.getAsJsonArray("sol_keys")) {
        solKeys.add(element.getAsString());
      }
      marsWeather.setSolKeys(solKeys);

      Map<String, Sol> sols = new HashMap<>();
      for (String solKey : solKeys) {
        JsonObject solData = jsonObject.getAsJsonObject(solKey);
        Sol sol = new Sol();

        if (solData.has("AT")) {
          JsonObject atData = solData.getAsJsonObject("AT");
          Temperature temp = new Temperature();
          temp.setAverage(atData.get("av").getAsDouble());
          temp.setMinimum(atData.get("mn").getAsDouble());
          temp.setMaximum(atData.get("mx").getAsDouble());
          temp.setCount(atData.get("ct").getAsInt());
          sol.setTemperature(temp);
        }

        if (solData.has("PRE")) {
          JsonObject preData = solData.getAsJsonObject("PRE");
          Pressure pressure = new Pressure();
          pressure.setAverage(preData.get("av").getAsDouble());
          pressure.setMinimum(preData.get("mn").getAsDouble());
          pressure.setMaximum(preData.get("mx").getAsDouble());
          pressure.setCount(preData.get("ct").getAsInt());
          sol.setPressure(pressure);
        }

        if (solData.has("WD")) {
          JsonObject wdData = solData.getAsJsonObject("WD");
          Wind wind = new Wind();
          Map<String, Wind.WindDirection> directions = new HashMap<>();

          for (int i = 0; i < 16; i++) {
            String dirKey = String.valueOf(i);
            if (wdData.has(dirKey)) {
              JsonObject dirData = wdData.getAsJsonObject(dirKey);
              Wind.WindDirection direction = new Wind.WindDirection();
              direction.setCompassDegrees(dirData.get("compass_degrees").getAsDouble());
              direction.setCompassPoint(dirData.get("compass_point").getAsString());
              direction.setCompassRight(dirData.get("compass_right").getAsDouble());
              direction.setCompassUp(dirData.get("compass_up").getAsDouble());
              direction.setCount(dirData.get("ct").getAsInt());
              directions.put(dirKey, direction);
            }
          }
          wind.setDirections(directions);
          sol.setWind(wind);
        }

        sols.put(solKey, sol);
      }
      marsWeather.setSols(sols);

      return marsWeather;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}