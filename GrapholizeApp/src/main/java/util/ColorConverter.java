package util;

import javafx.scene.paint.Color;


public class ColorConverter {

    /**
     * Takes a Color from javafx.scene.paint.Color and converts it into a Model.Entities.Color object.
     * @param javafxColor The JavaFX Color object that needs to be converted
     * @return a Model.Entities.Color object that mirrors the JavaFX color.
     */
    public static Model.Entities.Color convertJavaFXColorToModelColor(javafx.scene.paint.Color javafxColor){
        return new Model.Entities.Color(javafxColor.getRed(), javafxColor.getGreen(), javafxColor.getBlue(), javafxColor.getOpacity());
    }

    /**
     * Takes a Color from Model.Entities.Color and converts it into a javafx.scene.paint.Color.
     * @param modelColor the Model.Entities.Color object that needs to be converted
     * @return a javafx.scene.paint.Color object that mirrors the Model.Entities color.
     */
    public static javafx.scene.paint.Color convertModelColorToJavaFXColor(Model.Entities.Color modelColor){
        return new javafx.scene.paint.Color(modelColor.getR(), modelColor.getG(), modelColor.getB(), modelColor.getO());
    }
}
