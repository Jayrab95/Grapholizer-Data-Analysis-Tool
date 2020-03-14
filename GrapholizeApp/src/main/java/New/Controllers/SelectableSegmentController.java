package New.Controllers;

import New.CustomControls.Annotation.SelectableSegmentRectangle;
import New.CustomControls.TimeLine.SelectableSegmentationPane;
import New.Interfaces.Selector;

public class SelectableSegmentController {

    private SelectableSegmentationPane parent;
    private Selector selector;

    public SelectableSegmentController(SelectableSegmentationPane parent, Selector s){
        this.parent = parent;
        this.selector = s;
    }

    /**
     * Selects the parent timeline if it is not currently selected and also deselects all elements if CTRL isn't held down.
     * @param CtrlHeld Is CTRL currently held down? (Multiselect)
     */
    public void selectTimeLine(boolean CtrlHeld, SelectableSegmentRectangle selected){
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

    public void selectOnlyDotsWithinTimeFrame(double timeStart, double timeEnd){
        selector.selectOnlyTimeFrame(timeStart, timeEnd);
    }

    public void deselectDots(double timeStart, double timeEnd){selector.deselect(timeStart, timeEnd);}
}
