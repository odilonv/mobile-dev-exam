package com.odilonvidal.exam.model;

import java.io.Serializable;

public class Pressure implements Serializable {
  private static final long serialVersionUID = 1L;
  private double average;
  private double minimum;
  private double maximum;
  private int count;

  public double getAverage() {
    return average;
  }

  public void setAverage(double average) {
    this.average = average;
  }

  public double getMinimum() {
    return minimum;
  }

  public void setMinimum(double minimum) {
    this.minimum = minimum;
  }

  public double getMaximum() {
    return maximum;
  }

  public void setMaximum(double maximum) {
    this.maximum = maximum;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}