package New.Controllers;

import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLineContainer;
import New.CustomControls.Annotation.AnnotationRectangle;
import New.Model.Entities.Annotation;
import New.Model.ObservableModel.ObservablePage;
import New.Model.ObservableModel.ObservableTimeLine;
import New.Model.ObservableModel.ObservableTimeLineTag;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CustomTimeLineController {
    private ObservableTimeLineTag timeLineTag;
    private ObservablePage page;
    private ObservableTimeLine observableTimeLine;
    private TimeLineContainer parent;

    public CustomTimeLineController(ObservableTimeLineTag timeLineTag, ObservablePage page, TimeLineContainer parent) {
        this.timeLineTag = timeLineTag;
        this.page = page;
        this.parent = parent;
        this.observableTimeLine = parent.getSelectedTimeLine();
    }

    public void addAnnotation(Annotation a){
        page.addAnnotation(timeLineTag.getTag(), a);
    }

    public void editTimeLine(){
        parent.editTimeLine(timeLineTag);
    }

    public void removeTimeLine(CustomTimeLinePane timeLine){
        if(parent.removeTimeLine(timeLine)){
            page.removeObserver(timeLine);
            observableTimeLine.removeObserver(timeLine);
        }
    }

    public double[] getCombinedAnnotationBoundaries(){
        List<AnnotationRectangle> selectedAnnotations = observableTimeLine.getSelectedElements();
        Comparator<AnnotationRectangle> comp = Comparator.comparing(AnnotationRectangle::getTimeStart);
        Collections.sort(selectedAnnotations, comp);
        double timeStart = selectedAnnotations.get(0).getTimeStart();
        double timeStop = selectedAnnotations.get(selectedAnnotations.size() - 1).getTimeStop();
        return new double[]{timeStart, timeStop};
    }

    public List<AnnotationRectangle> getSelectedAnnotations(){
        return observableTimeLine.getSelectedElements();
    }

    public boolean collidesWithOtherElements(Optional<double[]> combined){
        if(combined.isPresent()){
            return collidesWithOtherElements(combined.get()[0], combined.get()[1]);
        }
        return collidesWithOtherElements();
    }

    public boolean collidesWithOtherElements(){
        return page.listCollidesWithOtherElements(timeLineTag.getTag(), observableTimeLine.getSelectedElements());
    }

    public boolean collidesWithOtherElements(double timeStart, double timeStop){
        return page.collidesWithOtherElements(timeLineTag.getTag(), timeStart, timeStop);
    }

    public void createNewTimeLine(){
        parent.createNewTimeLineOutOfSelectedElements();
    }
    public void createNewTimeLine(Optional<Annotation> combined){
        if(combined.isPresent()){
            parent.createNewTimeLineOutOfCombinedElement(combined.get());
        }
        else{
            parent.createNewTimeLineOutOfSelectedElements();
        }
    }



}
