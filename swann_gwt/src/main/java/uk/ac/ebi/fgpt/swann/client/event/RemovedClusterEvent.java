package uk.ac.ebi.fgpt.swann.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class RemovedClusterEvent extends GwtEvent<RemovedClusterEventHandeler> {
  public static Type<RemovedClusterEventHandeler> TYPE = new Type<RemovedClusterEventHandeler>();
  
  private String labelRemoved;
  
  public RemovedClusterEvent(String label) {
    labelRemoved = label;
  }
  
  @Override
  protected void dispatch(RemovedClusterEventHandeler handler) {
    handler.onEvent(this);
    
  }
  
  @Override
  public Type<RemovedClusterEventHandeler> getAssociatedType() {
    return TYPE;
  }
  
  public String getLabelRemoved() {
    return labelRemoved;
  }
  
}
