package New.CustomControls.SegmentationPanes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class TimeUnitPane extends VBox {
    private enum Unit{
        LIMIT("s", 0.0d, 10000, 10),
        TEN_SECONDS("s", 0.05d, 10000,10),
        SECONDS("s", 0.1d, 1000,1),
        HUNDRED_MILLISECONDS(" hms", 0.6d, 100,1),
        UPPER_LIMIT(" ", 1.1d, 1, 1);

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
    private TilePane labelLayer;
    private TilePane markerLayer;

    public TimeUnitPane(DoubleProperty scale, double height, DoubleProperty totalwidth) {
        this.scale = new SimpleDoubleProperty(scale.get());
        this.scale.bind(scale);
        this.height = height;
        this.totalwidth = new SimpleDoubleProperty(totalwidth.get());
        this.totalwidth.bind(totalwidth);

        setHeight(height);
        setPrefHeight(height);
        setWidth(totalwidth.get());
        setPrefWidth(totalwidth.get());

        labelLayer = new TilePane();
        labelLayer.setPrefRows(1);
        labelLayer.setPrefWidth(totalwidth.get() * scale.get());
        markerLayer = new TilePane();
        labelLayer.setPrefRows(1);
        labelLayer.setPrefWidth(totalwidth.get() * scale.get());
        this.getChildren().addAll(labelLayer, markerLayer);

        this.scale.addListener((observableValue,oldValue,newValue) -> {
            recalculateUnits(scale.get(),totalwidth.get(), false,false);
        });

        this.totalwidth.addListener((observableValue,oldVal,newValue) -> {
            this.setWidth((double)newValue);
            recalculateUnits(scale.get(), totalwidth.get(), true,false);
        });

        recalculateUnits(scale.get(),totalwidth.get(), true,true);
    }

    public void recalculateUnits(double scale, double totalWidth, boolean hasWidthChanged,boolean isNotInitialize){
        boolean hasChanged = ChooseRightUnit(scale);
        int gapNumber = (int)totalWidth/unit.msRatio;
        if(hasWidthChanged || hasChanged || isNotInitialize) {
            labelLayer.getChildren().clear();
            markerLayer.getChildren().clear();
            fillLabelLayer(gapNumber);
            fillMarkerLayer(gapNumber);
        }

        double spacing = resizeTiles(scale);
        resizeTilePanes(scale, totalWidth, spacing);

    }

    private void resizeTilePanes(double scale, double totalWidth, double spacing) {
        double newWidth = scale * totalWidth + 10*spacing*scale;
        this.setPrefWidth(newWidth);
        this.setMaxWidth(newWidth);
        this.setWidth(newWidth);
        markerLayer.setPrefWidth(newWidth);
        labelLayer.setPrefWidth(newWidth);
        markerLayer.setMaxWidth(newWidth);
        labelLayer.setMaxWidth(newWidth);
    }

    private long resizeTiles(double scale) {
        double spacing = unit.msRatio * scale;
        long rounded_spacing = Math.round(spacing);
        labelLayer.prefTileWidthProperty().setValue(rounded_spacing);
        markerLayer.prefTileWidthProperty().setValue(rounded_spacing);
        return rounded_spacing;
    }

    private boolean ChooseRightUnit(double scale) {
        Unit lastUnit = Unit.HUNDRED_MILLISECONDS;
        Unit oldUnit = unit;
        for(Unit unit : Unit.values()) {
            if(scale < unit.threshhold){
                this.unit = lastUnit;
                break;
            }
            lastUnit = unit;
        }
        if(unit == oldUnit) return false;
        return true;
    }

    private void fillLabelLayer(int gapNumber) {
        long runningNumber = 0;
        for(int i = 0; i < gapNumber; i++) {
            runningNumber = i * unit.amount;
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(runningNumber);
            sBuilder.append(unit.label);
            Label label = new Label(sBuilder.toString());
            TilePane.setAlignment(label, Pos.CENTER_LEFT);
            labelLayer.getChildren().add(label);
        }
    }

    private void fillMarkerLayer(int gapNumber) {
        Line firstLine = new Line( 0, 0, 0, height);
        TilePane.setAlignment(firstLine, Pos.CENTER_LEFT);
        markerLayer.getChildren().add(firstLine);
        for(int i = 0; i < gapNumber - 1; i++) {
            Line line = new Line( 0, 0, 0, height);
            TilePane.setAlignment(line,Pos.CENTER_LEFT);
            markerLayer.getChildren().add(line);
        }
    }
}