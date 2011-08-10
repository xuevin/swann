package uk.ac.ebi.fgpt.swann.client.view.panel;

import java.util.Map;

import uk.ac.ebi.fgpt.swann.client.event.UpdateInfoEvent;
import uk.ac.ebi.fgpt.swann.client.event.UpdateInfoEventHandeler;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;

public class Annotations extends FlowPanel {
  public Annotations(final SimpleEventBus bus) {
    super();
    
    bus.addHandler(UpdateInfoEvent.TYPE, new UpdateInfoEventHandeler() {
      
      public void onEvent(UpdateInfoEvent event) {
        clear();
        Map<String,Integer> annotations = event.getTop20Annotations();
        int i = 0;
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendHtmlConstant("<table>");
        for (String key : annotations.keySet()) {
          if (i > 20) {
            break;
          }
          sb.appendHtmlConstant("<tr><td>");
          SafeHtml safeValue = SafeHtmlUtils.fromString(key);
          sb.append(safeValue);
          sb.appendHtmlConstant("</td><td>");
          sb.append(annotations.get(key).intValue());
          sb.appendHtmlConstant("</td>");
          i++;
        }
        sb.appendHtmlConstant("</table>");
        add(new InlineHTML(sb.toSafeHtml()));
      }
    });
  }
  
}
