package New.CustomControls.TimeLine;

import New.Controllers.SelectableTimeLineController;
import New.CustomControls.TimeLineContainer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

public abstract class SelectableTimeLinePane extends TimeLinePane {

    private SelectableTimeLineController selectableTimeLineController;
    private BooleanProperty timeLineSelectedProperty;

    protected SelectableTimeLinePane(double width, double height, DoubleProperty scaleProp, StringProperty name, TimeLineContainer parent) {
        super(width, height, scaleProp, name);
        selectableTimeLineController = new SelectableTimeLineController(parent);
        this.timeLineSelectedProperty = new SimpleBooleanProperty(false);
    }

    public boolean isSelected(){
        return timeLineSelectedProperty.get();
    }

    public BooleanProperty timeLineSelectedPropertyProperty() {
        return timeLineSelectedProperty;
    }

    public void setTimeLineSelected(boolean selected) {
        this.timeLineSelectedProperty.set(selected);
    }
}
