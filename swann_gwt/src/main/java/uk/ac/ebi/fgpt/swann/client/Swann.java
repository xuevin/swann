package uk.ac.ebi.fgpt.swann.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ebi.fgpt.swann.client.event.UpdateClusterEvent;
import uk.ac.ebi.fgpt.swann.client.event.UpdateClusterEventHandeler;
import uk.ac.ebi.fgpt.swann.client.event.UpdateInfoEvent;
import uk.ac.ebi.fgpt.swann.client.event.toggles.BoxExperimentsEvent;
import uk.ac.ebi.fgpt.swann.client.event.toggles.BoxExperimentsEventHandeler;
import uk.ac.ebi.fgpt.swann.client.event.toggles.CircleSamplesEvent;
import uk.ac.ebi.fgpt.swann.client.event.toggles.CircleSamplesEventHandeler;
import uk.ac.ebi.fgpt.swann.client.event.toggles.UseExclusiveOrEvent;
import uk.ac.ebi.fgpt.swann.client.event.toggles.UseExclusiveOrEventHandeler;
import uk.ac.ebi.fgpt.swann.client.service.DataService;
import uk.ac.ebi.fgpt.swann.client.service.DataServiceAsync;
import uk.ac.ebi.fgpt.swann.client.view.panel.Annotations;
import uk.ac.ebi.fgpt.swann.client.view.panel.InfoPanel;
import uk.ac.ebi.fgpt.swann.model.Color;
import uk.ac.ebi.fgpt.swann.model.ColoredHashSetOfPoints;
import uk.ac.ebi.fgpt.swann.model.Point;
import uk.ac.ebi.fgpt.swann.model.ViewOfScaledScatterPlot;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Swann implements EntryPoint {
  /*
   * This canvas draws all the the buffers
   */
  private Canvas mainCanvas;
  private Context2d mainContext;
  /*
   * This canvas draws the points on the plot
   */
  private Canvas plotCanvas;
  private Context2d plotContext;
  
  /*
   * This canvas holds all of the squares which box all the points in the same experiment
   */
  private Canvas sameExperimentsCanvas;
  private Context2d sameExperimentsContext;
  
  /*
   * This canvas holds all of the circles which encapsulate all the points just highlighted
   */
  private Canvas pointsInLastHighlightCanvas;
  private Context2d pointsInLastHighlightContext;
  /*
   * This canvas draws the axis
   */
  private Canvas axisCanvas;
  private Context2d axisContext;
  
  private final int WIDTH = 500;
  private final int HEIGHT = 500;
  private final int PLOTOFFSET_X = 20;
  private final int PLOTOFFSET_Y = 20;
  private final int PLOTWIDTH = WIDTH - 2 * PLOTOFFSET_X;
  private final int PLOTHEIGHT = HEIGHT - 2 * PLOTOFFSET_Y;
  
  private int firstMouseX = -1;
  private int firstMouseY = -1;
  private int mouseX;
  private int mouseY;
  
  private Set<String> experimentAccessionsInLastHighlight;
  private HashSet<Point> pointsInLastHighlight;
  private boolean shouldBoxAllExperiments = false;
  private boolean shouldCircleSamples = true;
  private boolean shouldShowOtherPoints = false;
  
  private DataServiceAsync service = GWT.create(DataService.class);
  
  private ViewOfScaledScatterPlot view;
  
  private final SimpleEventBus bus = new SimpleEventBus();
  private final InfoPanel infoPanel = new InfoPanel(bus);
  private final Annotations annotationsPanel = new Annotations(bus);
  
  public void onModuleLoad() {
    
    experimentAccessionsInLastHighlight = new HashSet<String>();
    pointsInLastHighlight = new HashSet<Point>();
    
    initCanvas();
    
    view = new ViewOfScaledScatterPlot(PLOTWIDTH, PLOTHEIGHT);
    
    fetchDataFromServer(1, 2);
    
    RootPanel.get("infoContainer").add(infoPanel);
    RootPanel.get("plotContainer").add(mainCanvas);
    RootPanel.get("annotationsContainer").add(annotationsPanel);
    
    initHandelers();
    
    // setup timer
    final Timer timer = new Timer() {
      @Override
      public void run() {
        draw();
      }
    };
    timer.scheduleRepeating(60);
  }
  
  private void fetchDataFromServer(int i, int j) {
    // Draw scatterplot into buffer
    service.getPoints(PLOTWIDTH, PLOTHEIGHT, i, j, new AsyncCallback<ArrayList<Point>>() {
      
      public void onSuccess(ArrayList<Point> result) {
        view.addColoredPointsToScatterPlot(result, new Color(0, 0, 0));
        updatePlotContext();
        updateAxisContext();
      }
      
      public void onFailure(Throwable caught) {

      }
    });
    
  }
  
  private void initCanvas() {
    mainCanvas = Canvas.createIfSupported();
    mainCanvas.setSize(WIDTH + "px", HEIGHT + "px");
    mainCanvas.setCoordinateSpaceWidth(WIDTH);
    mainCanvas.setCoordinateSpaceHeight(HEIGHT);
    mainCanvas.setTabIndex(-1);
    mainContext = mainCanvas.getContext2d();
    
    plotCanvas = Canvas.createIfSupported();
    plotCanvas.setSize(PLOTWIDTH + "px", PLOTHEIGHT + "px");
    plotCanvas.setCoordinateSpaceWidth(PLOTWIDTH);
    plotCanvas.setCoordinateSpaceHeight(PLOTHEIGHT);
    plotContext = plotCanvas.getContext2d();
    
    sameExperimentsCanvas = Canvas.createIfSupported();
    sameExperimentsCanvas.setSize(PLOTWIDTH + "px", PLOTHEIGHT + "px");
    sameExperimentsCanvas.setCoordinateSpaceWidth(PLOTWIDTH);
    sameExperimentsCanvas.setCoordinateSpaceHeight(PLOTHEIGHT);
    sameExperimentsContext = sameExperimentsCanvas.getContext2d();
    
    pointsInLastHighlightCanvas = Canvas.createIfSupported();
    pointsInLastHighlightCanvas.setSize(PLOTWIDTH + "px", PLOTHEIGHT + "px");
    pointsInLastHighlightCanvas.setCoordinateSpaceWidth(PLOTWIDTH);
    pointsInLastHighlightCanvas.setCoordinateSpaceHeight(PLOTHEIGHT);
    pointsInLastHighlightContext = pointsInLastHighlightCanvas.getContext2d();
    
    axisCanvas = Canvas.createIfSupported();
    axisCanvas.setSize(WIDTH + "px", HEIGHT + "px");
    axisCanvas.setCoordinateSpaceWidth(WIDTH);
    axisCanvas.setCoordinateSpaceHeight(HEIGHT);
    axisCanvas.setTabIndex(-1);
    axisContext = axisCanvas.getContext2d();
    
  }
  
  private void initHandelers() {
    mainCanvas.addMouseMoveHandler(new MouseMoveHandler() {
      public void onMouseMove(MouseMoveEvent event) {
        
        mouseX = event.getX();
        mouseY = event.getY();
      }
    });
    mainCanvas.addMouseDownHandler(new MouseDownHandler() {
      
      public void onMouseDown(MouseDownEvent event) {
        if (firstMouseX == -1 && firstMouseY == -1) {
          firstMouseX = mouseX;
          firstMouseY = mouseY;
        }
        
      }
    });
    mainCanvas.addMouseUpHandler(new MouseUpHandler() {
      
      public void onMouseUp(MouseUpEvent event) {
        if (!event.isShiftKeyDown()) {
          pointsInLastHighlight.clear();
          experimentAccessionsInLastHighlight.clear();
        }
        
        pointsInLastHighlight.addAll(view.extractEverythingInRectangularRegion(firstMouseX - PLOTOFFSET_X,
          firstMouseY - PLOTOFFSET_Y, mouseX - PLOTOFFSET_X, mouseY - PLOTOFFSET_Y));
        
        firstMouseX = -1;
        firstMouseY = -1;
        
        for (Point point : pointsInLastHighlight) {
          experimentAccessionsInLastHighlight.add(point.getExperiment());
        }
        
        // EVERYTHING BELOW IS EXPENSIVE
        updateSameExperimentsContext();
        updatePointsInLastHighlightContext();
        bus.fireEvent(new UpdateInfoEvent(experimentAccessionsInLastHighlight, pointsInLastHighlight));
        
      }
    });
    bus.addHandler(BoxExperimentsEvent.TYPE, new BoxExperimentsEventHandeler() {
      
      public void onEvent(BoxExperimentsEvent event) {
        shouldBoxAllExperiments = event.shouldBoxSimilarExperiments();
        updateSameExperimentsContext();
        
      }
    });
    bus.addHandler(CircleSamplesEvent.TYPE, new CircleSamplesEventHandeler() {
      
      public void onEvent(CircleSamplesEvent event) {
        shouldCircleSamples = event.shouldCircleSamples();
        updatePointsInLastHighlightContext();
        updateSameExperimentsContext();
      }
    });
    bus.addHandler(UpdateClusterEvent.TYPE, new UpdateClusterEventHandeler() {
      
      public void onEvent(UpdateClusterEvent event) {
        view.recolor(event.getTermToColor());
        updatePlotContext();
        updateSameExperimentsContext();
      }
    });
    
    bus.addHandler(UseExclusiveOrEvent.TYPE, new UseExclusiveOrEventHandeler() {
      
      @Override
      public void onEvent(UseExclusiveOrEvent event) {
      // TODO Do something when the toggle is pressed
      
      }
    });
    
  }
  
  // FIXME this is an expensive task
  private void updateSameExperimentsContext() {
    sameExperimentsContext.clearRect(0, 0, PLOTWIDTH, PLOTHEIGHT);
    if (shouldBoxAllExperiments) {
      for (ColoredHashSetOfPoints set : view.getAllPoints()) {
        sameExperimentsContext.beginPath();
        Color color = set.getColor();
        sameExperimentsContext.setStrokeStyle(CssColor
            .make(color.getRed(), color.getGreen(), color.getBlue()));
        for (Point point : set) {
          // STEP 2 WHILE ITERATING, DRAW BOXES AROUND THE EXPERIMENTS JUST HIGHLIGHTED
          if (experimentAccessionsInLastHighlight.contains(point.getExperiment())) {
            sameExperimentsContext.rect(point.getScaledXPosition() - 5, point.getScaledYPosition() - 5, 10,
              10);
          }
        }
        sameExperimentsContext.stroke();
      }
    }
    
  }
  
  private void updatePlotContext() {
    
    ImageData imgData = plotContext.getImageData(0, 0, PLOTWIDTH, PLOTHEIGHT);
    for (ColoredHashSetOfPoints set : view.getAllPoints()) {
      Color color = set.getColor();
      int red = color.getRed();
      int green = color.getGreen();
      int blue = color.getBlue();
      
      for (Point point : set) {
        imgData.setBlueAt(blue, point.getScaledXPosition(), point.getScaledYPosition());
        imgData.setRedAt(red, point.getScaledXPosition(), point.getScaledYPosition());
        imgData.setGreenAt(green, point.getScaledXPosition(), point.getScaledYPosition());
        imgData.setAlphaAt(255, point.getScaledXPosition(), point.getScaledYPosition());
      }
      // if(shouldUseLargerPoints){
      // plotContext.set
      // }
      // plotContext.beginPath();
      // plotContext.closePath();
      // plotContext.stroke();
    }
    plotContext.putImageData(imgData, 0, 0);
  }
  
  private void updateAxisContext() {
    axisContext.beginPath();
    axisContext.moveTo(WIDTH - PLOTOFFSET_X, HEIGHT - PLOTOFFSET_Y);
    axisContext.lineTo(PLOTOFFSET_X, HEIGHT - PLOTOFFSET_Y);
    axisContext.lineTo(PLOTOFFSET_X, PLOTOFFSET_Y);
    axisContext.stroke();
    axisContext.setFillStyle("#000000");
    axisContext.setFont("12 pt Calibri");
    axisContext.setTextAlign(TextAlign.RIGHT);
    axisContext.fillText(view.getYAxisUpper() + "", PLOTOFFSET_X, PLOTOFFSET_Y);
    axisContext.fillText(view.getYAxisLower() + "", PLOTOFFSET_X, HEIGHT - PLOTOFFSET_Y);
    axisContext.fillText(view.getXAxisUpper() + "", WIDTH - PLOTOFFSET_X, HEIGHT - PLOTOFFSET_Y + 10);
    axisContext.fillText(view.getXAxisLower() + "", PLOTOFFSET_X, HEIGHT - PLOTOFFSET_Y + 10);
    axisContext.closePath();
  }
  
  private void updatePointsInLastHighlightContext() {
    pointsInLastHighlightContext.clearRect(0, 0, PLOTWIDTH, PLOTHEIGHT);
    if (shouldCircleSamples) {
      for (Point point : pointsInLastHighlight) {
        pointsInLastHighlightContext.beginPath();
        pointsInLastHighlightContext.arc(point.getScaledXPosition(), point.getScaledYPosition(), 3, 0,
          2 * Math.PI);
        pointsInLastHighlightContext.closePath();
        pointsInLastHighlightContext.stroke();
      }
    }
  }
  
  private void draw() {
    // Clear Image
    
    mainContext.clearRect(0, 0, WIDTH, HEIGHT);
    // mainContext.setFillStyle("#000000");
    // mainContext.fillRect(0, 0, mainCanvas.getCoordinateSpaceWidth(), mainCanvas.getCoordinateSpaceWidth());
    drawScatterPlot(mainContext);
    drawAxis(mainContext);
    drawBoxAroundSameExperiments(mainContext);
    drawCircleAroundLastHighlightedPoints(mainContext);
    drawHighlightingRectangle(mainContext);
  }
  
  private void drawCircleAroundLastHighlightedPoints(Context2d context) {
    context.drawImage(pointsInLastHighlightCanvas.getCanvasElement(), PLOTOFFSET_X, PLOTOFFSET_Y);
  }
  
  private void drawBoxAroundSameExperiments(Context2d context) {
    context.drawImage(sameExperimentsCanvas.getCanvasElement(), PLOTOFFSET_X, PLOTOFFSET_Y);
    
  }
  
  private void drawHighlightingRectangle(Context2d context) {
    if (firstMouseX != -1 && firstMouseY != -1) {
      context.beginPath();
      context.rect(firstMouseX, firstMouseY, mouseX - firstMouseX, mouseY - firstMouseY);
      context.stroke();
      context.closePath();
    }
    
  }
  
  private void drawScatterPlot(Context2d context) {
    context.drawImage(plotCanvas.getCanvasElement(), PLOTOFFSET_X, PLOTOFFSET_Y);
  }
  
  private void drawAxis(Context2d context) {
    context.drawImage(axisCanvas.getCanvasElement(), 0, 0);
  }
}
