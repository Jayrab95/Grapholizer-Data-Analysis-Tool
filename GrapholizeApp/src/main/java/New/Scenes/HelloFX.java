package New.Scenes;

import New.util.datagenerator.CircleGenerator;
import New.util.datagenerator.DataGenerator;
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
        DataGenerator dataGenerator = new CircleGenerator(2.0d,2
                ,15,2
                ,2,15
                ,15,20,0, 0.5);
        System.out.println(dataGenerator.createJsonFromShape());
        stage.setTitle("Grapholizer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}