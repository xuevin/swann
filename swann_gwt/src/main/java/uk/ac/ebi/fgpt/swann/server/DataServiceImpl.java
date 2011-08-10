package uk.ac.ebi.fgpt.swann.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import uk.ac.ebi.fgpt.swann.client.service.DataService;
import uk.ac.ebi.fgpt.swann.model.Color;
import uk.ac.ebi.fgpt.swann.model.Point;
import uk.ac.ebi.fgpt.swann.model.ViewOfScaledScatterPlot;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements DataService {
  
  private HashMap<String,ViewOfScaledScatterPlot> serviceIdToView = new HashMap<String,ViewOfScaledScatterPlot>();
  
  public ArrayList<Point> getPoints(int width, int height, int i, int j) throws IllegalArgumentException,
                                                                        IOException {
    File annotations = new File("/home/chrnovx/private_data/annotations.samples.txt");
    File coords = new File(
//     "/home/chrnovx/private_data/assembly2_30915x3.txt_with_names.copy_short.txt");
        "/home/chrnovx/private_data/assembly2_30915x3.txt_with_names.copy.txt");
    
    // First match the points and annotate them
    DataProvider provider = new DataProvider(coords, annotations, i, j);
    
    // Make a new view with the given dimensions
    ViewOfScaledScatterPlot view = new ViewOfScaledScatterPlot(width, height);
    
    // Add all the annotated points to the view
    view.addColoredPointsToScatterPlot(provider.getAnnotatedPointsAsArrayList(), new Color(0, 0, 0));
    serviceIdToView.put(getSessionId(), view);
    
    getServiceIdToTerms().put(getSessionId(), provider.getSortedCollectionOfAllTerms());
    
    return provider.getAnnotatedPointsAsArrayList();
  }
  
  private String getSessionId() {
    return this.getThreadLocalRequest().getSession().getId();
  }
  
  private HashMap<String,Collection<String>> getServiceIdToTerms() {
//    if (getServletContext().getAttribute("sessionToTerms") == null) {
//      getServletContext().setAttribute("sessionToTerms", new HashMap<String,Collection<String>>());
//    }
    return (HashMap<String,Collection<String>>) getServletContext().getAttribute("sessionToTerms");
  }
  
}
