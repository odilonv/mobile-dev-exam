package com.odilonvidal.exam.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MarsWeather implements Serializable {
  private static final long serialVersionUID = 1L;
  private List<String> solKeys;
  private Map<String, Sol> sols;

  public List<String> getSolKeys() {
    return solKeys;
  }

  public void setSolKeys(List<String> solKeys) {
    this.solKeys = solKeys;
  }

  public Map<String, Sol> getSols() {
    return sols;
  }

  public void setSols(Map<String, Sol> sols) {
    this.sols = sols;
  }
}