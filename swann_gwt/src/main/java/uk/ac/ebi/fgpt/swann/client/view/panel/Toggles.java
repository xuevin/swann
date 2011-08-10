package uk.ac.ebi.fgpt.swann.client.view.panel;

import uk.ac.ebi.fgpt.swann.client.event.toggles.BoxExperimentsEvent;
import uk.ac.ebi.fgpt.swann.client.event.toggles.CircleSamplesEvent;
import uk.ac.ebi.fgpt.swann.client.event.toggles.UseExclusiveOrEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Toggles extends VerticalPanel {
  public Toggles(final SimpleEventBus bus) {
    super();
    
    final CheckBox boxSimilarExperimentsToggle = new CheckBox("Box points in same exper.");
    boxSimilarExperimentsToggle.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        bus.fireEvent(new BoxExperimentsEvent(boxSimilarExperimentsToggle.getValue()));
      }
    });
    
    final CheckBox circleSamplesHighlighted = new CheckBox("Circle points highlighted");
    circleSamplesHighlighted.setValue(true);
    circleSamplesHighlighted.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        bus.fireEvent(new CircleSamplesEvent(circleSamplesHighlighted.getValue()));
      }
    });
    
    final CheckBox useExclusiveOrToggle = new CheckBox("Use exclusive or");
    useExclusiveOrToggle.setValue(true);
    useExclusiveOrToggle.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        bus.fireEvent(new UseExclusiveOrEvent(useExclusiveOrToggle.getValue()));
      }
    });
    
    final CheckBox userLargerPoints = new CheckBox("Use larger colored points");
    userLargerPoints.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
      // bus.fireEvent(new UseExclusiveOrEvent(useExclusiveOrToggle.getValue()));
      // TODO;
      }
    });
    
    Label clusterToggleLabel = new Label("Clustering Toggles");
    clusterToggleLabel.addStyleName("toggleLabels");
    Label selectionToggleLabel = new Label("Selection Toggles");
    selectionToggleLabel.addStyleName("toggleLabels");
    
    add(selectionToggleLabel);
    add(boxSimilarExperimentsToggle);
    add(circleSamplesHighlighted);
    add(clusterToggleLabel);
    add(useExclusiveOrToggle);
    add(userLargerPoints);
    
  }
}
