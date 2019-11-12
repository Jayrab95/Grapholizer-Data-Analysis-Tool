package Scenes;

import Model.Page;
import Model.Stroke;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import util.PageDataReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/MainScene.fxml"));

        Scene scene = new Scene(root, 300, 275);

        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }




}