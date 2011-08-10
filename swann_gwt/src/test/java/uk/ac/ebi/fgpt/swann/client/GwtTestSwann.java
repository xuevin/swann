package uk.ac.ebi.fgpt.swann.client;

import java.util.ArrayList;

import uk.ac.ebi.fgpt.swann.client.service.DataService;
import uk.ac.ebi.fgpt.swann.client.service.DataServiceAsync;
import uk.ac.ebi.fgpt.swann.model.Point;
import uk.ac.ebi.fgpt.swann.shared.FieldVerifier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * GWT JUnit <b>integration</b> tests must extend GWTTestCase. Using <code>"GwtTest*"</code> naming pattern
 * exclude them from running with surefire during the test phase.
 */
public class GwtTestSwann extends GWTTestCase {
  
  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "uk.ac.ebi.fgpt.swann.SwannJUnit";
  }
  
  /**
   * Tests the FieldVerifier.
   */
  public void testFieldVerifier() {
    assertFalse(FieldVerifier.isValidName(null));
    assertFalse(FieldVerifier.isValidName(""));
    assertFalse(FieldVerifier.isValidName("a"));
    assertFalse(FieldVerifier.isValidName("ab"));
    assertFalse(FieldVerifier.isValidName("abc"));
    assertTrue(FieldVerifier.isValidName("abcd"));
  }
  
  /**
   * This test will send a request to the server using the greetServer method in dataService and verify the
   * response.
   */
  public void testDataService() {
    // Create the service that we will test.
    DataServiceAsync dataService = GWT.create(DataService.class);
    
    ServiceDefTarget target = (ServiceDefTarget) dataService;
    target.setServiceEntryPoint(GWT.getModuleBaseURL() + "Swann/dataService");
    
    // Since RPC calls are asynchronous, we will need to wait for a response
    // after this test method returns. This line tells the test runner to wait
    // up to 10 seconds before timing out.
    delayTestFinish(10000);
    
    // Send a request to the server.
    dataService.getPoints(100, 100, 1, 2, new AsyncCallback<ArrayList<Point>>() {
      public void onFailure(Throwable caught) {
        // The request resulted in an unexpected error.
        fail("Request failure: " + caught.getMessage());
      }
      
      public void onSuccess(ArrayList<Point> result) {
        assertEquals("E-GEOD-10715", result.get(0).getExperiment());
        finishTest();
      }
    });
  }
  
}
