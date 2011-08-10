package uk.ac.ebi.fgpt.swann.client.service.searchservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.SuggestOracle;

@RemoteServiceRelativePath("searchService")
public interface SearchService extends RemoteService {
  
  /**
   * This method is used to do server side asynchronous autosuggestion. Send a SuggestOracle.Request and
   * receive a Suggest.Oracle.Response
   * 
   * @param req
   *          This is a SuggestOracle.Request
   * @return A SuggestOracle.Response is returned
   */
  SuggestOracle.Response getTerm(SuggestOracle.Request req);
}
