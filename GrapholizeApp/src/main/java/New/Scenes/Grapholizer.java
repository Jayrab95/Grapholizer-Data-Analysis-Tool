package New.Scenes;

import New.Controllers.MainSceneController;
import New.util.DialogGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Grapholizer extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader root = new FXMLLoader(this.getClass().getClassLoader().getResource("fxml/views/MainScene.fxml"));
        Scene scene = new Scene(root.load(), 300, 275); //TODO choose sensible size for initial window
        MainSceneController controller = root.getController();
        stage.setTitle("Grapholizer");
        initializeCleanUpSteps(stage, controller);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets up the behaviour that cleans-up all ressources used during the execution of the application
     * @param stage the main stage of this application
     * @param controller the controller class of the main fxml file
     */
    private void initializeCleanUpSteps(Stage stage, MainSceneController controller) {
        stage.setOnHidden( e -> {
                if(controller != null && controller._session != null) {
                    System.out.println("is not null");
                    try {
                        if (controller._session.getZ_Helper() != null)
                            controller._session.getZ_Helper().cleanUp(); //Delete the temporary files if they exist
                    } catch (IOException exp) {
                        DialogGenerator.simpleErrorDialog("Cleanup Error"
                                , "Error while cleaning temporary files"
                                , "while removing tempory data an Read Write error occured");
                    }
                }else {
                    System.out.println("is null");
                }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}