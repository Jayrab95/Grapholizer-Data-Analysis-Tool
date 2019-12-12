package New.CustomControls.TimeLine;

import New.Model.Entities.Annotation;
import New.Model.Entities.TimeLineTag;
import New.util.ColorConverter;
import javafx.beans.property.DoubleProperty;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.DialogGenerator;

import java.util.Optional;

public class CustomTimeLinePane extends TimeLinePane {

    private Light.Point anchor;
    private Rectangle selection;
    private double mouseDelta;
    private double[] dragBounds;

    public CustomTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableActiveState state, TimeLineTag tag) {
        super(width, height, scaleProp, state, tag);
        anchor = new Light.Point();
        selection = new Rectangle();
        dragBounds = new double[2];
    }

    public void addAnnotation(Annotation a){
        timeLineController.addAnnotation(a);
        getChildren().add(new AnnotationRectangle(
                ColorConverter.convertModelColorToJavaFXColor(timeLineController.getTimeLineTag().getSimpleColor()),
                scale,
                a,
                this));
    }


    //Source: https://coderanch.com/t/689100/java/rectangle-dragging-image
    private void handleTimelineMousePress(MouseEvent event){
        //TODO: Check if a comment was clicked.
        //If an element was clicked => set Selected Element. The element can now be dragged.
        //Reset selection

        dragBounds = getBounds(event.getX());

        //Prepare selection
        getChildren().add(selection);
        selection.setWidth(0);
        selection.setHeight(getHeight());

        anchor.setX(event.getX());
        anchor.setY(0);

        selection.setX(event.getX());
        selection.setY(0);

        selection.setFill(null); // transparent
        selection.setStroke(Color.BLACK); // border
        selection.getStrokeDashArray().add(10.0);

    }

    private void handleTimelineMouseDrag(MouseEvent event){
        if(event.getX() > dragBounds[0] && event.getX() < dragBounds[1]){
            selection.setWidth(Math.abs(event.getX() - anchor.getX()));
            selection.setX(Math.min(anchor.getX(), event.getX()));
        }
        else{ //Out of bounds (Collision with other element or timeline border)
            double temporaryOutOfBoundsPos = (event.getX() < dragBounds[0] ? dragBounds[0] : dragBounds[1]) + 0.1;
            selection.setWidth(Math.abs(temporaryOutOfBoundsPos - anchor.getX()));
            selection.setX(Math.min(anchor.getX(), temporaryOutOfBoundsPos));
        }
    }

    private void handleTimelineMouseRelease(MouseEvent e){
        //TODO: Check if width of selection is larger than 0-5
        System.out.printf("X: %.2f, Y: %.2f, Width: %.2f, Height: %.2f%n",
                selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight());
        if(selection.getWidth() > 0){
            //Call annotation creation dialogue
            Optional<String> s = DialogGenerator.simpleTextInputDialog(
                    "New annotation",
                    "Create new annotation",
                    "Enter a text for your annotation. (The annotation text can also be empty).",
                    "Annotation text");
            if(s.isPresent()){
                double timeStart = selection.getX() / scale.get();
                double timeStop = (selection.getX() + selection.getWidth()) / scale.get();
                Annotation newAnnotation = new Annotation(s.get(),timeStart, timeStop);
                addAnnotation(newAnnotation);
            }
            else{
                System.out.println("Annotation creation aborted.");
            }
        }

        //Reset SelectionRectangle
        selection.setWidth(0);
        selection.setHeight(0);
        getChildren().remove(selection);
    }
    //endregion
}
