package New.Model.ObservableModel;

import New.Model.Entities.SimpleColor;
import New.Model.Entities.TimeLineTag;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObservableTimeLineTag {
    private TimeLineTag inner;
    StringProperty tagProperty;
    ObjectProperty<SimpleColor> colorProperty;

    public ObservableTimeLineTag(TimeLineTag inner){
        this.inner = inner;
        tagProperty = new SimpleStringProperty(inner.getTag());
        tagProperty.addListener((observable, oldValue, newValue) -> inner.setTag(newValue));
        colorProperty = new SimpleObjectProperty<>();
        colorProperty.addListener((observable, oldValue, newValue) -> inner.setSimpleColor(newValue));
    }

}
