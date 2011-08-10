package uk.ac.ebi.fgpt.swann.client.event;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import uk.ac.ebi.fgpt.swann.model.Point;
import uk.ac.ebi.fgpt.swann.model.utils.AnnotationUtils;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateInfoEvent extends GwtEvent<UpdateInfoEventHandeler> {
  
  public static Type<UpdateInfoEventHandeler> TYPE = new Type<UpdateInfoEventHandeler>();
  
  private Collection<String> experiments;
  private Collection<Point> pointsHighlighted;
  
  public UpdateInfoEvent(Collection<String> experimentsInLastHighlight, Collection<Point> pointsHighlighted) {
    this.experiments = experimentsInLastHighlight;
    this.pointsHighlighted = pointsHighlighted;
  }
  
  @Override
  protected void dispatch(UpdateInfoEventHandeler handler) {
    handler.onEvent(this);
  }
  
  @Override
  public Type<UpdateInfoEventHandeler> getAssociatedType() {
    return TYPE;
  }
  
  public Collection<String> getExperiments() {
    return experiments;
  }
  
  public Map<String,Integer> getTop20Annotations() {
    Collection<Map.Entry<String,Integer>> annotations = AnnotationUtils
        .getSortedCollectionOfAnnotations(pointsHighlighted);
    
    // Order matters so use LinkedHashMap
    Map<String,Integer> outputHashMap = new LinkedHashMap<String,Integer>();
    int i = 0;
    for (Map.Entry<String,Integer> pair : annotations) {
      if (i >= 20) {
        break;
      }
      outputHashMap.put(pair.getKey(), pair.getValue());
      i++;
    }
    return outputHashMap;
  }
  
  public int getNumberOfSamplesHighlighted() {
    return pointsHighlighted.size();
  }
  
}
