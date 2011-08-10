package uk.ac.ebi.fgpt.swann.client.view.panel;

import java.util.Collection;

import uk.ac.ebi.fgpt.swann.client.event.UpdateInfoEvent;
import uk.ac.ebi.fgpt.swann.client.event.UpdateInfoEventHandeler;
import uk.ac.ebi.fgpt.swann.client.view.ExperimentCell;
import uk.ac.ebi.fgpt.swann.client.view.RangeLabelPager;
import uk.ac.ebi.fgpt.swann.client.view.ShowMorePagerPanel;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

public class ExperimentsList extends VerticalPanel {
  
  private CellList<String> cellList;
  private ListDataProvider<String> dataProvider;
  private ShowMorePagerPanel showMorePagerPanel;
  private RangeLabelPager rangeLabelPager;
  
  public ExperimentsList(final SimpleEventBus bus) {
    super();
    
    bus.addHandler(UpdateInfoEvent.TYPE, new UpdateInfoEventHandeler() {
      public void onEvent(UpdateInfoEvent event) {
        setData(event.getExperiments());
      }
    });
    
    ExperimentCell textCell = new ExperimentCell();
    
    showMorePagerPanel = new ShowMorePagerPanel();
    showMorePagerPanel.setStyleName("experimentListStyle");
    dataProvider = new ListDataProvider<String>();
    rangeLabelPager = new RangeLabelPager();
    cellList = new CellList<String>(textCell, dataProvider);
    
    cellList.setPageSize(20);
    cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
    cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
    
    dataProvider.addDataDisplay(cellList);
    showMorePagerPanel.setDisplay(cellList);
    rangeLabelPager.setDisplay(cellList);
    
    add(showMorePagerPanel);
    add(rangeLabelPager);
    
  }
  
  private void setData(Collection<String> data) {
    dataProvider.getList().clear();
    dataProvider.getList().addAll(data);
    cellList.setRowCount(data.size());
    cellList.setRowData(0, dataProvider.getList());
  }
  
}
