package uk.ac.ebi.fgpt.swann.model.utils;

public class MathUtils {
  public static int map(double value, double low1, double high1, float low2, float high2) {
    double range1 = high1 - low1;
    float range2 = high2 - low2;
    double dif = value - low1;
    double percent = dif / range1;
    float howFarFromLow2 = (float) (percent * range2);
    if(range1 ==0){
      System.err.println("DIVIDE BY ZERO");
    }
    return (int)(low2 + howFarFromLow2);
  }
}
