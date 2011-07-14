package uk.ac.ebi.fgpt.pcascatterplot.model;

import java.util.Map;

public class Point {
  private Float scaledXPosition;
  private Float scaledYPosition;
  private double unscaledXPositoin;
  private double unscaledYPosition;
  private Map<String,Integer> annotations;
  
  public Point(double unscaledXPosition,
               double unscaledYPosition,
               float scaledXPosition,
               float scaledYPosition) {
    this.unscaledXPositoin = unscaledXPosition;
    this.unscaledYPosition = unscaledYPosition;
    this.scaledXPosition = scaledXPosition;
    this.scaledYPosition = scaledYPosition;
  }
  
  public Point(double unscaledXPosition, double unscaledYPosition) {
    this.unscaledXPositoin = unscaledXPosition;
    this.unscaledYPosition = unscaledYPosition;
  }
  
  public void setScaledXPosition(float scaledXPosition) {
    this.scaledXPosition = scaledXPosition;
  }
  
  public float getScaledXPosition() {
    return scaledXPosition;
  }
  
  public void setScaledYPosition(float scaledYPosition) {
    this.scaledYPosition = scaledYPosition;
  }
  
  public float getScaledYPosition() {
    return scaledYPosition;
  }
  
  public void setUnscaledXPositoin(double unscaledXPosition) {
    this.unscaledXPositoin = unscaledXPosition;
  }
  
  public double getUnscaledXPosition() {
    return unscaledXPositoin;
  }
  
  public void setUnscaledYPosition(double unscaledYPosition) {
    this.unscaledYPosition = unscaledYPosition;
  }
  
  public double getUnscaledYPosition() {
    return unscaledYPosition;
  }
  
  public void setAnnotations(Map<String,Integer> annotations) {
    this.annotations = annotations;
  }
  
  public Map<String,Integer> getAnnotations() {
    return annotations;
  }
}
