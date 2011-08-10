package uk.ac.ebi.fgpt.swann.client.service.searchservice;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;

public class SearchModule {
	final Label label = new Label("Enter the text in Suggestion box");
	
	public void onModuleLoad() {
		SearchOracle oracle2 = new SearchOracle();
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		oracle.add("Cat");
		oracle.add("Dog");
		oracle.add("Horse");
		oracle.add("india");
		oracle.add("Canada");
		oracle.add("France");
		oracle.add("uk");
		oracle.add("Japan");
		oracle.add("Russia");

		SuggestBox box = new SuggestBox(oracle2);
		RootPanel.get("search").add(label);
		RootPanel.get("search").add(box);
	};

}