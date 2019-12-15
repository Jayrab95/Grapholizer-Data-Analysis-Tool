package New.Model.ObservableModel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Line;

public class DotLine  extends Line {

    private BooleanProperty lineSelected;
    private BooleanProperty strokeSelected;
    private BooleanBinding dotBinding;

    private DoubleProperty canvasScale;

    private ObservableDot dot1;
    private ObservableDot dot2;

    private ObservableList<BooleanProperty> dotsSelectedProperties;


    public DotLine(ObservableDot dot1, ObservableDot dot2, BooleanProperty strokeSelected){
        this.lineSelected = new SimpleBooleanProperty(false);

        this.strokeSelected = new SimpleBooleanProperty(strokeSelected.get());
        this.strokeSelected.bind(strokeSelected);
        this.strokeSelected.addListener((observable, oldValue, newValue) -> lineSelected.setValue(newValue));

        this.dot1 = dot1;
        this.dot2 = dot2;

        this.dotsSelectedProperties = FXCollections.observableArrayList(dot1.getSelectedProperty(), dot2.getSelectedProperty());

        //Source: http://andresalmiray.com/creating-aggregate-javafx-bindings/
        this.dotBinding = Bindings.createBooleanBinding(
                () -> dotsSelectedProperties.stream().allMatch(BooleanProperty::get),
                dotsSelectedProperties
        );


    }

}
