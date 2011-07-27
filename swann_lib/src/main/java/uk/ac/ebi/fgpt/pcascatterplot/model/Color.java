package uk.ac.ebi.fgpt.pcascatterplot.model;

public class Color {
  private int red;
  private int green;
  private int blue;
  
  public Color(int red, int green, int blue) {
    //TODO Think about putting checks for the range 
    this.red = red;
    this.green = green;
    this.blue = blue;
  }
  
  public int getRed() {
    return red;
  }
  
  public int getGreen() {
    return green;
  }
  
  public int getBlue() {
    return blue;
  }
  
}
