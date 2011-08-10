package uk.ac.ebi.fgpt.swann.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import uk.ac.ebi.fgpt.swann.client.service.searchservice.SearchService;
import uk.ac.ebi.fgpt.swann.model.ViewOfScaledScatterPlot;
import uk.ac.ebi.fgpt.swann.shared.SearchSuggestion;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SearchServiceImpl extends RemoteServiceServlet implements SearchService {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public Response getTerm(Request req) {
    // Get the session
    Collection<String> sortedListOfTerms = getServiceIdToTerms().get(getSessionId());
    SuggestOracle.Response response = new SuggestOracle.Response();
    String query = req.getQuery();
    List<SearchSuggestion> listOfEFOTerms = new ArrayList<SearchSuggestion>();
    int j = 0;
    for (String term : sortedListOfTerms) {
      if (term.toLowerCase().startsWith(query.toLowerCase())) {
        listOfEFOTerms.add(new SearchSuggestion(term, term, term));
        if (j > req.getLimit()) {
          break;
        }
        j++;
      }
      
    }
    response.setSuggestions(listOfEFOTerms);
    return response;
  }
  
  private String getSessionId() {
    return this.getThreadLocalRequest().getSession().getId();
  }
  
  private HashMap<String,Collection<String>> getServiceIdToTerms() {
    return (HashMap<String,Collection<String>>) getServletContext().getAttribute("sessionToTerms");
  }
}
