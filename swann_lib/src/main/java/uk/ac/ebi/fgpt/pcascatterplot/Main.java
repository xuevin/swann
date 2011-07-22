package uk.ac.ebi.fgpt.pcascatterplot;

import processing.core.PApplet;

public class Main {
  public static void main(String args[]) {
    ArgParser argParser = new ArgParser();
    if(argParser.parse(args)){
      PApplet.main(new String[] {"uk.ac.ebi.fgpt.pcascatterplot.view.ProcessingPApplet",
                                 argParser.getAnnotationsFile(), argParser.getCoordinateFile(),
                                 argParser.getPca1(), argParser.getPca2()});
    }
  }
  
}
