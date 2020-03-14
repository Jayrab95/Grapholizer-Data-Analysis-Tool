package New.util.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public final class JavaFxUtil {
    /**
     * @param originClass class from which the method is called
     * @param fxmlPath path to the fxml file
     * @param stageTitle title of stage
     * @param width width of window
     * @param height height of window
     * @return returns the FXML loader which holds all the metadata for fxml file
     * @throws IOException in case the fxml can't be found or is unreadable
     */
    public static FXMLLoader openWindowReturnController(Class originClass, String fxmlPath, String stageTitle, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(originClass.getClassLoader().getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), width, height);
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stage.setScene(scene);
        stage.show();
        return loader;
    }

    /**
     *
     * @param windowTitle The title of the newly opened window
     * @param chooserTitle The title of the file chooser
     * @param isSaveDialog true if this it is indended to save the file
     * @param fileFilters A list of String representing the allowed file endings
     * @return Returns the chosen file or null if no file was picked
     */
    public static File openFileDialog(String windowTitle , String chooserTitle, boolean isSaveDialog, String ... fileFilters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(chooserTitle);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Supported Formats", fileFilters)
        );
        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        File sFile;
        if(isSaveDialog) sFile = fileChooser.showSaveDialog(stage);
        else sFile = fileChooser.showOpenDialog(stage);
        return sFile;
    }
}
