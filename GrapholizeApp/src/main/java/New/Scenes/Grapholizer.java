package New.Scenes;

import New.Controllers.MainSceneController;
import New.util.datagenerator.CircleGenerator;
import New.util.datagenerator.DataGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Grapholizer extends Application {

    private MainSceneController controller;

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader root = new FXMLLoader(this.getClass().getClassLoader().getResource("fxml/views/MainScene.fxml"));
        this.controller = root.getController();
        Scene scene = new Scene(root.load(), 300, 275); //TODO choose sensible window size on basis of desktop size
        stage.setTitle("Grapholizer");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws IOException {
        if(controller._session.getZ_Helper() != null)
            controller._session.getZ_Helper().cleanUp();
    }
    public static void main(String[] args) {
        launch();
    }
}