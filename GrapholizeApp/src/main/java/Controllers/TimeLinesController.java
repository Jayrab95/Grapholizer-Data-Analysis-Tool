package Controllers;

import Controls.Container.TimeLineContainer;
import Controls.Timeline.Pane.CommentTimeLinePane;
import Controls.Timeline.Pane.PressureTimeLinePane;
import Controls.Timeline.Pane.StrokeDurationTimeLinePane;
import Controls.Timeline.Pane.TimeLinePane;
import Controls.TimelineElement.TimeLineElementRect;
import Model.Entities.*;
import Model.ObservableActiveState;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import util.ColorConverter;
import util.DialogGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class TimeLinesController {

    private Project project;
    private double totalWidth;
    private double timeLinesHeight;
    private DoubleProperty scale;
    ObservableActiveState state;
    private Map<TimeLineTag, TimeLinePane> timeLines;
    private TimeLinePane selectedTimeLine;


    public TimeLinesController(Project p, double initialScale, double timeLineHeight, ObservableActiveState state){
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
        StrokeDurationTimeLinePane s = new StrokeDurationTimeLinePane("Stroke Duration Timeline", totalWidth, timeLinesHeight, scale, state.getObservableStrokes());
        res.add(s);
        //TODO: Remove after moving subtimelinegeneration to actual timelinepanes.
        res.add(new PressureTimeLinePane("pressure", totalWidth, timeLinesHeight, scale, javafx.scene.paint.Color.PINK, s, state.getObservableStrokes()));
        for(TimeLineTag tag : timeLines.keySet()){
            List<TimeLineElement> timeLineElements = state.getActivePage().getTimeLines().get(tag.getTag());
            if(timeLineElements != null){
                CommentTimeLinePane commentTimeLinePane = new CommentTimeLinePane(
                        tag.getTag(),
                        totalWidth,
                        timeLinesHeight,
                        scale,
                        ColorConverter.convertModelColorToJavaFXColor(tag.getColor()),
                        timeLineElements);
                res.add(commentTimeLinePane);
            }
        }
        return res;
    }


    public void createNewTimeLine(String newTimeLineName, Color newTimeLineColor){
        if(project.insertTimeLineTag(new TimeLineTag(newTimeLineName, newTimeLineColor))){

        }
    }

    public TimeLinePane createNewCustomTimeLine(String name, javafx.scene.paint.Color c, Optional<List<TimeLineElementRect>> tleList){
        //Creation of timeline
        TimeLinePane ctlp = new CommentTimeLinePane(name, totalWidth, 50, scale, c);
        if(tleList.isPresent()){
            for(TimeLineElementRect tle : tleList.get()){
                TimeLineElementRect ctle = new TimeLineElementRect(c, tle, tle.getAnnotationText(), scale);
                ctlp.addTimeLineElement(ctle);
            }
        }
        return ctlp;
    }

    //TODO: maybe move this to a "Dialog creator class"
    public boolean deleteConfirmation(String tlname){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Timeline");
        alert.setHeaderText("Delete timeline?");
        alert.setContentText("Are you sure you want to delete the timeline " + tlname + "? This action cannot be undone.");
        Optional<ButtonType> option = alert.showAndWait();
        if(option.isPresent() && option.get() == ButtonType.OK){
            return true;
        }
        return false;
    }

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



    public boolean editTimeLine(TimeLineTag timeLineTag, String newName, Color newColor){
        return project.editTimeLineTag(timeLineTag, newName, newColor);
    }



}
