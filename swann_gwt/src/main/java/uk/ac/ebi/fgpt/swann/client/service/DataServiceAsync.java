package uk.ac.ebi.fgpt.swann.client.service;

import java.util.ArrayList;

import uk.ac.ebi.fgpt.swann.model.Point;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {
  public void getPoints(int width, int height, int i, int j, AsyncCallback<ArrayList<Point>> callback);
}
