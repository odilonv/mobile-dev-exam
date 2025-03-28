package com.odilonvidal.exam.model;

import java.io.Serializable;

public class Sol implements Serializable {
  private static final long serialVersionUID = 1L;
  private Temperature temperature;
  private Pressure pressure;
  private Wind wind;

  public Temperature getTemperature() {
    return temperature;
  }

  public void setTemperature(Temperature temperature) {
    this.temperature = temperature;
  }

  public Pressure getPressure() {
    return pressure;
  }

  public void setPressure(Pressure pressure) {
    this.pressure = pressure;
  }

  public Wind getWind() {
    return wind;
  }

  public void setWind(Wind wind) {
    this.wind = wind;
  }
}