package uk.ac.ebi.fgpt.swann.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class SearchSuggestion implements IsSerializable, Suggestion, Comparable<SearchSuggestion> {
  private String term;
  private String efoAccession;
  private String preferred;
  
  public SearchSuggestion() {
  // Need this to make it serializable
  }
  
  public SearchSuggestion(String term, String preferred, String efoAccession) {
    this.term = term;
    this.efoAccession = efoAccession;
    this.preferred = preferred;
  }
  
  public String getDisplayString() {
    if (!term.equals(preferred)) {
      return term + " - (" + preferred + ")";
    }
    return term;
  }
  
  public String getReplacementString() {
    return preferred;
  }
  
  public String getTerm() {
    return term;
  }
  
  public String getEFOAccession() {
    return efoAccession;
  }
  
  public String getPreferred() {
    return preferred;
  }
  
  public int compareTo(SearchSuggestion item2) {
    return (this.getTerm().toLowerCase()).compareTo((item2.getTerm().toLowerCase()));
  }
  
}
