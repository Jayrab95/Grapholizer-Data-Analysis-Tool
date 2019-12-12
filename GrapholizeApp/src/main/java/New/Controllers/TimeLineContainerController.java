package New.Controllers;


import Execptions.TimeLineTagException;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLine.StrokeDurationTimeLinePane;
import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.Entities.Annotation;
import New.Model.Entities.SimpleColor;
import New.Model.Entities.Project;
import New.Model.Entities.TimeLineTag;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


import java.util.*;

public class TimeLineContainerController {

    private final static String TXT_TL_CREATION_TITLE = "Create a new timeline";
    private final static String TXT_TL_CREATION_HEADER = "Creation of a new timeline";
    private final static String TXT_TL_CREATION_TEXT = "Create a new timeline by entering a tag. The tag must be unique and cannot be empty.";
    private final static String TXT_TL_TIMELINETAG_LABEL = "Timeline tag:";
    private final static String TXT_TL_TAG_DEFAULTVAL = "New Timeline";
    private final static String TXT_TL_EDIT_TITLE = "Edit timeline";
    private final static String TXT_TL_EDIT_HEADER = "Editing a timeline";
    private final static String TXT_TL_EDIT_TEXT = "Change the name of the timeline";
    private final static String TXT_TL_CREATION_ERROR_TITLE = "Timeline creation error";
    private final static String TXT_TL_CREATION_ERROR_HEADER = "Error while creating timeline";
    private final static String TXT_TL_EDIT_ERROR_TITLE = "Timeline edit error";
    private final static String TXT_TL_EDIT_ERROR_HEADER = "Error while editing timeline";
    private final static String TXT_TL_ERROR_NAMEEMPTY = "The tag cannot be empty.";
    private final static String TXT_TL_ERROR_NAME_NOT_UNIQUE = "This tag already exists. Please choose another tag.";
    private final static String TXT_TL_ERROR_CANNOT_BE_DELETED = "This timeline cannot be deleted. Only custom timelines can be deleted.";
    private final static String TXT_TL_ERROR_CANNOT_BE_EDITED = "This timeline cannot be edited. Only custom timelines can be edited.";
    private final static String TXT_TL_DELETE_ERROR_TITLE = "Delete timeline error";
    private final static String TXT_TL_DELETE_ERROR_HEADER = "Error while deleting timeline";

    private Project project;
    private double totalWidth;
    private double timeLinesHeight;
    private DoubleProperty scale;
    ObservableActiveState state;
    private Map<TimeLineTag, TimeLinePane> timeLines;
    private TimeLinePane selectedTimeLine;


    public TimeLineContainerController(Project p, double initialScale, double timeLineHeight, ObservableActiveState state){
        this.project = p;
        scale = new SimpleDoubleProperty(initialScale);

        this.timeLinesHeight = timeLineHeight;
        this.state = state;
        this.totalWidth = state.getActivePage().getStrokes().get(state.getActivePage().getStrokes().size()-1).getTimeEnd();
        timeLines = new HashMap<>();
    }

    public Set<String> getTimeLineTags(){
        return Collections.unmodifiableSet(project.getProjectTagsMap().keySet());
    }

    //TODO: Generation process can be optimized by splitting itup into initial timeline creation on project load and timeline clearing/newfilling per page switch
    public List<TimeLinePane> generateTimeLinePanes(){
        List<TimeLinePane> res = new ArrayList<>();
        StrokeDurationTimeLinePane s = new StrokeDurationTimeLinePane(totalWidth, timeLinesHeight, scale, state, null);
        res.add(s);

        for(TimeLineTag tag : timeLines.keySet()){
            List<Annotation> timeLineElements = state.getActivePage().getTimeLines().get(tag.getTag());
            if(timeLineElements != null){
                CustomTimeLinePane commentTimeLinePane = new CustomTimeLinePane(
                        totalWidth,
                        timeLinesHeight,
                        scale,
                        state,
                        tag
                );
                res.add(commentTimeLinePane);
            }
        }
        return res;
    }

    public TimeLineTag createNewTimeLineTag(String newTimeLineName, SimpleColor newSimpleColor) throws TimeLineTagException {
        TimeLineTag newTimeLineTag = new TimeLineTag(newTimeLineName, newSimpleColor);
        state.getActiveProject().insertTimeLineTag(newTimeLineTag);
        return newTimeLineTag;
    }


    public TimeLinePane createNewTimeLinePane(TimeLineTag tag, Optional<List<AnnotationRectangle>> annotations){
        CustomTimeLinePane newTimeLine = new CustomTimeLinePane(totalWidth, timeLinesHeight, scale, state, tag);
        if(annotations.isPresent()){
            for(AnnotationRectangle ar : annotations.get()){
                Annotation annotation = new Annotation(ar.getTimeLineElement());
                newTimeLine.addAnnotation(annotation);
            }
        }
        return newTimeLine;
    }

    /*
    public void createCopyAnnotations(TimeLinePane tl, boolean combinedElement, String combinedAnnotationText){
        List<TimeLineElementRect> tles = selectedTimeLine.getChildren().stream()
                .map(node -> (TimeLineElementRect)node)
                .filter(elem -> elem.isSelected())
                .collect(Collectors.toList());
        boolean newAnnotationsColideWithExisting = tles.stream()
                .filter(element -> ((CommentTimeLinePane)tl).collidesWithOtherElements(element))
                .count() > 0;
        if(!newAnnotationsColideWithExisting){
            if(!combinedElement){
                for(TimeLineElementRect tle : tles){
                    tl.addTimeLineElement(new TimeLineElementRect(tl.getTimeLineColor(), tle, tle.getAnnotationText(), scale));
                }
            }
            else{
                TimeLineElementRect tle = new TimeLineElementRect(tles.get(0).getTimeStart(), tles.get(tles.size()-1).getTimeStop(), tl.getHeight(), tl.getTimeLineColor(), combinedAnnotationText, scale);
                tl.addTimeLineElement(tle);
                //TODO: What should happen if the newly created comment (or copies in general) overlaps with existing comments?
                //TODO: For the combined element, use the dialogue to figure out what the comment should be => Checkbox combined? If Checked, enble textbox for new comment
            }
        }
        else{
            DialogGenerator.simpleErrorDialog(
                    "Annotation copy error",
                    "Error while copying annotations to timeline " + tl.getTimeLineName(),
                    "One or more of the selected elements collides with other elements on the timeline."
            );
        }


    }

     */


}
