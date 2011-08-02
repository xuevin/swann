package uk.ac.ebi.fgpt.swann.model.utils;

import uk.ac.ebi.fgpt.swann.model.Vector;

public class ArrayUtils {
  /**
   * Converts an array of strings, into an array of vectors.
   * 
   * @param arrayOfStrings
   *          an array of strings as represented in the document.
   * @return a 2D array of vectors. Each row represents a vector.
   */
  public static Vector[] convertArrayOfStringsIntoArrayOfVectors(String[] arrayOfStrings) {
    Vector[] vectorArray = new Vector[arrayOfStrings[0].split("\t").length];
    
    // instantiate each vector
    for (int i = 0; i < vectorArray.length; i++) {
      vectorArray[i] = new Vector(arrayOfStrings.length);
    }
    
    // fill each vector
    int positionIndex = 0;
    for (String string : arrayOfStrings) {
      String[] row = string.split("\t");
      for (int vectorIndex = 0; vectorIndex < row.length; vectorIndex++) {
        vectorArray[vectorIndex].setQuick(positionIndex, Double.parseDouble(row[vectorIndex]));
      }
      positionIndex++;
    }
    return vectorArray;
  }

}
