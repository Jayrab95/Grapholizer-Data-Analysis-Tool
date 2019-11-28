package Model.Entities;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Timeline {
    private String timeLineName;
    private Color timeLineColor;
    private List<TimeLineElement> timeLineElements;

    public Timeline(String timeLineName, Color timeLineColor){
        this.timeLineName = timeLineName;
        this.timeLineColor = timeLineColor;
        this.timeLineElements = new LinkedList<>();
    }

    public String getTimeLineName() {
        return timeLineName;
    }

    /**
     * Sets the timeline name.
     * @param timeLineName
     */
    public void setTimeLineName(String timeLineName) {
        this.timeLineName = timeLineName;
    }

    public Color getTimeLineColor() {
        return timeLineColor;
    }

    /**
     * Sets the timeline color.
     * @param timeLineColor the new timeline color.
     */
    public void setTimeLineColor(Color timeLineColor) {
        this.timeLineColor = timeLineColor;
    }

    /**
     * Returns the list of TimeLineElements stored within this timeline. The list is of the type LinkedList.
     * Please only use this Getter for iterating over the elements. To add or remove elements, use the specific methods
     * "addTimeLineElementInOrder" or "removeTimeLineElement".
     * @return
     */
    public List<TimeLineElement> getTimeLineElements() {
        return timeLineElements;
    }

    /**
     * Adds a new TimeLineElement into the list at the right position. (The elements are ordered by their timeStart value.)
     * @param tle the new element to be added into the list.
     */
    public void addTimeLineElementInOrder(TimeLineElement tle){
        if(timeLineElements.size() == 0){
            timeLineElements.add(tle);
        }
        else{
            int i = 0;
            while(i < timeLineElements.size()){
                if(tle.getTimeStart() <= timeLineElements.get(i).getTimeStart()){
                    timeLineElements.add(i, tle);
                    break;
                }
                i++;
            }
            if (i >= timeLineElements.size()){
                timeLineElements.add(tle);
            }
        }
    }
    public boolean removeTimeLineElement(TimeLineElement tle){
        return timeLineElements.remove(tle);
    }

    /**
     * Updates both the timeLineName and the timeLineColor according to the passed values.
     * Use this method instead of the individual setters when updating a timeline via a timeline edit, so that
     * the listeners are only notified about a change once.
     * (Note: Could also possibly be used when adapting a command pattern into the application)
     * @param newTimeLineName
     * @param newColor
     */
    public void updateTimeLine(String newTimeLineName, Color newColor){
        timeLineName = newTimeLineName;
        timeLineColor = newColor;
    }


}
