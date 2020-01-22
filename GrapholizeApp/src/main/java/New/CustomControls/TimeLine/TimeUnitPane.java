package New.CustomControls.TimeLine;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;

public class TimeUnitPane extends HBox {
    private enum Unit{
        LIMIT("", 0.0d, 10000, 10),
        TEN_SECONDS("s", 0.05d, 10000,10),
        SECONDS("s", 0.2d, 1000,1),
        HUNDRED_MILLISECONDS(" ms", 0.4d, 100,100),
        TEN_MILLISECONDS("ms", 0.8d, 10,10),
        UPPER_LIMIT(" ", 1.0d, 1, 1);
        public final String label;
        public final double threshhold;
        public final int msRatio;
        public final int amount;
        Unit(String label, double threshhold, int msRatio, int amount) {
            this.label = label;
            this.threshhold = threshhold;
            this.msRatio = msRatio;
            this.amount = amount;
        }
    }

    private final double height;
    private final DoubleProperty totalwidth;
    private DoubleProperty scale;
    private Unit unit;
    private HBox labelLayer;
    private HBox markerLayer;

    public TimeUnitPane(DoubleProperty scale, double height, DoubleProperty totalwidth) {
        this.scale = scale;
        this.height = height;
        this.totalwidth = totalwidth;

        setHeight(height);
        setPrefHeight(height);
        setWidth(totalwidth.get());
        setPrefWidth(totalwidth.get());
        setPadding(new Insets(0,0,0,120));
        labelLayer = new HBox();
        markerLayer = new HBox();

        scale.addListener((oldVal,newVal,unused) -> {
            resizeUnits(scale.get(),totalwidth.get());
        });

        totalwidth.addListener((oldVal,newVal,unused) -> {
            resizeUnits(scale.get(), totalwidth.get());
        });

        resizeUnits(scale.get(),totalwidth.get());
    }

    public void resizeUnits(double scale, double totalWidth){
        Unit lastUnit = Unit.TEN_MILLISECONDS;
        for(Unit unit : Unit.values()) {
            if(scale < unit.threshhold){
                this.unit = lastUnit;
                break;
            }
            lastUnit = unit;
        }
        int gapNumber = (int)totalWidth/this.unit.msRatio;
        this.getChildren().clear();
        this.getChildren().add(new Line( 0, 0, 0, height));
        for(int i = 0; i < gapNumber; i++) {
            this.getChildren().add(new Line( 0, 0, 0, height));
        }
        this.setSpacing(unit.msRatio * scale);
    }
}
