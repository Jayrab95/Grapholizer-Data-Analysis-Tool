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

    /**
     * Selects the dots within the given timeframe. this does NOT deselect other dots.
     * @param timeStart beginning of the timeframe
     * @param timeEnd end of the timeframe
     */
    public void selectDots(double timeStart, double timeEnd){
        selector.select(timeStart, timeEnd);
    }

    /**
     * Selects only dots within the timeframe and deselects all dots outside of the timeframe
     * @param timeStart beginning of the timeframe
     * @param timeEnd end of the timeframe
     */
    public void selectOnlyDotsWithinTimeFrame(double timeStart, double timeEnd){
        selector.selectOnlyTimeFrame(timeStart, timeEnd);
    }

    /**
     * Deselects all dots in timeframe
     * @param timeStart beginning of timeframe
     * @param timeEnd end of timeframe
     */
    public void deselectDots(double timeStart, double timeEnd){selector.deselect(timeStart, timeEnd);}
}
