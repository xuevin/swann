package uk.ac.ebi.fgpt.swann.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface RemovedClusterEventHandeler extends EventHandler{
  public void onEvent(RemovedClusterEvent event);

}
