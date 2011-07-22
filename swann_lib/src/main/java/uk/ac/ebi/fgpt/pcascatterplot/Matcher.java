package uk.ac.ebi.fgpt.pcascatterplot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.fgpt.pcascatterplot.model.Point;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Matcher {
  private Map<String,Point> uniqueCelNameToPointMap = new HashMap<String,Point>();
  private Map<String,Collection<String>> uniqueExperimentToAnnotationsMapMap = new HashMap<String,Collection<String>>();
  
  public Matcher(File coordFiles, File annotations, int pca1, int pca2) throws IOException {
    // First parse the two files into a list of strings
    List<String> listOfValues = Files.readLines(coordFiles, Charsets.UTF_8);
    List<String> listOfAnnotations = Files.readLines(annotations, Charsets.UTF_8);
    
    // Delete the header lines
    listOfValues.remove(0);
    listOfAnnotations.remove(0);
    
    for (String string : listOfValues) {
      String[] columns = string.split("\t");
      
      uniqueCelNameToPointMap.put(columns[0].replace("\"", ""), new Point(Double.parseDouble(columns[pca1]),
          Double.parseDouble(columns[pca2])));
    }
    // Store listOfAnnotations as a experiment to annotations map
    for (String string : listOfAnnotations) {
      String[] columns = string.split("\t");
      String experiment = columns[0];
      Collection<String> collectionOfAnnotations = convertAnnotationsToCollectoin(columns[4]);
      collectionOfAnnotations.add(experiment);
      uniqueExperimentToAnnotationsMapMap.put(experiment, collectionOfAnnotations);
    }
    
    // Now reannotate the points in uniqueCelNameToPointMap
    for (String uniqueCELName : uniqueCelNameToPointMap.keySet()) {
      String experiment = uniqueCELName.split("_")[0];
      uniqueCelNameToPointMap.get(uniqueCELName).setAnnotations(
        uniqueExperimentToAnnotationsMapMap.get(experiment));
      uniqueCelNameToPointMap.get(uniqueCELName).setExperiment(experiment);
    }
  }
  
  public Collection<Point> getAnnotatedPoints() {
    return uniqueCelNameToPointMap.values();
  }
  
  public ArrayList<Point> getSerializedAnnotatedPoints() {
    ArrayList<Point> points = new ArrayList<Point>();
    points.addAll(uniqueCelNameToPointMap.values());
    return points;
  }
  
  private Collection<String> convertAnnotationsToCollectoin(String string) {
    String[] annotationsAsArray = string.split(";");
    Collection<String> map = new HashSet<String>();
    for (String termCountPair : annotationsAsArray) {
      String[] array = termCountPair.split(":");
      if (!array[0].equals("")) {
        map.add(array[0]);
      }
    }
    return map;
  }
  
}
