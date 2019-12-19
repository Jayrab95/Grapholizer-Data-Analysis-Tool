package New.Observables;

import New.Model.Entities.SimpleColor;
import New.Model.Entities.TimeLineTag;
import New.util.ColorConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class ObservableTimeLineTag {
    private TimeLineTag inner;

    StringProperty tagProperty;
    ObjectProperty<Color> colorProperty;

    public ObservableTimeLineTag(TimeLineTag inner){
        this.inner = inner;
        tagProperty = new SimpleStringProperty(inner.getTag());
        tagProperty.addListener((observable, oldValue, newValue) -> inner.setTag(newValue));
        colorProperty = new SimpleObjectProperty<>(ColorConverter.convertModelColorToJavaFXColor(inner.getSimpleColor()));
        colorProperty.addListener((observable, oldValue, newValue) -> inner.setSimpleColor(
                ColorConverter.convertJavaFXColorToModelColor(newValue)));
    }

    public String getTag() {
        return tagProperty.get();
    }

    public StringProperty getTagProperty() {
        return tagProperty;
    }

    public void setTag(String tagProperty) {
        this.tagProperty.set(tagProperty);
    }

    public Color getColor() {
        return colorProperty.get();
    }

    public ObjectProperty<Color> getColorProperty() {
        return colorProperty;
    }

    public void setColor(Color color) {
        this.colorProperty.set(color);
    }

}
