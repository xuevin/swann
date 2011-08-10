package uk.ac.ebi.fgpt.swann.server;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitilizationServletContextListener implements ServletContextListener {
  
  private ServletContext context = null;
  
  @Override
  public void contextDestroyed(ServletContextEvent arg0) {

  }
  
  @Override
  public void contextInitialized(ServletContextEvent arg0) {
    context = arg0.getServletContext();
    context.setAttribute("sessionToTerms", new HashMap<String,Collection<String>>());
  }
}
