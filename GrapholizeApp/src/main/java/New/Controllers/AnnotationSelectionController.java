package New.Controllers;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.Annotation.SelectableAnnotationRectangle;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Interfaces.Selector;
import New.Observables.DotSelector;

public class AnnotationSelectionController {

    private SelectableTimeLinePane parent;
    private Selector selector;

    public AnnotationSelectionController(SelectableTimeLinePane parent){
        this.parent = parent;
        //this.selector = s;
    }

    /**
     * Selects the parent timeline if it is not currently selected and also deselects all elements if CTRL isn't held down.
     * @param CtrlHeld Is CTRL currently held down? (Multiselect)
     */
    public void selectTimeLine(boolean CtrlHeld, SelectableAnnotationRectangle selected){
        if(!parent.isSelected()){
            parent.setTimeLineSelected(true);
        }
        if(!CtrlHeld){
            parent.deselectAllElements(selected);
        }
    }

    public void selectDots(double timeStart, double timeEnd){
        selector.select(timeStart, timeEnd);
    }
}
