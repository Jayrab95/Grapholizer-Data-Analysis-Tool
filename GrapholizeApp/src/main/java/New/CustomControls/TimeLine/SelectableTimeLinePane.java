package New.CustomControls.TimeLine;

import New.Controllers.SelectableTimeLineController;
import New.CustomControls.Annotation.SegmentRectangle;
import New.CustomControls.Annotation.SelectableSegmentRectangle;
import New.CustomControls.Containers.TimeLineContainer;
import New.Interfaces.Observer.TimeLineObserver;
import New.Observables.ObservableSegment;
import New.Observables.ObservableTimeLine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class SelectableTimeLinePane extends TimeLinePane implements TimeLineObserver {

    private SelectableTimeLineController selectableTimeLineController;
    private BooleanProperty timeLineSelectedProperty;
    protected Set<ObservableSegment> observableSegments;

    protected SelectableTimeLinePane(double width, double height, DoubleProperty scaleProp, StringProperty name, TimeLineContainer parent, String id) {
        super(width, height, scaleProp, name, id);
        selectableTimeLineController = new SelectableTimeLineController(parent.getSelectedTimeLine());
        this.timeLineSelectedProperty = new SimpleBooleanProperty(false);

        parent.getSelectedTimeLine().addObserver(this);
        this.observableSegments = new TreeSet<>();
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

    //TODO: Replace with binding
    @Override
    public void update(ObservableTimeLine sender){
        if(!sender.equals(this)){
            this.timeLineSelectedProperty.set(false);
            deselectAllElements(null);
        }
    }


}
