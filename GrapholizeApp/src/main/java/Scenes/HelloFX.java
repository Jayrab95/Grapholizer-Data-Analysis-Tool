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
        try{loadThatShitBoy();}
        catch(Exception e){
            System.out.println("aaa");
        }
        System.out.println("Loaded page.");

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/MainScene.fxml"));

        Scene scene = new Scene(root, 300, 275);

        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();


        /*
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
         */
    }

    public static void main(String[] args) {
        launch();
    }

    //Relocate this method after testing.
    private static void loadThatShitBoy() throws Exception{
        String path = "src\\main\\resources\\data\\page.data";
        Page p = PageDataReader.ReadPage(path);
        System.out.println("Done");

    }


}