package uk.ac.ebi.fgpt.swann.client.view.panel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.fgpt.swann.client.event.RemovedClusterEvent;
import uk.ac.ebi.fgpt.swann.client.event.RemovedClusterEventHandeler;
import uk.ac.ebi.fgpt.swann.client.event.UpdateClusterEvent;
import uk.ac.ebi.fgpt.swann.client.service.searchservice.SearchOracle;
import uk.ac.ebi.fgpt.swann.client.view.LabelWithRemovalButton;
import uk.ac.ebi.fgpt.swann.model.Color;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CustomClustering extends VerticalPanel {
  
  private Color[] colorMaps = new Color[] {new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255),
                                           new Color(255, 255, 0), new Color(0, 255, 255),
                                           new Color(255, 0, 255)};
  
  private final Map<String,Color> mapToColor = new HashMap<String,Color>();
  private SimpleEventBus bus;
  
  private final SuggestOracle searchOracle = new SearchOracle();
  private final SuggestBox term = new SuggestBox(searchOracle);
  private final Button addButton = new Button("Add");
  
  public CustomClustering(final SimpleEventBus bus) {
    super();
    this.bus = bus;
    initHandelers();
    
    term.setWidth("140px");
    term.setFocus(true);
    term.setLimit(10);
    
    FlowPanel panelContents = new FlowPanel();
    panelContents.setWidth("200px");
    panelContents.setHeight("60px");
    panelContents.add(new Label("Term"));
    panelContents.add(term);
    panelContents.add(addButton);
    
    add(panelContents);
    
  }
  
  private void initHandelers() {
    bus.addHandler(RemovedClusterEvent.TYPE, new RemovedClusterEventHandeler() {
      
      public void onEvent(RemovedClusterEvent event) {
        mapToColor.remove(event.getLabelRemoved());
        bus.fireEvent(new UpdateClusterEvent(mapToColor));
      }
    });
    addButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        addNewClusterToPlot();
      }
    });
    term.getTextBox().addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
        if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode()) {
          addNewClusterToPlot();
        }
      }
    });
  }
  
  private void addNewClusterToPlot() {
    Color color = getNewColor();
    mapToColor.put(term.getValue(), color); // TODO this can be customized
    bus.fireEvent(new UpdateClusterEvent(mapToColor));
    LabelWithRemovalButton button = new LabelWithRemovalButton(bus, term.getValue());
    button.getElement().setClassName("colored_button");
    button.getElement().setAttribute("style", ("background-color:" + CssColor.make(color.getCSSString())));
    
    add(button);
  }
  
  private Color getNewColor() {
    Collection<Color> mapValues = mapToColor.values();
    for (Color color : colorMaps) {
      if (mapValues.contains(color)) {
        // SKIP
      } else {
        return color;
      }
    }
    return new Color(0, 0, 0);
  }
}
