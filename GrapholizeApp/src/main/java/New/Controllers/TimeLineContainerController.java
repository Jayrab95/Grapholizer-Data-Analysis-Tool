package New.Controllers;


import New.CustomControls.TimeLineContainer;
import New.Execptions.TimeLineTagException;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLine.StrokeDurationTimeLinePane;
import New.CustomControls.TimeLine.TimeLinePane;
import New.CustomControls.TimeLineElement.AnnotationRectangle;
import New.Model.Entities.Annotation;
import New.Model.Entities.SimpleColor;
import New.Model.Entities.Project;
import New.Model.Entities.TimeLineTag;
import New.Model.ObservableModel.ObservablePage;
import New.Model.ObservableModel.ObservableProject;
import New.Model.ObservableModel.ObservableTimeLineTag;
import New.util.ColorConverter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;


import java.util.*;
import java.util.stream.Collectors;

public class TimeLineContainerController {


    private ObservableProject project;
    private ObservablePage page;


    public TimeLineContainerController(ObservableProject project, ObservablePage page){
        this.project = project;
        this.page = page;
    }


    public ObservablePage getPage(){return this.page;}

    //Assumption with create and edit: CheckIfTagIsValid has been called beforehand.
    //In the code, create and edit are only called as a result of a dialog, which calls the checkFunction.
    //This convention needs to be upheld.
    //Reason for this: It allows the reusage of the dialog window for both create and edit.
    public ObservableTimeLineTag createNewTimeLineTag(String tag, Color c){
        TimeLineTag newTag = new TimeLineTag(tag, ColorConverter.convertJavaFXColorToModelColor(c));
        project.insertTimeLineTag(newTag);
        return new ObservableTimeLineTag(newTag);
    }

    public void editTimeLineTag(String oldTag, String newTag, Color newColor){
        project.editTimeLineTag(oldTag, newTag, newColor);
    }

    public void removeTimeLine(String tag){
        project.removeTimeLineTag(tag);
    }

    public void checkIfTagIsValid(String tag) throws TimeLineTagException{
        project.checkIfTagIsValid(tag);
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
