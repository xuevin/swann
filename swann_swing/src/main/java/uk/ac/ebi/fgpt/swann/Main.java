package uk.ac.ebi.fgpt.swann;

import javax.swing.JFrame;

import uk.ac.ebi.fgpt.swann.view.ProcessingPApplet;
import uk.ac.ebi.fgpt.swann.view.Swann_Frame;

public class Main {
  public static void main(final String args[]) {
    JFrame frame = new JFrame("Swann");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    ArgParser argParser = new ArgParser();
    argParser.parse(args);
    ProcessingPApplet applet = new ProcessingPApplet();
    applet.args = new String[] {argParser.getAnnotationsFile(), argParser.getCoordinateFile(),
                                argParser.getPca1(), argParser.getPca2()};
    applet.init();
    
    // Create and set up the content pane.
    Swann_Frame swann_Frame = new Swann_Frame(applet);
    swann_Frame.setOpaque(true); // content panes must be opaque
    frame.setContentPane(swann_Frame);
    
    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }
}
