package New.CustomControls.Annotation;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MovableAnnotationWrapper extends StackPane {
    public MovableAnnotationWrapper (MovableAnnotationRectangle rect){
        Text text = new Text(rect.annotationText.get());
        text.textProperty().bind(rect.annotationText);
        getChildren().addAll(rect, text);

    }
}
