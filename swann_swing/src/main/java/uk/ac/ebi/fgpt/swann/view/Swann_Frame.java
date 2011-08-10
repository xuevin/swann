package uk.ac.ebi.fgpt.swann.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import processing.core.PApplet;

public class Swann_Frame extends JPanel{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public Swann_Frame(ProcessingPApplet applet){
    setLayout(new BorderLayout());

    
    add(applet,BorderLayout.CENTER);
  }
 

}
