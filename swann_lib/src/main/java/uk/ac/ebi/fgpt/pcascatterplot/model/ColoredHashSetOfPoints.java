package uk.ac.ebi.fgpt.pcascatterplot.model;

import java.util.HashSet;

public class ColoredHashSetOfPoints extends HashSet<Point> {
  
  private int[] color;
  
  public ColoredHashSetOfPoints(int red, int green, int blue) {
    super();
    color = new int[3];
    color[0] = red;
    color[1] = green;
    color[2] = blue;
  }
  
  public int[] getColor() {
    return color;
  }
  
}
