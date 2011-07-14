package uk.ac.ebi.fgpt.pcascatterplot;

import java.io.File;
import java.util.Set;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import uk.ac.ebi.fgpt.pcascatterplot.model.ColoredHashSetOfPoints;
import uk.ac.ebi.fgpt.pcascatterplot.model.Point;
import uk.ac.ebi.fgpt.pcascatterplot.model.ScatterPlot;

public class ProcessingPApplet extends PApplet {
  private ScatterPlot scatterPlot;
  
  private PGraphics plotBuffer;
  private PImage plotImg;
  
  private float zoom;
  private int bufferXCoord;
  private int bufferYCoord;
  
  private final float ZOOMSTEP = 0.1f;
  private final int PIXELSTEP = 10;
  private final int PLOT_BORDER = 30;
  
  private int firstDragCornerX;
  private int firstDragCornerY;
  
  @Override
  public void setup() {
    size(400, 400, P2D);
    background(0);
    
    firstDragCornerX = -1;
    firstDragCornerY = -1;
    
    zoom = 1;
    
    textFont(loadFont(this.getClass().getClassLoader().getResource("Arial-Black-14.vlw").getPath()));
    
    // frameRate(2000);
    // System.out.println(args[0]);
    
    String[] arrayOfStrings = loadStrings(new File(this.getClass().getClassLoader().getResource(
//      "assembly2_30915x3.csv").getFile()));
     "mock_data.txt").getFile()));
    
    scatterPlot = new ScatterPlot(width, height, arrayOfStrings);
    scatterPlot.setPlot(0, 1);
    
    // Draw image into buffer
    bufferXCoord = 0;
    bufferYCoord = 0;
    drawPlotIntoBuffer();
    
  }
  
  @Override
  public void draw() {
    fill(0);
    background(0);
    image(plotImg, PLOT_BORDER, PLOT_BORDER);
    // System.out.println(frameRate);
    drawSelectingRect();
    // System.out.println(frameRate);
    drawAxis();
    
    stroke(255);
  }
  
  private void drawAxis() {
    // TODO If you want this to be faster, push this into a buffer. (It won't make much of a difference)
    stroke(255);
    line(PLOT_BORDER, height - PLOT_BORDER, width - PLOT_BORDER, height - PLOT_BORDER);
    line(PLOT_BORDER, PLOT_BORDER, PLOT_BORDER, height - PLOT_BORDER);
    
    fill(255);
    textAlign(RIGHT, CENTER);
    text(getYAxisUpper(), PLOT_BORDER, PLOT_BORDER);
    text(getYAxisLower(), PLOT_BORDER, height - PLOT_BORDER);
    text(getXAxisUpper(), width - PLOT_BORDER, height - PLOT_BORDER + 10);
    text(getXAxisLower(), PLOT_BORDER, height - PLOT_BORDER + 10);
  }
  
  private int getYAxisUpper() {
    double range = scatterPlot.getYAxisMax() - scatterPlot.getYAxisMin();
    double relativePositionTop = bufferYCoord / (double) bufferHeight();
    double topValue = scatterPlot.getYAxisMax() - (range * relativePositionTop);
    
    // System.out.println(topValue);
    // System.out.println(bottomValue);
    
    return (int) topValue;
  }
  
  private int getYAxisLower() {
    double range = scatterPlot.getYAxisMax() - scatterPlot.getYAxisMin();
    double relativePositionBottom = (bufferYCoord + plotHeight()) / (double) bufferHeight();
    double bottomValue = scatterPlot.getYAxisMax() - (range * relativePositionBottom);
    return (int) bottomValue;
  }
  
  private int getXAxisLower() {
    double range = scatterPlot.getXAxisMax() - scatterPlot.getXAxisMin();
    double relativeLeft = (bufferXCoord) / (double) bufferWidth();
    double xAxisLower = scatterPlot.getXAxisMin() + (range * relativeLeft);
    return (int) xAxisLower;
  }
  
  private int getXAxisUpper() {
    double range = scatterPlot.getXAxisMax() - scatterPlot.getXAxisMin();
    double relativeRight = (bufferXCoord + plotWidth()) / (double) bufferWidth();
    double xAxisUpper = scatterPlot.getXAxisMin() + (range * relativeRight);
    return (int) xAxisUpper;
  }
  
  @Override
  public void mousePressed() {
    if (firstDragCornerX == -1 && firstDragCornerY == -1) {
      firstDragCornerX = mouseX;
      firstDragCornerY = mouseY;
    }
  }
  
