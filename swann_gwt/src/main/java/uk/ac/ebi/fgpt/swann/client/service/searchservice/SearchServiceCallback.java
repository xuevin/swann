package uk.ac.ebi.fgpt.swann.client.service.searchservice;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

public class SearchServiceCallback implements AsyncCallback<SuggestOracle.Response>{
	private SuggestOracle.Callback callback; 
	private SuggestOracle.Request req; 
	
	public SearchServiceCallback(SuggestOracle.Request req, SuggestOracle.Callback callback){
		this.req = req;
		this.callback = callback;
	}
	public void onFailure(Throwable arg0) {
		System.out.println("Connection problem");
//		callback.onSuggestionsReady(req,new SuggestOracle.Response());
	}
	public void onSuccess(SuggestOracle.Response response) {
		callback.onSuggestionsReady(req, response);
	}
}
