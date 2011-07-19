package uk.ac.ebi.fgpt.pcascatterplot.model;

public class Vector {
  private double[] values;
  private Double max;
  private Double min;
  
  public Vector(int size) {
    values = new double[size];
  }
  
  public void setQuick(int position, double value) {
    values[position] = value;
    if (max == null) {
      max = value;
    } else {
      if (value > max) {
        max = value;
      }
    }
    if (min == null) {
      min = value;
    } else {
      if (value < min) {
        min = value;
      }
    }
    
  }
  
  public double getQuick(int position) {
    return values[position];
  }
  
  public double getMin() {
    return min;
  }
  
  public double getMax() {
    return max;
  }
  
  public int getSize() {
    return values.length;
  }
}
