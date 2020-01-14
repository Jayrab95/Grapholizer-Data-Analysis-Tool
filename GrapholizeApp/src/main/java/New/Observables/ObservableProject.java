package New.Observables;

import New.Execptions.TimeLineTagEmptyException;
import New.Execptions.TimeLineTagException;
import New.Execptions.TimelineTagNotUniqueException;
import New.Interfaces.Observer.ProjectObserver;
import New.Model.Entities.Participant;
import New.Model.Entities.Project;
import New.Model.Entities.TimeLineTag;
import New.util.ColorConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.*;

public class ObservableProject {
    private ObjectProperty<Project> innerProjectProperty;
    private Project inner;
    private List<ProjectObserver> observers;
    private List<ObservableTimeLineTag> timeLineTags;
    //private ObservableList<ObservableTimeLineTag> observableTimeLineTags;

    public ObservableProject(Project inner){
        innerProjectProperty = new SimpleObjectProperty<>(inner);
        this.inner = inner;
        //observableTimeLineTags = FXCollections.emptyObservableList();
        this.observers = new LinkedList<>();
        generateTimeLineTags(inner);

        /*
        //https://docs.oracle.com/javase/8/javafx/api/javafx/collections/ListChangeListener.Change.html
        //https://dzone.com/articles/javafx-collections-observablelist-and-observablema
        //http://what-when-how.com/javafx-2/understanding-observable-collections-collections-and-concurrency-javafx-2-part-2/
        observableTimeLineTags.addListener(new ListChangeListener<ObservableTimeLineTag>() {
            @Override
            public void onChanged(Change<? extends ObservableTimeLineTag> c) {
                while(c.next()){
                    for(ObservableTimeLineTag t : c.getAddedSubList()){

                    }
                    for(ObservableTimeLineTag t : c.getRemoved()){

                    }
                }
            }
        });

         */
    }

    private void generateTimeLineTags(Project p){
        timeLineTags = new LinkedList<>();
        for(String s : p.getTimeLineTagNames()){
            ObservableTimeLineTag tag = new ObservableTimeLineTag(p.getTimeLineTag(s));
            timeLineTags.add(tag);
            //observableTimeLineTags.add(tag);
        }
    }

    public List<ObservableTimeLineTag> getObservableTimeLineTags(){
        return timeLineTags;
    }

    public List<TimeLineTag> getTimeLineTags(){
        return new LinkedList<>(inner.getProjectTagsMap().values());
    }

    public Project getInner(){
        return inner;
    }

    public Set<String> getParticipantIDs(){
        return Collections.unmodifiableSet(inner.getParticipantIDs());
    }

    public Set<String> getTimeLineTagNames(){
        return Collections.unmodifiableSet(inner.getTimeLineTagNames());
    }

    public ObservableParticipant getParticipant(String key){
        return new ObservableParticipant(inner.getParticipant(key));
    }

    public ObjectProperty<Project> getProjectProperty(){
        return innerProjectProperty;
    }

    public ObservableTimeLineTag getTimeLineTag(String tag){
        return new ObservableTimeLineTag(inner.getTimeLineTag(tag));
    }

    public void setInnerProject(Project p){
        System.out.println("setInnerProject in ObservableProject called");
        this.inner = p;
        this.innerProjectProperty.set(p);

        notifyObservers();
    }

    //region Participant Modification and insertion
    //TODO: Are these functionalities even necessary?
    public boolean insertParticipant(Participant p){
        if(containsParticipantId(p.getID())){return false;}
        //TODO: are the ID's unmodifiable?
        inner.getParticipantsMap().put(p.getID(), p);
        return true;
    }

    public boolean editParticipant(Participant oldParticipant, String name){
        if(!oldParticipant.getID().equals(name)){
            if(containsParticipantId(name)){
                return false;
            }
            inner.getParticipantsMap().remove(oldParticipant.getID());
            oldParticipant.setID(name);
            inner.getParticipantsMap().put(name, oldParticipant);
        }
        return true;
    }

    public Participant removeParticipant(String key){
        return inner.getParticipantsMap().remove(key);
    }

    public boolean containsParticipantId(String id){
        return inner.getParticipantIDs().contains(id);
    }
    //endregion

    public void insertTimeLineTag(TimeLineTag t) {
        inner.getProjectTagsMap().put(t.getTag(), t);
    }

    //TODO: What's better? Doing the valid check before inserting or assuming that the check has been called before inserting?
    //PRoblem with doing the check during creation and edit: the insertion has to happen as a result of a DialogOK.
    //Problem with assumption: The caller could technically use the edit function without checking first.
    public boolean editTimeLineTag(String oldTag, String newTagName, Color newColor){
        TimeLineTag oldTimeLineTag = inner.getTimeLineTag(oldTag);
        if(oldTimeLineTag != null){
            if(!oldTag.equals(newTagName)){
                inner.getProjectTagsMap().remove(oldTag);
                oldTimeLineTag.setTag(newTagName);
                inner.getProjectTagsMap().put(newTagName, oldTimeLineTag);
            }
            oldTimeLineTag.setSimpleColor(ColorConverter.convertJavaFXColorToModelColor(newColor));
            return true;
        }
        //The tag was not found for soem reason. Throw exception?
        return false;
    }

    public void checkIfTagIsValid(String key) throws TimeLineTagException {
        timeLineTagExists(key);
        timeLineTagIsNotEmpty(key);
    }

    private void timeLineTagExists(String tag) throws TimeLineTagException{
        if(inner.getProjectTagsMap().keySet().contains(tag)){
            throw new TimelineTagNotUniqueException(tag);
        }
    }

    private void timeLineTagIsNotEmpty(String tag) throws TimeLineTagException{
        if(tag.isBlank()){
            throw new TimeLineTagEmptyException(tag);
        }
    }

    public TimeLineTag removeTimeLineTag(String key){
        return inner.getProjectTagsMap().remove(key);
    }

    public void addObserver(ProjectObserver obs){
        observers.add(obs);
    }
    public void removeObserver(ProjectObserver obs){
        observers.remove(obs);
    }
    public void notifyObservers(){
        for(ProjectObserver obs : observers){
            obs.update(this);
        }
    }
}
