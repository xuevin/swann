package uk.ac.ebi.fgpt.swann.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import uk.ac.ebi.fgpt.swann.model.Point;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class DataProvider {
  private Map<String,Point> uniqueCelNameToPointMap = new HashMap<String,Point>();
  private Map<String,Collection<String>> uniqueCELLFileToAnnotationsMap = new HashMap<String,Collection<String>>();
  private Collection<String> collectionOfAllAnnotatedTerms = new TreeSet<String>(); // Orderd and unique
  
  public DataProvider(File coordFiles, File annotations, int pca1, int pca2) throws IOException {
    // First parse the two files into a list of strings
    List<String> listOfValues = Files.readLines(coordFiles, Charsets.UTF_8);
    List<String> listOfAnnotations = Files.readLines(annotations, Charsets.UTF_8);
    
    // Delete the header lines
    listOfValues.remove(0);
    listOfAnnotations.remove(0);
    
    // Parse the coordinate points
    for (String string : listOfValues) {
      String[] columns = string.split("\t");
      uniqueCelNameToPointMap.put(columns[0].replace("\"", "").toUpperCase(), new Point(Double
          .parseDouble(columns[pca1]), Double.parseDouble(columns[pca2])));
    }
    // Store listOfAnnotations as a experiment to annotations map
    for (String string : listOfAnnotations) {
      String[] columns = string.split("\t");
      String experiment = columns[0];
      String celFile = columns[1];
      Collection<String> collectionOfAnnotations = convertAnnotationsToCollection(columns[4]);
      collectionOfAnnotations.add(experiment); // Add accession into list of annotations
      uniqueCELLFileToAnnotationsMap.put((experiment + "_" + celFile).toUpperCase(), collectionOfAnnotations);
      // Add all the annotations to the master list.
      collectionOfAllAnnotatedTerms.addAll(collectionOfAnnotations);
    }
    annotatePoints();
  }
  
  public void annotatePoints() {
    for (String uniqueCELName : uniqueCelNameToPointMap.keySet()) {
      String experiment = uniqueCELName.split("_")[0];
      if (uniqueCELLFileToAnnotationsMap.get(uniqueCELName) != null) {
        uniqueCelNameToPointMap.get(uniqueCELName).setAnnotations(
          uniqueCELLFileToAnnotationsMap.get(uniqueCELName));
      } else {
        // System.out.println(uniqueCELName); // These are the points that did not get annotated
        uniqueCelNameToPointMap.get(uniqueCELName).setAnnotations(new HashSet<String>());
      }
      uniqueCelNameToPointMap.get(uniqueCELName).setExperiment(experiment);
      uniqueCelNameToPointMap.get(uniqueCELName).setSampleName(uniqueCELName);
    }
  }
  
  public Collection<Point> getAnnotatedPoints() {
    return uniqueCelNameToPointMap.values();
  }
  
  public ArrayList<Point> getAnnotatedPointsAsArrayList() {
    ArrayList<Point> points = new ArrayList<Point>();
    points.addAll(uniqueCelNameToPointMap.values());
    return points;
  }
  
  private Collection<String> convertAnnotationsToCollection(String string) {
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
  
  public Collection<String> getSortedCollectionOfAllTerms() {
    return collectionOfAllAnnotatedTerms;
  }
  
}
