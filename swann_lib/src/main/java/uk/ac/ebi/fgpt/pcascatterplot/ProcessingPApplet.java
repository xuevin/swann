package uk.ac.ebi.fgpt.pcascatterplot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import uk.ac.ebi.fgpt.pcascatterplot.model.ColoredHashSetOfPoints;
import uk.ac.ebi.fgpt.pcascatterplot.model.Matcher;
import uk.ac.ebi.fgpt.pcascatterplot.model.Point;
import uk.ac.ebi.fgpt.pcascatterplot.model.EmptyScatterPlot;
import uk.ac.ebi.fgpt.pcascatterplot.model.EmptyScatterPlot.Type;

public class ProcessingPApplet extends PApplet {
  
  private EmptyScatterPlot scatterPlot;
  
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
  
  private int annotationsRectX = 0;
  private int annotationsRectY = 0;
  
  private Set<String> experimentsInLastHighlight;
  private static final int ANNOTATIONSRECTSIZE = 20;
  
  @Override
  public void setup() {
    size(800, 800, P2D);
    background(0);
    frame.setResizable(true);
    
    firstDragCornerX = -1;
    firstDragCornerY = -1;
    zoom = 1;
    
    experimentsInLastHighlight = new HashSet<String>();
    
    textFont(loadFont(this.getClass().getClassLoader().getResource("Arial-Black-14.vlw").getPath()));
    
    // frameRate(30);
    scatterPlot = new EmptyScatterPlot(width, height);
    
    File annotations = new File(args[0]);
    File coords = new File(args[1]);
    Matcher match;
    try {
      match = new Matcher(coords, annotations);
      // colorPoints(match.getAnnotatedPoints(), "cell_line", "brain", "bone marrow","blood");
      // colorPoints(match.getAnnotatedPoints(), "cell_line", "bone marrow","blood");
      colorPoints(match.getAnnotatedPoints(), "cell_line", "bone marrow", "blood", "brain"); // Why does
      // adding lung remove this entire bottom section?
      // scatterPlot.addPointsToScatterPlot(match.getAnnotatedPoints(), 0, 0, 255);
      // colorPoints(match.getAnnotatedPoints(), "E-GEOD-8507");
      
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    // Draw image into buffer
    bufferXCoord = 0;
    bufferYCoord = 0;
    drawPlotIntoBuffer();
    
  }
  
  private void colorPoints(Collection<Point> annotatedPoints, String... termsToFilterOn) {
    HashSet<Point> annotatedPointsCopy = new HashSet<Point>(annotatedPoints);
    int[][] colorMap = new int[4][3];
    colorMap[0] = new int[] {255, 0, 0};
    colorMap[1] = new int[] {0, 255, 0};
    colorMap[2] = new int[] {0, 0, 255};
    colorMap[3] = new int[] {255, 0, 255};
    
    int i = 0;
    for (String annotation : termsToFilterOn) {
      Set<Point> coloredSet = new HashSet<Point>();
      for (Point point : annotatedPoints) {
        // Make sure that the point only has one of the annotations
        if (point.getAnnotations().contains(annotation)) {
          boolean foundOnce = true;
          for (String otherTerm : termsToFilterOn) {
            if (otherTerm != annotation) {
              if (point.getAnnotations().contains(otherTerm)) {
                foundOnce = false;
              }
            }
          }
          if (foundOnce) {
            coloredSet.add(point);
            annotatedPointsCopy.remove(point);
          }
        }
      }
      scatterPlot.addPointsToScatterPlot(coloredSet, colorMap[i][0], colorMap[i][1], colorMap[i][2]);
      i++;
    }
    
    // Color remaining points
    System.out.println(annotatedPointsCopy.size());
    scatterPlot.addPointsToScatterPlot(annotatedPointsCopy, 144, 144, 144);
  }
  
  @Override
  public void draw() {
    // System.out.println(frameRate);
    fill(0);
    background(0);
    
    image(plotImg, PLOT_BORDER, PLOT_BORDER);
    
    drawSelectingRect();
    drawAxis();
    stroke(255);
  }
  
  private void drawAxis() {
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
    Set<Point> set = scatterPlot.extractEverythingInRectangularRegion(
      convertMouseXPositionToBufferPosition(firstDragCornerX),
      convertMouseYPositionToBufferPosition(firstDragCornerY), convertMouseXPositionToBufferPosition(mouseX),
      convertMouseYPositionToBufferPosition(mouseY), EmptyScatterPlot.Type.SCALED);
    
    // DEBUGGING PRINTOUT
    // System.out.println("First X " + convertMouseXPositionToAbsolutePosition(firstDragCornerX));
    // System.out.println("First Y " + convertMouseYPositionToAbsolutePosition(firstDragCornerY));
    // System.out.println("Second X " + convertMouseXPositionToAbsolutePosition(mouseX));
    // System.out.println("Second Y " + convertMouseYPositionToAbsolutePosition(mouseY));
    List<Map.Entry<String,Integer>> annotations = printAnnotations(set);
    
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
  
  private List<Map.Entry<String,Integer>> printAnnotations(Set<Point> set) {
    
    // STEP 0 GET ANNOTATIONS
    List<Map.Entry<String,Integer>> sortedList = getAnnotations(set);
    
    // STEP 1 PRINT OUT TO STDOUT
    System.out.print("Size:" + set.size() + " \t");
    for (Map.Entry<String,Integer> term : sortedList) {
      System.out.print(term.getKey() + ":" + (float) term.getValue() / set.size() + ";\t");
    }
    System.out.println();
    return sortedList;
  }
  
  private List<Map.Entry<String,Integer>> getAnnotations(Set<Point> set) {
    // STEP 0 SUM UP TERMS
    Map<String,Integer> sumCounts = new HashMap<String,Integer>();
    for (Point point : set) {
      for (String term : point.getAnnotations()) {
        if (sumCounts.get(term) != null) {
          sumCounts.put(term, (1 + sumCounts.get(term)));
        } else {
          sumCounts.put(term, 1);
        }
      }
    }
    
    // STEP 1 REMOBE BAD TERMS
    removeBadTerms(sumCounts);
    
    // STEP 2 SORT FROM GREATEST TO LEAST
    List<Map.Entry<String,Integer>> sortedList = new ArrayList<Entry<String,Integer>>();
    sortedList.addAll(sumCounts.entrySet());
    Collections.sort(sortedList, new BySize());
    return sortedList;
  }
  
  private void removeBadTerms(Map<String,Integer> map) {
    map.remove("A");
    map.remove("data");
    map.remove("Experiment");
    map.remove("RNA");
    map.remove("microarray");
    map.remove("Homo sapiens");
    map.remove("gene");
    map.remove("normal");
    map.remove("year");
    map.remove("age");
    map.remove("protocol");
    map.remove("organism_part");
    map.remove("Human");
    map.remove("whole organism");
    map.remove("month");
    map.remove("adult");
    map.remove("female");
    map.remove("male");
    map.remove("hour");
    map.remove("role");
    map.remove("array");
    map.remove("array design");
    map.remove("submitted");
    map.remove("organism");
    map.remove("software");
    map.remove("submitter");
    map.remove("labeling");
    map.remove("transcription profiling by array");
    map.remove("control");
    map.remove("quality");
    map.remove("experimental factor");
    map.remove("transcription profiling");
    map.remove("publication status");
    map.remove("hardware");
    map.remove("feature_extraction");
    map.remove("protocol parameter");
    map.remove("replicate");
    map.remove("data transformation");
    map.remove("disease");
    map.remove("time");
    map.remove("treatment");
    map.remove("sex");
    map.remove("unit");
    map.remove("cell_type");
    map.remove("site");
    
    Iterator<String> itr = map.keySet().iterator();
    String key;
    while (itr.hasNext()) {
      key = itr.next();
      if (key.length() < 3) { // map.get(key) == 1 ||
        itr.remove();
      }
    }
    
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
      printAnnotations(scatterPlot.extractEverythingInRectangularRegion(
        convertMouseXPositionToBufferPosition(0), convertMouseXPositionToBufferPosition(annotationsRectY),
        convertMouseXPositionToBufferPosition(width),
        convertMouseXPositionToBufferPosition(annotationsRectY + ANNOTATIONSRECTSIZE), Type.SCALED));
      rect(0, annotationsRectY, width, ANNOTATIONSRECTSIZE);
    } else if (key == 'j' || key == 'l') {
      printAnnotations(scatterPlot.extractEverythingInRectangularRegion(
        convertMouseXPositionToBufferPosition(annotationsRectX), convertMouseXPositionToBufferPosition(0),
        convertMouseXPositionToBufferPosition(annotationsRectX + ANNOTATIONSRECTSIZE),
        convertMouseXPositionToBufferPosition(height), Type.SCALED));
      rect(annotationsRectX, 0, ANNOTATIONSRECTSIZE, height);
    }
  }
  
  private void processMovementRelatedKeys() {
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
    if (key == 'q') {
      bufferXCoord = 0;
      bufferYCoord = 0;
      zoom = 1;
      drawPlotIntoBuffer();
    }
    
  }
  
  private int convertMouseXPositionToBufferPosition(int position) {
    return position - PLOT_BORDER + bufferXCoord;
  }
  
  private int convertMouseYPositionToBufferPosition(int position) {
    return position - PLOT_BORDER + bufferYCoord;
  }
  
  private double convertMouseXPositionToAbsolutePosition(int xPosition) {
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
  
  private double convertMouseYPositionToAbsolutePosition(int yPosition) {
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
    // STEP 0 SETUP
    scatterPlot.setScaledDimensions(bufferWidth(), bufferHeight());
    plotBuffer = createGraphics(bufferWidth(), bufferHeight(), P2D);
    plotBuffer.beginDraw();
    // STEP 1 DRAW POINTS
    plotBuffer.rectMode(CENTER);
    for (ColoredHashSetOfPoints set : scatterPlot.getSetsToDraw()) {
      // plotBuffer.strokeWeight(10);// TODO Delete
      plotBuffer.stroke(color(set.getColor()[0], set.getColor()[1], set.getColor()[2]));
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
  
  private class BySize implements Comparator<Map.Entry<String,Integer>> {
    
    public int compare(Entry<String,Integer> o1, Entry<String,Integer> o2) {
      if (o1.getValue() < o2.getValue()) {
        return 1;
      } else if (o1.getValue() == o2.getValue()) {
        return 0;
      } else {
        return -1;
      }
    }
    
  }
}
