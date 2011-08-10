package uk.ac.ebi.fgpt.swann.client.view.panel;


import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InfoPanel extends VerticalPanel {
  
  
  private ExperimentsList listOfExperiments;
  private Samples listOfSamples;
  private Toggles togglesPanel;
  private CustomClustering clusterPanel;

  public InfoPanel(final SimpleEventBus bus) {
    super();
    getElement().setId("rightInformationStack");
    
    DisclosurePanel statistics = new DisclosurePanel("Statistics");
    DisclosurePanel experiments = new DisclosurePanel("Experiments");
    DisclosurePanel clusters = new DisclosurePanel("Clusters");
    DisclosurePanel toggles = new DisclosurePanel("Toggles");
    
    statistics.getHeader().getElement().setId("disclosurePanelStyle");
    experiments.getHeader().getElement().setId("disclosurePanelStyle");
    clusters.getHeader().getElement().setId("disclosurePanelStyle");
    toggles.getHeader().getElement().setId("disclosurePanelStyle");
    
    listOfExperiments = new ExperimentsList(bus);
    listOfExperiments.setHeight("75px");
    listOfExperiments.setWidth("200px");
    experiments.add(listOfExperiments);
    
    listOfSamples = new Samples(bus);
    listOfSamples.setHeight("50px");
    listOfSamples.setWidth("200px");
    statistics.add(listOfSamples);
    
    togglesPanel = new Toggles(bus);//bus
    togglesPanel.setWidth("200px");
    toggles.add(togglesPanel);
    
    clusterPanel = new CustomClustering(bus);
    clusterPanel.setWidth("200px");
    clusters.add(clusterPanel);
    
    
    add(statistics);
    add(experiments);
    add(toggles);
    add(clusters);
  }
  
//  public void setValues(Collection<String> values) {
//    listOfExperiments.setData(values);
//  }
  
}
