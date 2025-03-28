package com.odilonvidal.exam.model;

import java.io.Serializable;
import java.util.Map;

public class Wind implements Serializable {
  private static final long serialVersionUID = 1L;
  private Map<String, WindDirection> directions;

  public Map<String, WindDirection> getDirections() {
    return directions;
  }

  public void setDirections(Map<String, WindDirection> directions) {
    this.directions = directions;
  }

  public static class WindDirection implements Serializable {
    private static final long serialVersionUID = 2L;
    private double compassDegrees;
    private String compassPoint;
    private double compassRight;
    private double compassUp;
    private int count;

    public double getCompassDegrees() {
      return compassDegrees;
    }

    public void setCompassDegrees(double compassDegrees) {
      this.compassDegrees = compassDegrees;
    }

    public String getCompassPoint() {
      return compassPoint;
    }

    public void setCompassPoint(String compassPoint) {
      this.compassPoint = compassPoint;
    }

    public double getCompassRight() {
      return compassRight;
    }

    public void setCompassRight(double compassRight) {
      this.compassRight = compassRight;
    }

    public double getCompassUp() {
      return compassUp;
    }

    public void setCompassUp(double compassUp) {
      this.compassUp = compassUp;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }
  }
}