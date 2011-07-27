package uk.ac.ebi.fgpt.pcascatterplot.model;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.fgpt.pcascatterplot.model.ScaledScatterPlot.Type;

public class ScaledScatterPlotTest {
  
  private ScaledScatterPlot newScatterPlot;
  
  @Before
  public void setUp() throws Exception {
    newScatterPlot = new ScaledScatterPlot(200, 200);
  }
  
  @Test
  public void assertSetupIsNotNull() {
    assertNotNull(newScatterPlot);
  }
  
  @Test
  public void showThatAllValuesAreFetchableWithNoPoints() {
    assertEquals(0, newScatterPlot.getSetsToDraw().size());
  }
  
  @Test
  public void showThatAddingOnePointChangesTheRange() {
    // Normally you don't want to plot only one point... You will get a divide by zero error.
    Collection<Point> points = new HashSet<Point>();
    points.add(new Point(100, 100));
    
    newScatterPlot.addColoredPointsToScatterPlot(points, new Color(0, 0, 255));
    assertEquals(100, newScatterPlot.getXAxisUnscaledMax(), 0);
    assertEquals(100, newScatterPlot.getXAxisUnscaledMin(), 0);
    assertEquals(100, newScatterPlot.getYAxisUnscaledMax(), 0);
    assertEquals(100, newScatterPlot.getYAxisUnscaledMin(), 0);
    
  }
  
  @Test
  public void showThatAddingNegativeNumbersChangeTheRange() {
    // Normally you don't want to plot only one point... You will get a divide by zero error.
    Collection<Point> points = new HashSet<Point>();
    points.add(new Point(-100, -100));
    
    newScatterPlot.addColoredPointsToScatterPlot(points, new Color(0, 0, 255));
    assertEquals(-100, newScatterPlot.getXAxisUnscaledMax(), 0);
    assertEquals(-100, newScatterPlot.getXAxisUnscaledMin(), 0);
    assertEquals(-100, newScatterPlot.getYAxisUnscaledMax(), 0);
    assertEquals(-100, newScatterPlot.getYAxisUnscaledMin(), 0);
  }
  
  @Test
  public void testExtractEverythingInRegion() {
    assertEquals(0, newScatterPlot.extractEverythingInRectangularRegion(Float.MIN_VALUE, Float.MIN_VALUE,
      Float.MAX_VALUE, Float.MAX_VALUE, Type.SCALED).size());
    assertEquals(0, newScatterPlot.extractEverythingInRectangularRegion(Float.MIN_VALUE, Float.MIN_VALUE,
      Float.MAX_VALUE, Float.MAX_VALUE, Type.UNSCALED).size());
    
    Collection<Point> points = new HashSet<Point>();
    points.add(new Point(100, 100));
    points.add(new Point(50, 50));
    points.add(new Point(0, 0));
    newScatterPlot.addColoredPointsToScatterPlot(points, new Color(0, 0, 255));
    assertEquals(3, newScatterPlot.extractEverythingInRectangularRegion(0, 0, 100, 100, Type.UNSCALED).size());
    assertEquals(2, newScatterPlot.extractEverythingInRectangularRegion(50, 50, 100, 100, Type.UNSCALED)
        .size());
    assertEquals(1, newScatterPlot.extractEverythingInRectangularRegion(55, 55, 100, 100, Type.UNSCALED)
        .size());
    
    // Remember, when using scaled, it is calculating how far it is from the top
    assertEquals(3, newScatterPlot.extractEverythingInRectangularRegion(0, 200, 200, 0, Type.SCALED).size());
    assertEquals(2, newScatterPlot.extractEverythingInRectangularRegion(100, 100, 200, 0, Type.SCALED).size());
    assertEquals(1, newScatterPlot.extractEverythingInRectangularRegion(101, 99, 200, 0, Type.SCALED).size());
  }
  
}
