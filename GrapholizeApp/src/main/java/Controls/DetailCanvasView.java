package Controls;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DetailCanvasView extends HBox {

    //Control which contains the detail canvas and
    public DetailCanvasView(){

    }

    //TODO: WIP
    private List<Checkbox> generateFilterList(){
        List<CheckBox> filters = new ArrayList<>();
        CheckBox option1 = new CheckBox();
        option1.setText("Filter option 1");
        //Maybe create own method for creating each checkbox and binding it to the canvas.
        //The Detail canvas could also be its own class, containing the strokes etc. Teh checkboxes can be bound
        //To the canvas.
        return null;
    }
}
