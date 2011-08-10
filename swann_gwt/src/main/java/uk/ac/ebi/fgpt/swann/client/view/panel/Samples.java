package uk.ac.ebi.fgpt.swann.client.view.panel;

import uk.ac.ebi.fgpt.swann.client.event.UpdateInfoEvent;
import uk.ac.ebi.fgpt.swann.client.event.UpdateInfoEventHandeler;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Samples extends VerticalPanel {
  
  private Label numberOfPoints;
  private static final String NUMBEROFPOINTSLABEL  = "Number of Points: ";
  
  public Samples(final SimpleEventBus bus){
    super();
    numberOfPoints = new Label(NUMBEROFPOINTSLABEL);
    add(numberOfPoints);
    
    bus.addHandler(UpdateInfoEvent.TYPE,new UpdateInfoEventHandeler() {
      
      public void onEvent(UpdateInfoEvent event) {
        numberOfPoints.setText(NUMBEROFPOINTSLABEL + event.getNumberOfSamplesHighlighted());
      }
    });
  }
}
