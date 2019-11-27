package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class DialogGenerator {

    /**
     * Opens a confirmation dialog which asks the user a question that can be answered with Yes (OK) or no (Cancel or close).
     * The dialog is an Alert and uses the AlertType CONFIRMATION.
     * @param title The title of the message box
     * @param header The header text of the message box
     * @param content The content text (question) of the message box
     * @return true if the user clicks on OK or false if the user clicks on cancel or closes the dialog.
     */
    public static boolean confirmationDialogue(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> option = alert.showAndWait();
        if(option.isPresent() && option.get() == ButtonType.OK){
            return true;
        }
        return false;
    }

    /**
     * Opens a TextInputDialog which asks the user to enter a text in a text box. The dialog returns an Optional<String>,
     * which either contains the text input by the user or is empty if the user canceled or closed the dialog.
     * This dialog does NOT validate the input.
     * Source for making TextInputDialogue: //https://code.makery.ch/blog/javafx-dialogs-official/
     * @param title The title of the dialog/message box.
     * @param header The header of the message box.
     * @param content The content text which describes what the user needs to input.
     * @param defaultValue The default value that should appear in the textbox
     * @return an Optional<String> is the user ends the dialog with "OK" or an empty Optional if the user canceled or closed the dialog.
     */
    public static Optional<String> simpleTextInputDialog(String title, String header, String content, String defaultValue){
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        return dialog.showAndWait();
    }

    /**
     * Opens an Error message box that informs the user of an error.
     * @param title Message box title
     * @param header Message box header
     * @param message The error message displayed to the user.
     */
    public static void simpleErrorDialog(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
