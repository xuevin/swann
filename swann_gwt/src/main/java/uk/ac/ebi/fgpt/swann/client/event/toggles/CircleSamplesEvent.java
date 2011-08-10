package uk.ac.ebi.fgpt.swann.client.event.toggles;

import com.google.gwt.event.shared.GwtEvent;

public class CircleSamplesEvent extends GwtEvent<CircleSamplesEventHandeler> {
  
  public static Type<CircleSamplesEventHandeler> TYPE = new Type<CircleSamplesEventHandeler>();
  private boolean shouldCircleSamples;
  
  public CircleSamplesEvent(boolean boxExperiments) {
    this.shouldCircleSamples = boxExperiments;
  }
  
  @Override
  protected void dispatch(CircleSamplesEventHandeler handler) {
    handler.onEvent(this);
  }
  
  @Override
  public Type<CircleSamplesEventHandeler> getAssociatedType() {
    return TYPE;
  }
  
  public boolean shouldCircleSamples() {
    return shouldCircleSamples;
  }
  
}
