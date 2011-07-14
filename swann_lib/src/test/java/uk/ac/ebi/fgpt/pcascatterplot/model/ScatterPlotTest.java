package uk.ac.ebi.fgpt.pcascatterplot.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ScatterPlotTest {
  
  @Before
  public void setUp() throws Exception {}
  
  @Test
  public void testMap() {
    assertEquals(50, ScatterPlot.map(0, -100, 100, 0, 100),0);
  }
  
}
