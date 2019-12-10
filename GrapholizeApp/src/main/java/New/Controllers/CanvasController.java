package New.Controllers;

import New.Model.ObservableModel.ObservableActiveState;
import Observables.ObservableStroke;

import java.util.List;

public class CanvasController {
    private ObservableActiveState state;

    public CanvasController(ObservableActiveState state){
        this.state = state;
    }

    public List<ObservableStroke> getStrokes(){
        return state.getObservableStrokes();
    }

}
