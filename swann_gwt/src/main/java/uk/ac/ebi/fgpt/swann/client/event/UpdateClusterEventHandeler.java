package uk.ac.ebi.fgpt.swann.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UpdateClusterEventHandeler extends EventHandler{
  public void onEvent(UpdateClusterEvent event);

}
