package uk.ac.ebi.fgpt.swann.client.view;

import uk.ac.ebi.fgpt.swann.client.event.RemovedClusterEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Button;

public class LabelWithRemovalButton extends Button {
  
  public LabelWithRemovalButton(final SimpleEventBus bus, final String string) {
    super(string);
    addClickHandler(new ClickHandler() {
      
      public void onClick(ClickEvent event) {
        bus.fireEvent(new RemovedClusterEvent(string));
        removeSelf();
      }
    });
  }
  
  private void removeSelf() {
    this.removeFromParent();
  }
  
}
