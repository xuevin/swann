package uk.ac.ebi.fgpt.swann.model;

import java.util.HashSet;

public class ColoredHashSetOfPoints extends HashSet<Point> {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Color color;
  
  public ColoredHashSetOfPoints(Color color) {
    super();
    this.color = color;
  }
  
  public ColoredHashSetOfPoints(int red, int green, int blue) {
    super();
    color = new Color(red, green, blue);
  }
  
  public Color getColor() {
    return color;
  }
  
}
