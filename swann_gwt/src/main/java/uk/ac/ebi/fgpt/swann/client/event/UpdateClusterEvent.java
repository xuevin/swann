package uk.ac.ebi.fgpt.swann.client.event;

import java.util.Map;

import uk.ac.ebi.fgpt.swann.model.Color;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateClusterEvent extends GwtEvent<UpdateClusterEventHandeler> {
  private Map<String,Color> termToColor;
  
  public UpdateClusterEvent(Map<String,Color> termToColor) {
    this.termToColor = termToColor;
  }
  
  public static Type<UpdateClusterEventHandeler> TYPE = new Type<UpdateClusterEventHandeler>();
  
  @Override
  protected void dispatch(UpdateClusterEventHandeler handler) {
    handler.onEvent(this);
  }
  
  @Override
  public Type<UpdateClusterEventHandeler> getAssociatedType() {
    return TYPE;
  }
  
  public Map<String,Color> getTermToColor() {
    return termToColor;
    
  }
  
}