  @Override
  public void mouseReleased() {
    //TODO This may be a dumb idea... The points in the scatter plot already have scaled values
    Set set = scatterPlot.extractEverythingInRegionUnscaled(convertXPositionToAbsolutePosition(firstDragCornerX),
      convertYPositionToAbsolutePosition(firstDragCornerY),
      convertXPositionToAbsolutePosition(mouseX),
      convertYPositionToAbsolutePosition(mouseY));
    
//    System.out.println("First X " + convertXPositionToAbsolutePosition(firstDragCornerX));
//    System.out.println("First Y " + convertYPositionToAbsolutePosition(firstDragCornerY));
//    System.out.println("Second X " + convertXPositionToAbsolutePosition(mouseX));
//    System.out.println("Second Y " + convertYPositionToAbsolutePosition(mouseY));
    System.out.println(set.size());
        
    // System.out.println((firstDragCornerX - PLOT_BORDER) + "\t" + (firstDragCornerY - PLOT_BORDER));
    // System.out.println((mouseX - PLOT_BORDER) + "\t" + (mouseY - PLOT_BORDER));
    firstDragCornerX = -1;
    firstDragCornerY = -1;
  }
  
  @Override
  public void keyPressed() {
    if (key == 'x') {
      zoom += ZOOMSTEP;
      drawPlotIntoBuffer();
    }
    if (key == 'z') {
      zoom -= ZOOMSTEP;
      if ((bufferXCoord - PIXELSTEP) >= 0) {
        bufferXCoord -= PIXELSTEP;
      }
      if ((bufferYCoord - PIXELSTEP) >= 0) {
        bufferYCoord -= PIXELSTEP;
      }
      drawPlotIntoBuffer();
    }
    if (key == 'a' && (bufferXCoord - PIXELSTEP) >= 0) {
      bufferXCoord -= PIXELSTEP;
      updateImg();
    }
    if (key == 'd' && (bufferXCoord + PIXELSTEP + plotWidth()) < plotBuffer.width) {
      bufferXCoord += PIXELSTEP;
      updateImg();
    }
    if (key == 'w' && (bufferYCoord - PIXELSTEP) >= 0) {
      bufferYCoord -= PIXELSTEP;
      updateImg();
    }
    if (key == 's' && (bufferYCoord + plotHeight() + PIXELSTEP) < plotBuffer.height) {
      bufferYCoord += PIXELSTEP;
      updateImg();
    }
  }
  
  private double convertXPositionToAbsolutePosition(int xPosition) {
    int position;
    if (xPosition < PLOT_BORDER) { // The mouse is too far left
      position = 0 + bufferXCoord;
    } else if (xPosition > plotWidth() + PLOT_BORDER) { // the mouse is too far right
      position = plotWidth() + bufferXCoord;
    } else {
      position = bufferXCoord + xPosition - PLOT_BORDER;
    }
    double relativePosition = (double) position / (double) bufferWidth();
    return relativePosition * (scatterPlot.getXAxisMax() - scatterPlot.getXAxisMin())
           + scatterPlot.getXAxisMin();
  }
  
  private double convertYPositionToAbsolutePosition(int yPosition) {
    int position;
    if (yPosition < PLOT_BORDER) { // The mouse is too far up
      position = 0 + bufferYCoord;
    } else if (yPosition > plotHeight() + PLOT_BORDER) { // the mouse is too far down
      position = plotHeight() + bufferYCoord;
    } else {
      position = bufferYCoord + yPosition - PLOT_BORDER;
    }
    double relativePosition = (double) position / (double) bufferHeight();
    return scatterPlot.getYAxisMax()
           - (relativePosition * (scatterPlot.getYAxisMax() - scatterPlot.getYAxisMin()));
  }
  
  private void updateImg() {
    if (bufferXCoord < 0 || bufferYCoord < 0 || bufferXCoord > plotBuffer.width
        || bufferYCoord > plotBuffer.height) {
      bufferXCoord = 0;
      bufferYCoord = 0;
    }
    plotImg = plotBuffer.get(bufferXCoord, bufferYCoord, plotWidth(), plotHeight());
  }
  
  // This is a function because the width can change (if the user rescales)
  private int plotWidth() {
    return width - 2 * PLOT_BORDER;
  }
  
  // This is a function because the width can change (if the user rescales)
  private int plotHeight() {
    return height - 2 * PLOT_BORDER;
  }
  
  private int bufferWidth() {
    return (int) (plotWidth() * zoom);
  }
  
  private int bufferHeight() {
    return (int) (plotHeight() * zoom);
  }
  
  // The image in the buffer is zoom times larger
  private void drawPlotIntoBuffer() {
    scatterPlot.setScaledDimensions(bufferWidth(), bufferHeight());
    plotBuffer = createGraphics(bufferWidth(), bufferHeight(), P2D);
    plotBuffer.beginDraw();

    for (ColoredHashSetOfPoints set : scatterPlot.getSetsToDraw()) {
      plotBuffer.strokeWeight(10);// TODO Delete
      plotBuffer.stroke(color(set.getColor()[0], set.getColor()[1], set.getColor()[2]));
      for (Point point : set) {
        plotBuffer.point(point.getScaledXPosition(), point.getScaledYPosition());
      }
    }
    plotBuffer.endDraw();
    updateImg();
  }
  
  private void drawSelectingRect() {
    if (firstDragCornerX != -1 && firstDragCornerY != -1) {
      fill(255, 80);
      rect(firstDragCornerX, firstDragCornerY, mouseX - firstDragCornerX, mouseY - firstDragCornerY);
    }
  }
}
