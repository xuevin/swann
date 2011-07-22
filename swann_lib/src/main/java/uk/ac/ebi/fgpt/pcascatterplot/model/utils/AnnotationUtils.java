package uk.ac.ebi.fgpt.pcascatterplot.model.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import uk.ac.ebi.fgpt.pcascatterplot.model.Point;

public class AnnotationUtils {
  public static List<Map.Entry<String,Integer>> printAnnotations(Set<Point> set) {
    
    // STEP 0 GET ANNOTATIONS
    List<Map.Entry<String,Integer>> sortedList = getAnnotations(set);
    
    // STEP 1 PRINT OUT TO STDOUT
    System.out.print("Size:" + set.size() + " \t");
    for (Map.Entry<String,Integer> term : sortedList) {
      System.out.print(term.getKey() + ":" + (float) term.getValue() / set.size() + ";\t");
    }
    System.out.println();
    
    // STEP 2 RETURN IF NEEDED
    return sortedList;
  }
  
  public static List<Map.Entry<String,Integer>> getAnnotations(Set<Point> set) {
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
  
  public static void removeBadTerms(Map<String,Integer> map) {
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
  
  private static class BySize implements Comparator<Map.Entry<String,Integer>> {
    
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
