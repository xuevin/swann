package uk.ac.ebi.fgpt.swann.model;

import java.io.Serializable;
import java.util.Collection;

public class Point implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private int scaledXPosition;
  private int scaledYPosition;
  private double unscaledXPositoin;
  private double unscaledYPosition;
  private Collection<String> annotations;
  private String experiment;
  private String sampleName;
  
  public Point() {}
  
  public Point(double unscaledXPosition,
               double unscaledYPosition,
               int scaledXPosition,
               int scaledYPosition) {
    this.unscaledXPositoin = unscaledXPosition;
    this.unscaledYPosition = unscaledYPosition;
    this.scaledXPosition = scaledXPosition;
    this.scaledYPosition = scaledYPosition;
  }
  
  public Point(double unscaledXPosition, double unscaledYPosition) {
    this.unscaledXPositoin = unscaledXPosition;
    this.unscaledYPosition = unscaledYPosition;
  }
  
  public void setScaledXPosition(int scaledXPosition) {
    this.scaledXPosition = scaledXPosition;
  }
  
  public int getScaledXPosition() {
    return scaledXPosition;
  }
  
  public void setScaledYPosition(int scaledYPosition) {
    this.scaledYPosition = scaledYPosition;
  }
  
  public int getScaledYPosition() {
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
  
  public void setAnnotations(Collection<String> annotations) {
    this.annotations = annotations;
  }
  
  public Collection<String> getAnnotations() {
    return annotations;
  }
  
  public String getAnnotationsAsString() {
    StringBuilder builder = new StringBuilder();
    for (String string : getAnnotations()) {
      builder.append(string + "\t");
    }
    return builder.toString();
  }
  public void setExperiment(String experiment){
    this.experiment = experiment;
  }
  
  public String getExperiment() {
    return experiment;
  }
  public void setSampleName(String sampleName){
    this.sampleName = sampleName;
  }
  public String getSampleName(){
    return sampleName;
  }
}
