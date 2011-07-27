package uk.ac.ebi.fgpt.pcascatterplot.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import uk.ac.ebi.fgpt.pcascatterplot.model.utils.MathUtils;

public class ScaledScatterPlot {
  public enum Type {
    SCALED,
    UNSCALED
  }
  
  private Collection<ColoredHashSetOfPoints> pointsToDraw;
  
  private int numberOfDataPoints;
  
  private double unscaledMin_X = Double.MAX_VALUE;
  private double unscaledMin_Y = Double.MAX_VALUE;
  private double unscaledMax_X = Double.NEGATIVE_INFINITY;
  private double unscaledMax_Y = Double.NEGATIVE_INFINITY;
  
  private int width;
  private int height;
  
  public ScaledScatterPlot(int width, int height) {
    this.width = width;
    this.height = height;
    this.pointsToDraw = new HashSet<ColoredHashSetOfPoints>();
    this.numberOfDataPoints = 0;
  }
  
  public void addColoredPointsToScatterPlot(Collection<Point> points, Color color) {
    // Update scale
    updateMinAndMax(points);
    
    // Give points a color
    ColoredHashSetOfPoints coloredPoints = new ColoredHashSetOfPoints(color);
    coloredPoints.addAll(points);
    this.pointsToDraw.add(coloredPoints);
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
  
  public Collection<Point> extractEverythingInRectangularRegion(float xPosition1,
                                                                float yPosition1,
                                                                float xPosition2,
                                                                float yPosition2,
                                                                Type type) {
    long time = System.currentTimeMillis();
    
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
    Collection<Point> pointsThatFallInRegion = new ArrayList<Point>(); // what data type should this be...
    if (type == Type.SCALED) {
      for (ColoredHashSetOfPoints set : pointsToDraw) {
        for (Point point : set) {
          int scaledX = point.getScaledXPosition();
          int scaledY = point.getScaledYPosition();
          if (scaledX >= smallerX && scaledX <= largerX && scaledY >= smallerY && scaledY <= largerY) {
            pointsThatFallInRegion.add(point);
          }
        }
      }
    } else {
      for (ColoredHashSetOfPoints set : pointsToDraw) {
        for (Point point : set) {
          double unscaledX = point.getUnscaledXPosition();
          double unscaledY = point.getUnscaledYPosition();
          if (unscaledX >= smallerX && unscaledX <= largerX && unscaledY >= smallerY && unscaledY <= largerY) {
            pointsThatFallInRegion.add(point);
          }
        }
      }
    }
    
    System.out.println("Extracting from a region took :" + (System.currentTimeMillis() - time));
    return pointsThatFallInRegion;
  }
  
  public void clearAllPoints() {
    this.pointsToDraw.clear();
  }
  
  public void setColoredPoints(Collection<Point> annotatedPoints, Map<String,Color> termToColor) {
    long time = System.currentTimeMillis();
    updateMinAndMax(annotatedPoints);
    this.numberOfDataPoints = annotatedPoints.size();
    
    HashMap<String,ColoredHashSetOfPoints> termToSet = new HashMap<String,ColoredHashSetOfPoints>();
    for (String term : termToColor.keySet()) {
      termToSet.put(term, new ColoredHashSetOfPoints(termToColor.get(term)));
    }
    termToSet.put("other", new ColoredHashSetOfPoints(new Color(155, 155, 155)));
    
    // Iterate through all of the points
    for (Point point : annotatedPoints) {
      termToSet.get(getGroup(point.getAnnotations(), termToColor)).add(point);
    }
    pointsToDraw = termToSet.values();
    update();
    System.out.println("Coloring takes " + (System.currentTimeMillis() - time));
    
  }
  
  public void recolor(Map<String,Color> termToColor) {
    long time = System.currentTimeMillis();
    HashMap<String,ColoredHashSetOfPoints> termToSet = new HashMap<String,ColoredHashSetOfPoints>();
    for (String term : termToColor.keySet()) {
      termToSet.put(term, new ColoredHashSetOfPoints(termToColor.get(term)));
    }
    termToSet.put("other", new ColoredHashSetOfPoints(new Color(155, 155, 155)));
    
    // Iterate through all of the points
    for (ColoredHashSetOfPoints set : pointsToDraw) {
      for (Point point : set) {
        termToSet.get(getGroup(point.getAnnotations(), termToColor)).add(point);
      }
    }
    pointsToDraw = termToSet.values();
    System.out.println("Recoloring takes " + (System.currentTimeMillis() - time));
  }
  
  private String getGroup(Collection<String> annotations, Map<String,Color> termToColor) {
    String termGroup = "other";
    boolean foundOnce = false;
    for (String term : termToColor.keySet()) {
      if (annotations.contains(term) && foundOnce == false) {
        termGroup = term;
        foundOnce = true;
      } else if (annotations.contains(term) && foundOnce == true) {
        termGroup = "other";
        foundOnce = false;
        break;
      } else {}
    }
    return termGroup;
  }
}
