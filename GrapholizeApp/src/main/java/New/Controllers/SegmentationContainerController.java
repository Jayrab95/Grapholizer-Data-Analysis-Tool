package New.Controllers;


import New.Execptions.SegmentationNameException;
import New.Model.Entities.Topic;
import New.Model.Entities.SuperSet;
import New.Observables.ObservablePage;
import New.Observables.ObservableProject;
import New.Observables.ObservableSuperSet;
import New.util.ColorConverter;

import java.util.List;
import java.util.stream.Collectors;

public class SegmentationContainerController {

    private ObservableProject project;
    private ObservablePage page;

    public SegmentationContainerController(ObservableProject project, ObservablePage page){
        this.project = project;
        this.page = page;
    }
    public ObservablePage getPage(){return this.page;}


    public List<SuperSet> getTopicSets(){
        return project.getTopicSets();
    }



    //Assumption with create and edit: CheckIfTagIsValid has been called beforehand.
    //In the code, create and edit are only called as a result of a dialog, which calls the checkFunction.
    //This convention needs to be upheld.
    //Reason for this: It allows the reusage of the dialog window for both create and edit.

    /**
     * Adds the given super set to the project and returns an observable version of that super set.
     * @param superSet given superset to be added to project
     * @return ObservableSuperSet object wrapping the given super set
     */
    public ObservableSuperSet createNewTimeLineTag(SuperSet superSet){
        ObservableSuperSet oTag = new ObservableSuperSet(superSet);
        project.putTopicSet(superSet);
        return oTag;
    }

    /**
     * Adjusts the information of the old super set by comparing its info against the new super set.
     * @param oldSet unedited superSet
     * @param newSet edited superset
     */
    public void editSuperSet(ObservableSuperSet oldSet, SuperSet newSet){
        oldSet.setTag(newSet.getSuperSetName());
        oldSet.setMainTopicID(newSet.getMainTopicID());
        oldSet.setColor(ColorConverter.convertModelColorToJavaFXColor(newSet.getSimpleColor()));
        List<Topic> toRemove = oldSet.getTopicsObservableList().stream()
                .filter(topic -> newSet.getTopics().stream().noneMatch(t -> t.getTopicID().equals(topic.getTopicID())))
                .collect(Collectors.toList());
        List<Topic> toAdd = newSet.getTopics().stream()
                .filter(topic -> oldSet.getTopicsObservableList().stream().noneMatch(t -> t.getTopicID().equals(topic.getTopicID())))
                .collect(Collectors.toList());

        //Remove deleted topics and add new topics
        oldSet.getTopicsObservableList().removeAll(toRemove);
        oldSet.getTopicsObservableList().addAll(toAdd);
        //Change topic names of adjusted topics
        for(Topic t : newSet.getTopics()){
            for(Topic oldT : oldSet.getTopicsObservableList()){
                if(t.getTopicID().equals(oldT.getTopicID())){
                    if(!t.getTopicName().equals(oldT.getTopicName())){
                        oldT.setTopicName(t.getTopicName());
                    }
                    break;
                }
            }
        }
    }

    /**
     * Removes the super set with the given string ID
     * @param superSetID
     */
    public void removeSuperSet(String superSetID){
        project.removeSuperSet(superSetID);
    }

    /**
     * Checks if the given name is valid (is not blank and not used by another super set)
     * @param tag string name to be checked
     * @throws SegmentationNameException if tag is not valid
     */
    public void checkIfSuperSetNameIsValid(String tag) throws SegmentationNameException {
        project.checkIfTagIsValid(tag);
    }

}
