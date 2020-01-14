package New.Scenes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/views/MainScene.fxml"));
        Scene scene = new Scene(root, 300, 275); //TODO choose sensible window size on basis of desktop size

        stage.setTitle("Grapholizer Annotator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}