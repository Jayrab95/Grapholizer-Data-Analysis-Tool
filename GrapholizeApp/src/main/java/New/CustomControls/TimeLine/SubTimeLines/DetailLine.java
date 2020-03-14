package New.CustomControls.TimeLine.SubTimeLines;

import javafx.beans.property.DoubleProperty;
import javafx.scene.shape.Line;

public class DetailLine extends Line {
    private final double startX, startY, endX, endY;

    public DetailLine(double startX, double startY, double endX, double endY, DoubleProperty scale) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        scale.addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                setCoordinates(newValue.doubleValue());
            }
        });
        setCoordinates(scale.get());
    }

    private void setCoordinates(double scale){
        this.setStartX(startX * scale);
        this.setStartY(startY);
        this.setEndX(endX * scale);
        this.setEndY(endY);
    }
}
