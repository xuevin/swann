package uk.ac.ebi.fgpt.pcascatterplot.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Matcher {
  private Map<String,Point> uniqueCelNameToPointMap = new HashMap<String,Point>();
  private Map<String,Map<String,Integer>> uniqueExperimentToAnnotationsMapMap = new HashMap<String,Map<String,Integer>>();
  
  public Matcher(File coordFiles, File annotations) throws IOException {
    // First parse the two files into a list of strings
    List<String> listOfValues = Files.readLines(coordFiles, Charsets.UTF_8);
    List<String> listOfAnnotations = Files.readLines(annotations, Charsets.UTF_8);
    
    // Delete the header lines
    listOfValues.remove(0);
    listOfAnnotations.remove(0);
    
    for (String string : listOfValues) {
      String[] columns = string.split("\t");
      
      // FOR TESTING PURPOSES, I AM HARDCODING THE COLUMNS I WANT TO PLOT
      // TODO Make this a function
      uniqueCelNameToPointMap.put(columns[0].replace("\"", ""), new Point(Double.parseDouble(columns[1]),
          Double.parseDouble(columns[2])));
    }
    // Store listOfAnnotations as a experiment to annotations map
    for (String string : listOfAnnotations) {
      String[] columns = string.split("\t");
      String experiment = columns[0];
      uniqueExperimentToAnnotationsMapMap.put(experiment, convertAnnotationsColumnToMap(columns[4]));
    }
    
    // Now reannotate the points in uniqueCelNameToPointMap
    for (String uniqueCELName : uniqueCelNameToPointMap.keySet()) {
      String experiment = uniqueCELName.split("_")[0];
      uniqueCelNameToPointMap.get(uniqueCELName).setAnnotations(
        uniqueExperimentToAnnotationsMapMap.get(experiment));
    }
  }
  
  public Collection getAnnotatedPoints() {
    return uniqueCelNameToPointMap.values();
  }
  
  private Map<String,Integer> convertAnnotationsColumnToMap(String string) {
    String[] annotationsAsArray = string.split(";");
    Map<String,Integer> map = new HashMap<String,Integer>();
    for (String termCountPair : annotationsAsArray) {
      String[] array = termCountPair.split(":");
      if (!array[0].equals("")) {
        map.put(array[0], Integer.parseInt(array[1]));
      }
    }
    return map;
  }
  
}
