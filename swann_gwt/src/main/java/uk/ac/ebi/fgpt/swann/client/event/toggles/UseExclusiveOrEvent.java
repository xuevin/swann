package uk.ac.ebi.fgpt.swann.client.event.toggles;

import com.google.gwt.event.shared.GwtEvent;

public class UseExclusiveOrEvent extends GwtEvent<UseExclusiveOrEventHandeler> {
  
  public static Type<UseExclusiveOrEventHandeler> TYPE = new Type<UseExclusiveOrEventHandeler>();
  private boolean useExclusiveOr;
  
  public UseExclusiveOrEvent(boolean boxExperiments) {
    this.useExclusiveOr = boxExperiments;
  }
  
  @Override
  protected void dispatch(UseExclusiveOrEventHandeler handler) {
    handler.onEvent(this);
  }
  
  @Override
  public Type<UseExclusiveOrEventHandeler> getAssociatedType() {
    return TYPE;
  }
  
  public boolean shouldUseExclusiveOr() {
    return useExclusiveOr;
  }
  
}
