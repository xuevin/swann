package uk.ac.ebi.fgpt.swann.model;

import java.util.Collection;

public class ViewOfScaledScatterPlot extends ScaledScatterPlot {
  
  private float zoom;
  private int plotOffset_X;
  private int plotOffset_Y;
  
  private final float ZOOMSTEP = 0.1f;
  private final int PIXELSTEP = 10;
  
  private int width;
  private int height;
  private int plotWidth;
  private int plotHeight;
  
  public ViewOfScaledScatterPlot(int width, int height) {
    super(width, height);
    this.zoom = 1;
    this.width = width;
    this.height = height;
    this.plotWidth = (int) (width * zoom);
    this.plotHeight = (int) (height * zoom);
    this.plotOffset_X = 0;
    this.plotOffset_Y = 0;
  }
  
  public void zoomIn() {
    zoom += ZOOMSTEP;
    this.plotWidth = (int) (width * zoom);
    this.plotHeight = (int) (height * zoom);
    setScaledDimensions(getPlotWidth(), getPlotHeight());
  }
  
  public void zoomOut() {
    zoom -= ZOOMSTEP;
    if ((plotOffset_X - PIXELSTEP) >= 0) {
      plotOffset_X -= PIXELSTEP;
    }
    if ((plotOffset_Y - PIXELSTEP) >= 0) {
      plotOffset_Y -= PIXELSTEP;
    }
    this.plotWidth = (int) (width * zoom);
    this.plotHeight = (int) (height * zoom);
    setScaledDimensions(getPlotWidth(), getPlotHeight());
  }
  
  public void moveLeft() {
    if ((plotOffset_X - PIXELSTEP) >= 0) {
      plotOffset_X -= PIXELSTEP;
    }
  }
  
  public void moveRight() {
    if ((plotOffset_X + PIXELSTEP + width) < getPlotWidth()) {
      plotOffset_X += PIXELSTEP;
    }
  }
  
  public void moveDown() {
    if ((plotOffset_Y + height + PIXELSTEP) < getPlotHeight()) {
      plotOffset_Y += PIXELSTEP;
    }
  }
  
  public void moveUp() {
    if ((plotOffset_Y - PIXELSTEP) >= 0) {
      plotOffset_Y -= PIXELSTEP;
    }
  }
  
  public int getYAxisUpper() {
    double range = getYAxisUnscaledMax() - getYAxisUnscaledMin();
    double relativePositionTop = plotOffset_Y / (double) getPlotHeight();
    double topValue = getYAxisUnscaledMax() - (range * relativePositionTop);
    
    return (int) topValue;
  }
  
  public int getYAxisLower() {
    double range = getYAxisUnscaledMax() - getYAxisUnscaledMin();
    double relativePositionBottom = (plotOffset_Y + height) / (double) getPlotHeight();
    double bottomValue = getYAxisUnscaledMax() - (range * relativePositionBottom);
    return (int) bottomValue;
  }
  
  public int getXAxisLower() {
    double range = getXAxisUnscaledMax() - getXAxisUnscaledMin();
    double relativeLeft = (plotOffset_X) / (double) getPlotWidth();
    double xAxisLower = getXAxisUnscaledMin() + (range * relativeLeft);
    return (int) xAxisLower;
  }
  
  public int getXAxisUpper() {
    double range = getXAxisUnscaledMax() - getXAxisUnscaledMin();
    double relativeRight = (plotOffset_X + height) / (double) getPlotWidth();
    double xAxisUpper = getXAxisUnscaledMin() + (range * relativeRight);
    return (int) xAxisUpper;
  }
  
  public int getPlotWidth() {
    return plotWidth;
    
  }
  
  public int getPlotHeight() {
    return plotHeight;
  }
  
  public Collection<Point> extractEverythingInRectangularRegion(int xPosition1,
                                                                int yPosition1,
                                                                int xPosition2,
                                                                int yPosition2) {
    return extractEverythingInRectangularRegion(xPosition1, yPosition1, xPosition2, yPosition2, Type.SCALED);
  }
  
  public void reset() {
    plotOffset_X = 0;
    plotOffset_Y = 0;
    zoom = 1;
    setScaledDimensions(getPlotWidth(), getPlotHeight());
  }
  
  public int getPlotOffset_X() {
    return plotOffset_X;
  }
  
  public int getPlotOffset_Y() {
    return plotOffset_Y;
  }
  
  public int getWidth() {
    return width;
  }
  
  public int getHeight() {
    return height;
  }
}
