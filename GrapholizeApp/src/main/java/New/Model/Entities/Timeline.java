package New.Model.Entities;



import java.util.LinkedList;

public class Timeline {

    private String timeLineName;
    private SimpleColor timeLineSimpleColor;
    private LinkedList<Annotation> annotations;

    public Timeline(String timeLineName, SimpleColor timeLineSimpleColor){
        this.timeLineName = timeLineName;
        this.timeLineSimpleColor = timeLineSimpleColor;
        this.annotations = new LinkedList<>();
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

    public SimpleColor getTimeLineSimpleColor() {
        return timeLineSimpleColor;
    }

    /**
     * Sets the timeline color.
     * @param timeLineSimpleColor the new timeline color.
     */
    public void setTimeLineSimpleColor(SimpleColor timeLineSimpleColor) {
        this.timeLineSimpleColor = timeLineSimpleColor;
    }

    /**
     * Returns the list of TimeLineElements stored within this timeline. The list is of the type LinkedList.
     * Please only use this Getter for iterating over the elements. To add or remove elements, use the specific methods
     * "addTimeLineElementInOrder" or "removeTimeLineElement".
     * @return
     */
    public LinkedList<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * Adds a new TimeLineElement into the list at the right position. (The elements are ordered by their timeStart value.)
     * @param tle the new element to be added into the list.
     */
    public void addTimeLineElementInOrder(Annotation tle){
        if(annotations.size() == 0){
            annotations.add(tle);
        }
        else{
            int i = 0;
            while(i < annotations.size()){
                if(tle.getTimeStart() <= annotations.get(i).getTimeStart()){
                    annotations.add(i, tle);
                    break;
                }
                i++;
            }
            if (i >= annotations.size()){
                annotations.add(tle);
            }
        }
    }
    public boolean removeTimeLineElement(Annotation tle){
        return annotations.remove(tle);
    }

    /**
     * Updates both the timeLineName and the timeLineColor according to the passed values.
     * Use this method instead of the individual setters when updating a timeline via a timeline edit, so that
     * the listeners are only notified about a change once.
     * (Note: Could also possibly be used when adapting a command pattern into the application)
     * @param newTimeLineName
     * @param newSimpleColor
     */
    public void updateTimeLine(String newTimeLineName, SimpleColor newSimpleColor){
        timeLineName = newTimeLineName;
        timeLineSimpleColor = newSimpleColor;
    }


}
