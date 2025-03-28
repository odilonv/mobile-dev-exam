package com.odilonvidal.exam.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.odilonvidal.exam.model.WeatherRepository;
import com.odilonvidal.exam.model.MarsWeather;
import com.odilonvidal.exam.model.WeatherDataParser;

public class WeatherViewModel extends AndroidViewModel {

  private final MutableLiveData<MarsWeather> weatherData = new MutableLiveData<>();
  private final MutableLiveData<String> errorData = new MutableLiveData<>();
  private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
  private final WeatherRepository weatherRepository;

  public WeatherViewModel(Application application) {
    super(application);
    weatherRepository = new WeatherRepository();
  }

  public LiveData<MarsWeather> getWeatherData() {
    return weatherData;
  }

  public LiveData<String> getErrorData() {
    return errorData;
  }

  public LiveData<Boolean> getIsLoading() {
    return isLoading;
  }

  public void fetchWeatherData(String apiUrl) {
    isLoading.setValue(true);
    weatherRepository.fetchWeatherData(apiUrl, new WeatherRepository.WeatherCallback() {
      @Override
      public void onWeatherDataFetched(String data) {
        MarsWeather parsedData = WeatherDataParser.parse(data);
        if (parsedData != null) {
          weatherData.postValue(parsedData);
        } else {
          errorData.postValue("Erreur lors du parsing des données");
        }
        isLoading.postValue(false);
      }

      @Override
      public void onError(String error) {
        errorData.postValue(error);
        isLoading.postValue(false);
      }
    });
  }
}
