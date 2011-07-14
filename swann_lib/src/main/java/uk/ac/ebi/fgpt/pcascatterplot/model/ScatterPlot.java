package uk.ac.ebi.fgpt.pcascatterplot.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScatterPlot {
  private Vector[] arrayOfVectors;
  private HashSet<ColoredHashSetOfPoints> pointsToDraw;
  
  private Vector xAxisVector;
  private Vector yAxisVector;
  
  private int numberOfDataPoints;
  
  private int width;
  private int height;
  
  public ScatterPlot(int width, int height, String[] arrayOfStrings) {
    this.width = width;
    this.height = height;
    
    // Store values as a 2D array (there is no header line)
    arrayOfVectors = convertArrayOfStringsIntoArrayOfVectors(arrayOfStrings);
    
    numberOfDataPoints = arrayOfStrings.length;
  }
  
  /**
   * Converts an array of strings, into an array of vectors.
   * 
   * @param arrayOfStrings
   *          an array of strings as represented in the document.
   * @return a 2D array of vectors. Each row represents a vector.
   */
  private Vector[] convertArrayOfStringsIntoArrayOfVectors(String[] arrayOfStrings) {
    Vector[] vectorArray = new Vector[arrayOfStrings[0].split("\t").length];
    
    // instantiate each vector
    for (int i = 0; i < vectorArray.length; i++) {
      vectorArray[i] = new Vector(arrayOfStrings.length);
    }
    
    // fill each vector
    int positionIndex = 0;
    for (String string : arrayOfStrings) {
      String[] row = string.split("\t");
      for (int vectorIndex = 0; vectorIndex < row.length; vectorIndex++) {
        vectorArray[vectorIndex].setQuick(positionIndex, Double.parseDouble(row[vectorIndex]));
      }
      positionIndex++;
    }
    return vectorArray;
  }
  
  public void setPlot(int vectorIndexForXAxis, int vectorIndexForYAxis) {
    xAxisVector = arrayOfVectors[vectorIndexForXAxis];
    yAxisVector = arrayOfVectors[vectorIndexForYAxis];
    update();
  }
  
  private void update() {
    pointsToDraw = new HashSet<ColoredHashSetOfPoints>();
    ColoredHashSetOfPoints bluePoints = new ColoredHashSetOfPoints(0, 0, 255);
    for (int i = 0; i < numberOfDataPoints; i++) {
      bluePoints.add(new Point(xAxisVector.getQuick(i), yAxisVector.getQuick(i), map(xAxisVector.getQuick(i),
        xAxisVector.getMin(), xAxisVector.getMax(), 0, width), map(yAxisVector.getQuick(i), yAxisVector
          .getMin(), yAxisVector.getMax(), height, 0)));
    }
    pointsToDraw.add(bluePoints);
  }
  
  public void setScaledDimensions(int width, int height) {
    this.width = width;
    this.height = height;
    update();
  }
  
  public Collection<ColoredHashSetOfPoints> getSetsToDraw() {
    return pointsToDraw;
  }
  
  public static float map(double value, double low1, double high1, float low2, float high2) {
    double range1 = high1 - low1;
    float range2 = high2 - low2;
    
    double dif = value - low1;
    double percent = dif / range1;
    float howFarFromLow2 = (float) (percent * range2);
    return low2 + howFarFromLow2;
  }
  
  public double getXAxisMin() {
    return xAxisVector.getMin();
  }
  
  public double getYAxisMin() {
    return yAxisVector.getMin();
  }
  
  public double getXAxisMax() {
    return xAxisVector.getMax();
  }
  
  public double getYAxisMax() {
    return yAxisVector.getMax();
  }
  
  public Set<Point> extractEverythingInRegionUnscaled(double xPosition1,
                                                      double yPosition1,
                                                      double xPosition2,
                                                      double yPosition2) {
    double largerX;
    double largerY;
    double smallerX;
    double smallerY;
    
    if (xPosition1 > xPosition2) {
      largerX = xPosition1;
      smallerX = xPosition2;
    } else {
      largerX = xPosition2;
      smallerX = xPosition1;
    }
    if (yPosition1 > yPosition2) {
      largerY = yPosition1;
      smallerY = yPosition2;
    } else {
      largerY = yPosition2;
      smallerY = yPosition1;
    }
    Set<Point> pointsThatFallInRegion = new HashSet<Point>();
    for (ColoredHashSetOfPoints set : pointsToDraw) {
      {
        for (Point point : set) {
          if (point.getUnscaledXPosition() >= smallerX && point.getUnscaledXPosition() <= largerX) {
            if (point.getUnscaledYPosition() >= smallerY && point.getUnscaledYPosition() <= largerY) {
              pointsThatFallInRegion.add(point);
            }
          }
        }
      }
    }
    return pointsThatFallInRegion;
  }
  
}
