package uk.ac.ebi.fgpt.swann.client.ginview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import uk.ac.ebi.fgpt.swann.client.presenter.TogglesPresenter;

/**
 * This is a work in progress. This is used to create a TRUE mvp pattern.
 * @author Vincent Xue
 *
 */
public class TogglesView extends Composite implements TogglesPresenter.ITogglesView {
  interface MyUiBinder extends UiBinder<Widget,TogglesView> {};
  
  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
  
  @UiField
  CheckBox boxAllExperimentsToggle;
  @UiField
  CheckBox circleSelectedPointsToggle;
  
  public TogglesView() {
    initWidget(uiBinder.createAndBindUi(this));
  }
  
  @Override
  public void set_boxAllExperiments(boolean value) {
    boxAllExperimentsToggle.setValue(value);
    
  }
  
  @Override
  public void set_circleSelectedPoints(boolean value) {
    circleSelectedPointsToggle.setValue(value);
  }
  
  @Override
  public boolean shouldBoxAllExperiments() {
    return boxAllExperimentsToggle.getValue();
  }
  
  @Override
  public boolean shouldCircleSelectedPoints() {
    return boxAllExperimentsToggle.getValue();
  }
  
  @Override
  public HasClickHandlers get_boxAllExperimentsToggle() {
    return boxAllExperimentsToggle;
  }
  
  @Override
  public HasClickHandlers get_circleSelectedPointsToggle() {
    return circleSelectedPointsToggle;
  }
}
