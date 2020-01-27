package New.CustomControls.TimeLine;

import New.Controllers.SelectableTimeLineController;
import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.Annotation.SelectableAnnotationRectangle;
import New.CustomControls.Containers.TimeLineContainer;
import New.Interfaces.Observer.TimeLineObserver;
import New.Observables.ObservableTimeLine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SelectableTimeLinePane extends TimeLinePane implements TimeLineObserver {

    private SelectableTimeLineController selectableTimeLineController;
    private BooleanProperty timeLineSelectedProperty;

    protected SelectableTimeLinePane(double width, double height, DoubleProperty scaleProp, StringProperty name, TimeLineContainer parent, String id) {
        super(width, height, scaleProp, name, id);
        selectableTimeLineController = new SelectableTimeLineController(parent.getSelectedTimeLine());
        this.timeLineSelectedProperty = new SimpleBooleanProperty(false);

        parent.getSelectedTimeLine().addObserver(this);
    }

    public void deselectAllElements(SelectableAnnotationRectangle selected){
        for(Node n : getChildren()){
            if(n instanceof AnnotationRectangle){
                SelectableAnnotationRectangle ar = (SelectableAnnotationRectangle)n;
                if(ar != selected){
                    ar.setSelected(false);
                }
            }
        }
    }

    public List<AnnotationRectangle> getAnnotations(){
        return getChildren().stream()
                .filter(n -> n instanceof AnnotationRectangle)
                .map(n -> (AnnotationRectangle)n)
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

    @Override
    public void update(ObservableTimeLine sender){
        if(!sender.equals(this)){
            this.timeLineSelectedProperty.set(false);
            deselectAllElements(null);
        }
    }
}
