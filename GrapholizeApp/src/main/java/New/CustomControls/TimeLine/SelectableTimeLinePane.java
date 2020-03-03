package New.CustomControls.TimeLine;

import New.Controllers.SelectableTimeLineController;
import New.CustomControls.Annotation.SegmentRectangle;
import New.CustomControls.Annotation.SelectableSegmentRectangle;
import New.CustomControls.Containers.TimeLineContainer;
import New.Dialogues.DialogControls.TopicTextControl;
import New.Dialogues.SegmentDialog;
import New.Interfaces.Observer.TimeLineObserver;
import New.Model.Entities.Segment;
import New.Observables.ObservableSegment;
import New.Observables.ObservableTimeLine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.stream.Collectors;

public abstract class SelectableTimeLinePane extends TimeLinePane implements TimeLineObserver {

    private SelectableTimeLineController selectableTimeLineController;
    private BooleanProperty timeLineSelectedProperty;
    protected Set<ObservableSegment> observableSegments;

    protected Light.Point anchor;
    protected Rectangle selection;
    protected boolean selectMode;

    protected SelectableTimeLinePane(double width, double height, DoubleProperty scaleProp, StringProperty name, TimeLineContainer parent, String id) {
        super(width, height, scaleProp, name, id);
        selectableTimeLineController = new SelectableTimeLineController(parent.getSelectedTimeLine());
        this.timeLineSelectedProperty = new SimpleBooleanProperty(false);

        parent.getSelectedTimeLine().addObserver(this);
        this.observableSegments = new TreeSet<>();

        anchor = new Light.Point();
        selection = new Rectangle();

        this.setOnMousePressed(event -> handleTimelineMousePress(event));
        this.setOnMouseDragged(event -> handleTimelineMouseDrag(event));
        this.setOnMouseReleased(event -> handleTimelineMouseRelease(event));
    }

    public void deselectAllElements(SelectableSegmentRectangle selected){
        List<SelectableSegmentRectangle> rects = getChildren().stream()
                .filter(node -> node instanceof SelectableSegmentRectangle)
                .map(node -> (SelectableSegmentRectangle)node)
                .collect(Collectors.toList());
        for(SelectableSegmentRectangle rect : rects){
            if(rect != selected){
                rect.setSelected(false);
            }
        }
    }

    public List<SegmentRectangle> getAnnotations(){
        return getChildren().stream()
                .filter(n -> n instanceof SegmentRectangle)
                .map(n -> (SegmentRectangle)n)
                .sorted(Comparator.comparing(a -> a.getTimeStart()))
                .collect(Collectors.toList());
    }

    public boolean isSelected(){
        return timeLineSelectedProperty.get();
    }

    public BooleanProperty timeLineSelectedPropertyProperty() {
        return timeLineSelectedProperty;
    }

    public void setTimeLineSelected(boolean selected) {
        this.timeLineSelectedProperty.set(selected);
        if(selected){
            selectableTimeLineController.selectTimeLine(this);
        }
    }

    protected void handleTimelineMousePress(MouseEvent event){
        if(event.isPrimaryButtonDown()){
            this.setTimeLineSelected(true);
            getChildren().add(selection);
            selection.setWidth(0);
            selection.setHeight(getHeight());

            anchor.setX(event.getX());
            anchor.setY(0);

            selection.setX(event.getX());
            selection.setY(0);

            if(event.isShiftDown()){
                selectMode = true;
                selection.setFill(new Color(0,0,1,0.5)); //transparent blue
            }
        }
    }

    protected void handleTimelineMouseDrag(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY) && selectMode) {
            selection.setWidth(Math.abs(event.getX() - anchor.getX()));
            selection.setX(Math.min(anchor.getX(), event.getX()));
        }
    }

    protected void handleTimelineMouseRelease(MouseEvent e){
        if(e.getButton().equals(MouseButton.PRIMARY)){
            if(selection.getWidth() > 0){
                //Call annotation creation dialogue
                double timeStart = selection.getX() / scale.get();
                double timeStop = (selection.getX() + selection.getWidth()) / scale.get();
                if(selectMode){
                    observableSegments.stream()
                            .filter(observableSegment -> observableSegment.isWithinTimeRange(timeStart, timeStop))
                            .forEach(observableSegment -> observableSegment.setSelected(true));
                }
            }
        }
        //Reset SelectionRectangle
        selection.setWidth(0);
        selection.setHeight(0);
        getChildren().remove(selection);
        selectMode = false;
    }



    //TODO: Replace with binding
    @Override
    public void update(ObservableTimeLine sender){
        if(!sender.equals(this)){
            this.timeLineSelectedProperty.set(false);
            deselectAllElements(null);
        }
    }


}
