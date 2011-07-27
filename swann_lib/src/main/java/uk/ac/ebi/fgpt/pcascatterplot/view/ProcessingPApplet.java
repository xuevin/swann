package uk.ac.ebi.fgpt.pcascatterplot.view;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import uk.ac.ebi.fgpt.pcascatterplot.DataProviderImpl;
import uk.ac.ebi.fgpt.pcascatterplot.model.Color;
import uk.ac.ebi.fgpt.pcascatterplot.model.ColoredHashSetOfPoints;
import uk.ac.ebi.fgpt.pcascatterplot.model.Point;
import uk.ac.ebi.fgpt.pcascatterplot.model.ViewOfScaledScatterPlot;
import uk.ac.ebi.fgpt.pcascatterplot.model.utils.AnnotationUtils;

public class ProcessingPApplet extends PApplet {
  
  private ViewOfScaledScatterPlot view;
  
  private PGraphics plotBuffer;
  private PImage plotImg;
  
  private int firstDragCornerX;
  private int firstDragCornerY;
  
  private int annotationsRectX = 0;
  private int annotationsRectY = 0;
  
  private Set<String> experimentsInLastHighlight;
  private static final int ANNOTATIONSRECTSIZE = 20;
  private final int PLOT_OFFSET = 30;
  
  @Override
  public void setup() {
    size(800, 800, P2D);
    background(0);
    
    firstDragCornerX = -1;
    firstDragCornerY = -1;
    
    experimentsInLastHighlight = new HashSet<String>();
    
    try {
      textFont(new PFont(this.getClass().getClassLoader().getResourceAsStream("data/Arial-Black-14.vlw")));
    } catch (IOException e1) {
      e1.printStackTrace();
      System.err.println("The default font could not be found!");
    }
    
    // frameRate(30);
    view = new ViewOfScaledScatterPlot(width - 2 * PLOT_OFFSET, height - 2 * PLOT_OFFSET);
    
    File annotations = new File(args[0]);
    File coords = new File(args[1]);
    int pca1 = Integer.parseInt(args[2]);
    int pca2 = Integer.parseInt(args[3]);
    DataProviderImpl match;
    try {
      match = new DataProviderImpl(coords, annotations, pca1, pca2);
      
      Map<String,Color> mapToColor = new HashMap<String,Color>();
      mapToColor.put("cell_line", new Color(0, 255, 0));
      mapToColor.put("bone marrow", new Color(255, 255, 0));
      mapToColor.put("blood", new Color(255, 0, 0));
      mapToColor.put("brain", new Color(0, 0, 255));
      
      view.setColoredPoints(match.getAnnotatedPoints(), mapToColor); 
      // Map<String,Color> mapToColor2 = new HashMap<String,Color>();
      // mapToColor2.put("blood", new Color(0, 0, 255));
      // view.recolor(mapToColor2);
      
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    // Draw image into buffer
    drawPlotIntoBuffer();
  }
  
  @Override
  public void draw() {
    // System.out.println(frameRate);
    fill(0);
    background(0);
    drawView();
    drawSelectingRect();
    drawAxis();
    stroke(255);
  }
  
  private void drawView() {
    image(plotImg, PLOT_OFFSET, PLOT_OFFSET);
  }
  
  private void drawAxis() {
    stroke(255);
    line(PLOT_OFFSET, height - PLOT_OFFSET, width - PLOT_OFFSET, height - PLOT_OFFSET);
    line(PLOT_OFFSET, PLOT_OFFSET, PLOT_OFFSET, height - PLOT_OFFSET);
    
    fill(255);
    textAlign(RIGHT, CENTER);
    text(view.getYAxisUpper(), PLOT_OFFSET, PLOT_OFFSET);
    text(view.getYAxisLower(), PLOT_OFFSET, height - PLOT_OFFSET);
    text(view.getXAxisUpper(), width - PLOT_OFFSET, height - PLOT_OFFSET + 10);
    text(view.getXAxisLower(), PLOT_OFFSET, height - PLOT_OFFSET + 10);
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
    Collection<Point> set = view.extractEverythingInRectangularRegion(
      convertMouseXPositionToViewPosition(firstDragCornerX),
      convertMouseYPositionToViewPosition(firstDragCornerY), convertMouseXPositionToViewPosition(mouseX),
      convertMouseYPositionToViewPosition(mouseY));
    
    // for(Point po:set){
    // System.out.println(po.getSampleName());
    // }
    
    // DEBUGGING PRINTOUT
    // System.out.println("First X " + convertMouseXPositionToAbsolutePosition(firstDragCornerX));
    // System.out.println("First Y " + convertMouseYPositionToAbsolutePosition(firstDragCornerY));
    // System.out.println("Second X " + convertMouseXPositionToAbsolutePosition(mouseX));
    // System.out.println("Second Y " + convertMouseYPositionToAbsolutePosition(mouseY));
    Collection<Map.Entry<String,Integer>> annotations = AnnotationUtils.printAnnotations(set);
    
    experimentsInLastHighlight.clear();
    for (Map.Entry<String,Integer> pair : annotations) {
      if (pair.getKey().startsWith("E-")) {
        experimentsInLastHighlight.add(pair.getKey());
      }
    }
    drawPlotIntoBuffer();
    
    firstDragCornerX = -1;
    firstDragCornerY = -1;
  }
  
  @Override
  public void keyPressed() {
    processMovementRelatedKeys();
    processAnnotationRelatedKeys();
  }
  
  private void processAnnotationRelatedKeys() {
    if (key == 'i') {
      annotationsRectY -= 10;
    }
    if (key == 'k') {
      annotationsRectY += 10;
    }
    if (key == 'j') {
      annotationsRectX -= 10;
    }
    if (key == 'l') {
      annotationsRectX += 10;
    }
    if (key == 'i' || key == 'k') {
      AnnotationUtils.printAnnotations(view.extractEverythingInRectangularRegion(
        convertMouseXPositionToViewPosition(0), convertMouseXPositionToViewPosition(annotationsRectY),
        convertMouseXPositionToViewPosition(width),
        convertMouseXPositionToViewPosition(annotationsRectY + ANNOTATIONSRECTSIZE)));
      rect(0, annotationsRectY, width, ANNOTATIONSRECTSIZE);
    } else if (key == 'j' || key == 'l') {
      AnnotationUtils.printAnnotations(view.extractEverythingInRectangularRegion(
        convertMouseXPositionToViewPosition(annotationsRectX), convertMouseXPositionToViewPosition(0),
        convertMouseXPositionToViewPosition(annotationsRectX + ANNOTATIONSRECTSIZE),
        convertMouseXPositionToViewPosition(height)));
      rect(annotationsRectX, 0, ANNOTATIONSRECTSIZE, height);
    }
  }
  
  private void processMovementRelatedKeys() {
    if (key == 'x') {
      view.zoomIn();
      drawPlotIntoBuffer();
    }
    if (key == 'z') {
      view.zoomOut();
      drawPlotIntoBuffer();
    }
    if (key == 'a') {
      view.moveLeft();
      updateImg();
    }
    if (key == 'd') {
      view.moveRight();
      updateImg();
    }
    if (key == 'w') {
      view.moveUp();
      updateImg();
    }
    if (key == 's') {
      view.moveDown();
      updateImg();
    }
    if (key == 'q') {
      view.reset();
      drawPlotIntoBuffer();
    }
    
  }
  
  private int convertMouseXPositionToViewPosition(int position) {
    return position - PLOT_OFFSET + view.getPlotOffset_X();
  }
  
  private int convertMouseYPositionToViewPosition(int position) {
    return position - PLOT_OFFSET + view.getPlotOffset_Y();
  }
  
  private double convertMouseXPositionToAbsolutePosition(int xPosition) {
    int position;
    if (xPosition < PLOT_OFFSET) { // The mouse is too far left
      position = 0 + view.getPlotOffset_X();
    } else if (xPosition > view.getWidth() + PLOT_OFFSET) { // the mouse is too far right
      position = view.getWidth() + view.getPlotOffset_X();
    } else {
      position = view.getPlotOffset_X() + xPosition - PLOT_OFFSET;
    }
    double relativePosition = (double) position / (double) view.getPlotWidth();
    return relativePosition * (view.getXAxisUnscaledMax() - view.getXAxisUnscaledMin())
           + view.getXAxisUnscaledMin();
  }
  
  private double convertMouseYPositionToAbsolutePosition(int yPosition) {
    int position;
    if (yPosition < PLOT_OFFSET) { // The mouse is too far up
      position = 0 + view.getPlotOffset_Y();
    } else if (yPosition > view.getHeight() + PLOT_OFFSET) { // the mouse is too far down
      position = view.getHeight() + view.getPlotOffset_Y();
    } else {
      position = view.getPlotOffset_Y() + yPosition - PLOT_OFFSET;
    }
    double relativePosition = (double) position / (double) view.getPlotHeight();
    return view.getYAxisUnscaledMax()
           - (relativePosition * (view.getYAxisUnscaledMax() - view.getYAxisUnscaledMin()));
  }
  
  private void updateImg() {
    if (view.getPlotOffset_X() < 0 || view.getPlotOffset_Y() < 0 || view.getPlotOffset_X() > plotBuffer.width
        || view.getPlotOffset_Y() > plotBuffer.height) {
      view.reset();
    }
    plotImg = plotBuffer.get(view.getPlotOffset_X(), view.getPlotOffset_Y(), view.getWidth(), view
        .getHeight());
  }
  
  private void drawPlotIntoBuffer() {
    // STEP 0 SETUP
    plotBuffer = createGraphics(view.getPlotWidth(), view.getPlotHeight(), P2D);
    plotBuffer.beginDraw();
    // STEP 1 DRAW POINTS
    plotBuffer.rectMode(CENTER);
    for (ColoredHashSetOfPoints set : view.getSetsToDraw()) {
      plotBuffer.stroke(color(set.getColor().getRed(), set.getColor().getGreen(), set.getColor().getBlue()));
      for (Point point : set) {
        plotBuffer.point(point.getScaledXPosition(), point.getScaledYPosition());
        // STEP 2 WHILE ITERATING, DRAW BOXES AROUND THE EXPERIMENTS JUST HIGHLIGHTED
        for (String experimentAccession : experimentsInLastHighlight) {
          if (point.getAnnotations().contains(experimentAccession)) {
            plotBuffer.rect(point.getScaledXPosition(), point.getScaledYPosition(), 10, 10);
            break;
          }
        }
      }
    }
    plotBuffer.rectMode(CORNER);
    
    // STEP 3 END
    plotBuffer.endDraw();
    
    // STEP 3 UPDATE IMAGE
    updateImg();
  }
  
  private void drawSelectingRect() {
    if (firstDragCornerX != -1 && firstDragCornerY != -1) {
      fill(255, 80);
      rect(firstDragCornerX, firstDragCornerY, mouseX - firstDragCornerX, mouseY - firstDragCornerY);
    }
  }
}
