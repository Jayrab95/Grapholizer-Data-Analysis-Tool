package New.Dialogues;

import New.Observables.ObservablePage;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class CustomTimeLineAnnotationDialog {
    private static final String TXT_COPYANNOTATION_DEFAULTVAL = "New combined annotation";

    private ObservablePage op;

    private Dialog annotationCopyDialog(String title, String header, String text){
        Dialog dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(text);

        CheckBox cbox_joinedAnnotation = new CheckBox("Combine selected elements into one annotation");

        Label label_AnnotationText = new Label("Annotation text: (Only applied if combine option is selected.)");
        TextField textField_annotationText = new TextField(TXT_COPYANNOTATION_DEFAULTVAL);
        textField_annotationText.disableProperty().bind(cbox_joinedAnnotation.selectedProperty().not());


        GridPane grid = new GridPane();
        grid.add(cbox_joinedAnnotation, 1, 1);
        grid.add(label_AnnotationText, 1, 2);
        grid.add(textField_annotationText, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);
        return dialog;
    }
}
