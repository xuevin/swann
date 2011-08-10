package uk.ac.ebi.fgpt.swann.client.presenter;

import uk.ac.ebi.fgpt.swann.client.event.toggles.BoxExperimentsEvent;
import uk.ac.ebi.fgpt.swann.client.event.toggles.CircleSamplesEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;

public class TogglesPresenter {
  public interface ITogglesView {
    void set_boxAllExperiments(boolean value);
    
    void set_circleSelectedPoints(boolean value);
    
    boolean shouldBoxAllExperiments();
    
    boolean shouldCircleSelectedPoints();
    
    HasClickHandlers get_circleSelectedPointsToggle();
    
    HasClickHandlers get_boxAllExperimentsToggle();
  }
  
  private ITogglesView view;
  private SimpleEventBus bus;
  
  public TogglesPresenter(SimpleEventBus bus, ITogglesView view) {
    this.bus = bus;
    this.view = view;
  }
  
  public void bind() {
    view.get_boxAllExperimentsToggle().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        bus.fireEvent(new BoxExperimentsEvent(view.shouldBoxAllExperiments()));
      }
    });
    view.get_circleSelectedPointsToggle().addClickHandler(new ClickHandler() {
      
      @Override
      public void onClick(ClickEvent event) {
        bus.fireEvent(new CircleSamplesEvent(view.shouldCircleSelectedPoints()));
      }
    });
  }
}
