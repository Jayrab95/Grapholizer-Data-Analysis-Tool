package New.util;

import New.Model.Entities.SimpleColor;
import javafx.scene.paint.Color;

/**
 * The ColorConverter offers two methods to convert JavaFX Color object into a SimpleColor
 * object and vice versa.
 */
public class ColorConverter {

    /**
     * Takes a Color from javafx.scene.paint.Color and converts it into a Model.Entities.Color object.
     * @param javafxColor The JavaFX Color object that needs to be converted
     * @return a Model.Entities.Color object that mirrors the JavaFX color.
     */
    public static SimpleColor convertJavaFXColorToModelColor(Color javafxColor){
        return new SimpleColor(javafxColor.getRed(), javafxColor.getGreen(), javafxColor.getBlue(), javafxColor.getOpacity());
    }

    /**
     * Takes a Color from Model.Entities.Color and converts it into a javafx.scene.paint.Color.
     * @param modelSimpleColor the Model.Entities.Color object that needs to be converted
     * @return a javafx.scene.paint.Color object that mirrors the Model.Entities color.
     */
    public static Color convertModelColorToJavaFXColor(SimpleColor modelSimpleColor){
        return new Color(modelSimpleColor.getR(), modelSimpleColor.getG(), modelSimpleColor.getB(), modelSimpleColor.getO());
    }
}
