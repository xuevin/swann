package uk.ac.ebi.fgpt.swann.model;

public class Color {
  private int red;
  private int green;
  private int blue;
  
  public Color(int red, int green, int blue) {
    // TODO Think about putting checks for the range
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
  
  public String getCSSString() {
    String cssGreen = Integer.toHexString(green);
    String cssBlue = Integer.toHexString(blue);
    String cssRed = Integer.toHexString(red);
    cssRed = cssRed.length() == 2 ? cssRed : "0" + cssRed;
    cssGreen = cssGreen.length() == 2 ? cssGreen : "0" + cssGreen;
    cssBlue = cssBlue.length() == 2 ? cssBlue : "0" + cssBlue;
    
    return "#" + (cssRed + cssGreen + cssBlue);
  }
  
}
