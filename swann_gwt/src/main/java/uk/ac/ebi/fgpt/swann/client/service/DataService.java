package uk.ac.ebi.fgpt.swann.client.service;

import java.io.IOException;
import java.util.ArrayList;

import uk.ac.ebi.fgpt.swann.model.Point;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("dataService")
public interface DataService extends RemoteService {
  ArrayList<Point> getPoints(int width, int height, int i, int j) throws IllegalArgumentException,
                                                                 IOException;
}
