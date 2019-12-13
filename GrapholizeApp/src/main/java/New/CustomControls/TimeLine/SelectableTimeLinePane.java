package New.CustomControls.TimeLine;

import New.Controllers.SelectableTimeLineController;
import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.TimeLineContainer;
import New.Interfaces.Observable;
import New.Interfaces.Observer;
import New.Model.ObservableModel.ObservableTimeLine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public abstract class SelectableTimeLinePane extends TimeLinePane implements Observer {

    private SelectableTimeLineController selectableTimeLineController;
    private BooleanProperty timeLineSelectedProperty;

    protected SelectableTimeLinePane(double width, double height, DoubleProperty scaleProp, StringProperty name, TimeLineContainer parent) {
        super(width, height, scaleProp, name);
        selectableTimeLineController = new SelectableTimeLineController(parent.getSelectedTimeLine());
        this.timeLineSelectedProperty = new SimpleBooleanProperty(false);

        parent.getSelectedTimeLine().addObserver(this);
    }

    public void deselectAllElements(){
        for(Node n : getChildren()){
            ((AnnotationRectangle)n).setSelected(false);
        }
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
    public void update(Observable sender){
        //TODO: Is this check necessary? The annotation already checks if the parent is the selected timeline.
        if(!((ObservableTimeLine)sender).equals(this)){
            deselectAllElements();
        }
    }
}
