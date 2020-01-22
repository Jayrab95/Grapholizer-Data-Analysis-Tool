package New.Observables;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;

public class DotLine  extends Line {

    private BooleanProperty lineSelected;
    private ObjectProperty<Color> lineColor;
    //private BooleanProperty strokeSelected;
    //private BooleanBinding dotBinding;

    private ObservableDot dot1;
    private ObservableDot dot2;

    //private ObservableList<BooleanProperty> dotsSelectedProperties;


    public DotLine(ObservableDot dot1, ObservableDot dot2, DoubleProperty scale){
        this.dot1 = dot1;
        this.dot2 = dot2;

        this.lineSelected = new SimpleBooleanProperty(false);
        this.lineColor = new SimpleObjectProperty<>(dot1.getColorProperty().get());


        //Problem: At this point, the data is invalidated. For some reason, it does not get validated
        //until a page change happens.
        //eventhough the binding happens, it could be that the dotLine does not take note of that change
        //Current Solution: ContentSwitcher needs to be initialized before Canvas
        //Reason: ContentSwitcher sets the active page when it becomes initialized.
        //If Canvas is initialized first, the dotlines may lose their reference to their dots
        //Only after a page change will they function correctly.
        //Perhaps the content switcher should not set the page on initialization
        this.lineSelected.bind(dot1.getSelectedProperty());
        this.lineColor.bind(dot1.getColorProperty());


        /*dot2.getSelectedProperty().addListener(observable -> lineSelected.set(dot1.isSelected() && dot2.isSelected()));



        this.dotsSelectedProperties = FXCollections.observableArrayList(dot1.getSelectedProperty(), dot2.getSelectedProperty());
        this.dotsSelectedProperties.addListener((InvalidationListener) observable -> {
            lineSelected.set(dotsSelectedProperties.stream().allMatch(BooleanProperty::get));
            System.out.println("Line selected!!!");
        });
        //Source: http://andresalmiray.com/creating-aggregate-javafx-bindings/
        this.dotBinding = Bindings.createBooleanBinding(
                () -> dotsSelectedProperties.stream().allMatch(BooleanProperty::get),
                dotsSelectedProperties
        );

        //this.dotBinding.addListener(observable -> lineSelected.set(dotBinding.getValue()));

         */

        this.lineSelected.addListener(observable -> setStyle());

        setStyle();

        this.lineColor.addListener(observable -> setStyle());

        setCoordinates(scale.get());

        scale.addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                setCoordinates(newValue.doubleValue());
            }
        });
    }

    private void setStyle(){
        if(!lineSelected.get()){
            setStroke(lineColor.get());
        }
        else{
            //Source: https://stackoverflow.com/questions/28764190/javafx-line-fill-color?noredirect=1&lq=1
            setStroke(new LinearGradient(0d, -5d, 0d, 5d, false,
                    CycleMethod.NO_CYCLE, new Stop(0,Color.BLACK),
                    new Stop(0.199,Color.GREEN),
                    new Stop(0.2,lineColor.get()),
                    new Stop(0.799,lineColor.get()),
                    new Stop(0.8,Color.GREEN)));
        }

    }

    private void setCoordinates(double scale){
        setStartX(dot1.getX() * scale);
        setStartY(dot1.getY() * scale);
        setEndX(dot2.getX() * scale);
        setEndY(dot2.getY() * scale);
    }

}
