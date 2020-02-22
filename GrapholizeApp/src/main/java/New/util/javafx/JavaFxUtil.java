package New.util.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
}
