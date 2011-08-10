package uk.ac.ebi.fgpt.swann.client.event.toggles;

import com.google.gwt.event.shared.EventHandler;

public interface UseExclusiveOrEventHandeler extends EventHandler{
  void onEvent(UseExclusiveOrEvent event);
}
