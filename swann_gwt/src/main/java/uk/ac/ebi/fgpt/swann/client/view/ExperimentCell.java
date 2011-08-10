package uk.ac.ebi.fgpt.swann.client.view;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class ExperimentCell extends AbstractCell<String> {
  
  @Override
  public void render(Context context, String value, SafeHtmlBuilder sb) {
    if (value == null) {
      return;
    }
    
    // If the value comes from the user, we escape it to avoid XSS attacks.
    SafeHtml safeValue = SafeHtmlUtils.fromString(value);
    
    // sb.appendHtmlConstant("<div style=\"color:" + safeValue.asString()
    // + "\">");
    sb.appendHtmlConstant("<a href = \"http://www.ebi.ac.uk/arrayexpress/experiments/" + safeValue.asString()
                          + "\"target=\"_blank\">");
    sb.append(safeValue);
    sb.appendHtmlConstant("</a>");
    // sb.appendHtmlConstant("</div>");
    
  }
  
}
