package uk.ac.ebi.fgpt.pcascatterplot.model;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MatcherTest {
  Matcher test;
  
  @Before
  public void setUp() throws Exception {
    File annotationsFile = new File(getClass().getClassLoader().getResource("annotations_summary.txt")
        .toURI());
    File coordFile = new File(getClass().getClassLoader().getResource(
      "assembly2_30915x3.txt_with_names.copy.txt").toURI());
    test = new Matcher(coordFile, annotationsFile);
    
  }
  
  @Test
  public void testThatMatcherIsNotNull() throws URISyntaxException, IOException {
    assertNotNull(test);
  }
  
  @Test
  public void takeAPeekAtPoints() {
    Collection<Point> points = test.getAnnotatedPoints();
    int i = 0;
    for(Point point:points){
      System.out.print(point.getUnscaledXPosition() + "\t" + point.getUnscaledYPosition() + "\t");
      Collection<String> termCountPair= point.getAnnotations();
      for(String string:termCountPair){
        System.out.print(string+"\t");
      }
      System.out.println();
      i++;
      if(i>10){
        break;
      }
    }
  }
}
