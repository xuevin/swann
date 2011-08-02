package uk.ac.ebi.fgpt.swann.model.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.fgpt.swann.model.utils.MathUtils;

public class MathUtilsTest {
  
  @Before
  public void setUp() throws Exception {}
  
  @Test
  public void test() {
    assertEquals(50, MathUtils.map(0, -100, 100, 0, 100), 0);
  }
  
}
