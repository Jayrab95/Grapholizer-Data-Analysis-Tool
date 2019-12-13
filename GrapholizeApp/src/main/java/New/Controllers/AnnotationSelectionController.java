package New.Controllers;

import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLine.SelectableTimeLinePane;

public class AnnotationSelectionController {
    private SelectableTimeLinePane parent;

    public AnnotationSelectionController(SelectableTimeLinePane parent){
        this.parent = parent;
    }

    /**
     * Selects the parent timeline if it is not currently selected and also deselects all elements if CTRL isn't held down.
     * @param CtrlHeld Is CTRL currently held down? (Multiselect)
     */
    public void selectTimeLine(boolean CtrlHeld){
        if(!parent.isSelected()){
            parent.setTimeLineSelected(true);
        }
        if(!CtrlHeld){
            parent.deselectAllElements();
        }
    }
}
