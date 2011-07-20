package uk.ac.ebi.fgpt.pcascatterplot.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ebi.fgpt.pcascatterplot.utils.MathUtils;

public class ScaledScatterPlot {
  public enum Type {
    SCALED,
    UNSCALED
  }
  
  private HashSet<ColoredHashSetOfPoints> pointsToDraw;
  
  private int numberOfDataPoints;
  
  private Double unscaledMin_X;
  private Double unscaledMax_X;
  private Double unscaledMin_Y;
  private Double unscaledMax_Y;
  
  private int width;
  private int height;
  
  public ScaledScatterPlot(int width, int height) {
    this.width = width;
    this.height = height;
    this.pointsToDraw = new HashSet<ColoredHashSetOfPoints>();
    this.numberOfDataPoints = 0;
  }
  
  public void addPointsToScatterPlot(Collection<Point> points, int red, int green, int blue) {
    // Update scale
    updateMinAndMax(points);
    // Give points a color
    ColoredHashSetOfPoints bluePoints = new ColoredHashSetOfPoints(red, green, blue);
    bluePoints.addAll(points);
    this.pointsToDraw.add(bluePoints);
    this.numberOfDataPoints = this.numberOfDataPoints + points.size();
    // update all positions
    update();
  }
  
  private void updateMinAndMax(Collection<Point> points) {
    double xUnscaled;
    double yUnscaled;
    for (Point point : points) {
      xUnscaled = point.getUnscaledXPosition();
      yUnscaled = point.getUnscaledYPosition();
      try {
        if (xUnscaled < unscaledMin_X) {
          unscaledMin_X = xUnscaled;
        }
        if (xUnscaled > unscaledMax_X) {
          unscaledMax_X = xUnscaled;
        }
        if (yUnscaled < unscaledMin_Y) {
          unscaledMin_Y = yUnscaled;
        }
        if (yUnscaled > unscaledMax_Y) {
          unscaledMax_Y = yUnscaled;
        }
      } catch (NullPointerException e) {
        unscaledMin_X = xUnscaled;
        unscaledMax_X = xUnscaled;
        unscaledMin_Y = yUnscaled;
        unscaledMax_Y = yUnscaled;
      }
      
    }
    
  }
  
  private void update() {
    // iterate through the colors
    for (ColoredHashSetOfPoints set : pointsToDraw) {
      // then iterate through the points in each color
      for (Point point : set) {
        point.setScaledXPosition(MathUtils.map(point.getUnscaledXPosition(), unscaledMin_X, unscaledMax_X, 0,
          width));
        // Flipping the map scale because Y coordinate is how far it is from the top
        point.setScaledYPosition(MathUtils.map(point.getUnscaledYPosition(), unscaledMin_Y, unscaledMax_Y,
          height, 0));
      }
    }
  }
  
  public void setScaledDimensions(int width, int height) {
    this.width = width;
    this.height = height;
    update();
  }
  
  public Collection<ColoredHashSetOfPoints> getSetsToDraw() {
    return pointsToDraw;
  }
  
  public double getXAxisUnscaledMin() {
    return unscaledMin_X;
  }
  
  public double getYAxisUnscaledMin() {
    return unscaledMin_Y;
  }
  
  public double getXAxisUnscaledMax() {
    return unscaledMax_X;
  }
  
  public double getYAxisUnscaledMax() {
    return unscaledMax_Y;
  }
  
  public Set<Point> extractEverythingInRectangularRegion(float xPosition1,
                                                         float yPosition1,
                                                         float xPosition2,
                                                         float yPosition2,
                                                         Type type) {
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
          if (type == Type.SCALED) {
            if (point.getScaledXPosition() >= smallerX && point.getScaledXPosition() <= largerX) {
              if (point.getScaledYPosition() >= smallerY && point.getScaledYPosition() <= largerY) {
                pointsThatFallInRegion.add(point);
              }
            }
          } else if (type == Type.UNSCALED) {
            if (point.getUnscaledXPosition() >= smallerX && point.getUnscaledXPosition() <= largerX) {
              if (point.getUnscaledYPosition() >= smallerY && point.getUnscaledYPosition() <= largerY) {
                pointsThatFallInRegion.add(point);
              }
            }
          }
        }
      }
    }
    return pointsThatFallInRegion;
  }
}
