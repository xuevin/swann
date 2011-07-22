package uk.ac.ebi.fgpt.pcascatterplot;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class ArgParser {
  private Options cliOptions;
  private String annotationsFile;
  private String coordinateFile;
  private String pca1;
  private String pca2;
  
  public ArgParser() {
    
    cliOptions = new Options();
    Option annotations = OptionBuilder.withArgName("annotations.txt").hasArg().isRequired().withDescription(
      "use the given text file as the reference with annotations").withLongOpt("annotations").create("a");
    
    Option coordinates = OptionBuilder.withArgName("coordinates.txt").hasArg().isRequired().withLongOpt(
      "coordinates").withDescription("use the given text file as the file with the coordinates").create("c");
    
    Option pca1 = OptionBuilder.withArgName("num").hasArg().isRequired().withLongOpt("pca1").withDescription(
      "Principal component X axis").create("pca1");
    Option pca2 = OptionBuilder.withArgName("num").hasArg().isRequired().withLongOpt("pca2").withDescription(
      "Principal component Y axis").create("pca2");
    
    // Add Options
    cliOptions.addOption(annotations);
    cliOptions.addOption(coordinates);
    cliOptions.addOption(pca1);
    cliOptions.addOption(pca2);
    
  }
  
  public boolean parse(String[] args) {
    HelpFormatter formatter = new HelpFormatter();
    
    // Try to parse options
    CommandLineParser parser = new PosixParser();
    
    if (args.length <= 0) {
      formatter.printHelp("pcaScatterPlot", cliOptions, true);
      System.exit(1);
    }
    try {
      CommandLine cmd = parser.parse(cliOptions, args, true);
      annotationsFile = cmd.getOptionValue("a");
      coordinateFile = cmd.getOptionValue("c");
      pca1 = cmd.getOptionValue("pca1");
      pca2 = cmd.getOptionValue("pca2");
      return true;
    } catch (ParseException e) {
      e.printStackTrace();
      formatter.printHelp("pcaJob", cliOptions, true);
      return false;
    }
  }
  
  public String getAnnotationsFile() {
    return annotationsFile;
  }
  
  public String getCoordinateFile() {
    return coordinateFile;
  }
  
  public String getPca1() {
    return pca1;
  }
  
  public String getPca2() {
    return pca2;
  }
}
