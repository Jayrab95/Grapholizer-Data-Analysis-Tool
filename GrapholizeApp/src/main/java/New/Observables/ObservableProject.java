package New.Observables;

import New.Execptions.TimeLineTagEmptyException;
import New.Execptions.TimeLineTagException;
import New.Execptions.TimelineTagNotUniqueException;
import New.Interfaces.Observer.ProjectObserver;
import New.Model.Entities.Participant;
import New.Model.Entities.Project;
import New.Model.Entities.TopicSet;
import New.util.ColorConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.*;

public class ObservableProject {
    private ObjectProperty<Project> innerProjectProperty;
    private Project inner;
    private List<ProjectObserver> observers;
    private List<ObservableTopicSet> observableTopicSets;
    //private ObservableList<ObservableTimeLineTag> observableTimeLineTags;

    public ObservableProject(Project inner){
        innerProjectProperty = new SimpleObjectProperty<>(inner);
        this.inner = inner;
        //observableTimeLineTags = FXCollections.emptyObservableList();
        this.observers = new LinkedList<>();
        generateObservableTopicSets(inner);

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

    private void generateObservableTopicSets(Project p){
        observableTopicSets = new LinkedList<>();
        for(String s : p.getTopicSetIDs()){
            ObservableTopicSet tag = new ObservableTopicSet(p.getTopicSet(s));
            observableTopicSets.add(tag);
            //observableTimeLineTags.add(tag);
        }
    }

    public List<ObservableTopicSet> getObservableTopicSets(){
        return observableTopicSets;
    }

    public List<TopicSet> getTopicSets(){
        return new LinkedList<>(inner.getProjectTagsMap().values());
    }

    public Project getInner(){
        return inner;
    }

    public Set<String> getParticipantIDs(){
        return Collections.unmodifiableSet(inner.getParticipantIDs());
    }

    public Set<String> getTopicSetIDs(){
        return Collections.unmodifiableSet(inner.getTopicSetIDs());
    }

    public ObservableParticipant getParticipant(String key){
        return new ObservableParticipant(inner.getParticipant(key));
    }

    public ObjectProperty<Project> getProjectProperty(){
        return innerProjectProperty;
    }

    public ObservableTopicSet getTimeLineTag(String tag){
        return new ObservableTopicSet(inner.getTopicSet(tag));
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

    public void insertTimeLineTag(TopicSet t) {
        inner.getProjectTagsMap().put(t.getTagID(), t);
    }

    //TODO: What's better? Doing the valid check before inserting or assuming that the check has been called before inserting?
    //PRoblem with doing the check during creation and edit: the insertion has to happen as a result of a DialogOK.
    //Problem with assumption: The caller could technically use the edit function without checking first.
    public boolean editTimeLineTag(String oldTag, String newTagName, Color newColor){
        //TODO: This functionality is currently obsolved in the TimelineCOntrinaer controller.
        // either remove this or move the functionality here.
        TopicSet oldTopicSet = inner.getTopicSet(oldTag);
        if(oldTopicSet != null){
            if(!oldTag.equals(newTagName)){
                inner.getProjectTagsMap().remove(oldTag);
                oldTopicSet.setTag(newTagName);
                inner.getProjectTagsMap().put(newTagName, oldTopicSet);
            }
            oldTopicSet.setSimpleColor(ColorConverter.convertJavaFXColorToModelColor(newColor));
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
        if(getTopicSets().stream().anyMatch(topicSet -> topicSet.getTag().equals(tag))){
            throw new TimelineTagNotUniqueException(tag);
        }
    }

    private void timeLineTagIsNotEmpty(String tag) throws TimeLineTagException{
        if(tag.isBlank()){
            throw new TimeLineTagEmptyException(tag);
        }
    }

    public TopicSet removeTimeLineTag(String key){
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
