package uk.ac.ebi.fgpt.swann.client.event.toggles;

import com.google.gwt.event.shared.GwtEvent;

public class BoxExperimentsEvent extends GwtEvent<BoxExperimentsEventHandeler> {
  
  public static Type<BoxExperimentsEventHandeler> TYPE = new Type<BoxExperimentsEventHandeler>();
  private boolean boxSimilarExperiments;
  
  public BoxExperimentsEvent(boolean boxExperiments) {
    this.boxSimilarExperiments = boxExperiments;
  }
  
  @Override
  protected void dispatch(BoxExperimentsEventHandeler handler) {
    handler.onEvent(this);
  }
  
  @Override
  public Type<BoxExperimentsEventHandeler> getAssociatedType() {
    return TYPE;
  }
  
  public boolean shouldBoxSimilarExperiments() {
    return boxSimilarExperiments;
  }
  
}
