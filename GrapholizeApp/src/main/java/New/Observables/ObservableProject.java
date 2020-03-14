package New.Observables;

import New.Execptions.TimeLineTagEmptyException;
import New.Execptions.TimeLineTagException;
import New.Execptions.TimelineTagNotUniqueException;
import New.Model.Entities.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.*;

public class ObservableProject {
    private static final String strokeSetID = "Stroke duration";
    private static final String durationTopicID = "Duration";
    private ObjectProperty<Project> innerProjectProperty;
    private List<ObservableSuperSet> observableSuperSets;

    public ObservableProject(Project inner){
        innerProjectProperty = new SimpleObjectProperty<>(inner);
        generateObservableTopicSets(inner);
        if(!inner.getTopicSetIDs().contains(strokeSetID)){
            generateStrokeTopicSetAndSegmentations(inner);
        }
    }

    //TODO: Potential memory leak?
    // Are certain topic set properties still referenced?
    private void generateObservableTopicSets(Project p){
        observableSuperSets = new LinkedList<>();
        for(String s : p.getTopicSetIDs()){
            ObservableSuperSet tag = new ObservableSuperSet(p.getTopicSet(s));
            observableSuperSets.add(tag);
        }
    }

    private void generateStrokeTopicSetAndSegmentations(Project p){
        SuperSet strokeSet = new SuperSet(
                strokeSetID,
                new SimpleColor(0,0,0,1),
                List.of(),
                "",
                strokeSetID
        );
        p.putTopicSet(strokeSet);
        observableSuperSets.add(new ObservableSuperSet(strokeSet));
        for(Participant participant : p.getAllParticipants()){
            for(Page page : participant.getPages()){
                for(Stroke s : page.getStrokes()){
                    Segment seg = new Segment(s.getTimeStart(), s.getTimeEnd());
                    page.getSegmentation(strokeSetID).add(seg);
                }
            }
        }
    }

    public List<SuperSet> getTopicSets(){
        return new LinkedList<>(innerProjectProperty.get().getAllTopicSets());
    }

    public Project getInner(){
        return innerProjectProperty.get();
    }

    public Set<String> getParticipantIDs(){
        return Collections.unmodifiableSet(innerProjectProperty.get().getParticipantIDs());
    }

    public Set<String> getTopicSetIDs(){
        return Collections.unmodifiableSet(innerProjectProperty.get().getTopicSetIDs());
    }

    public static String getStrokeSetID() {
        return strokeSetID;
    }

    public static String getDurationTopicID() {
        return durationTopicID;
    }

    public ObservableParticipant getParticipant(String key){
        return new ObservableParticipant(innerProjectProperty.get().getParticipant(key));
    }

    public ObjectProperty<Project> getProjectProperty(){
        return innerProjectProperty;
    }

    public ObservableSuperSet getObservableTopicSet(String tag){
        return new ObservableSuperSet(innerProjectProperty.get().getTopicSet(tag));
    }

    public void setInnerProject(Project p){

        generateObservableTopicSets(p);
        if(!p.getTopicSetIDs().contains(strokeSetID)){
            generateStrokeTopicSetAndSegmentations(p);
        }
        this.innerProjectProperty.set(p);

    }

    public void putTopicSet(SuperSet t) {
        innerProjectProperty.get().putTopicSet(t);
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

    public SuperSet removeTimeLineTag(String key){
        return innerProjectProperty.get().removeTopicSet(key);
    }

    public void cleanUp(){
        for(Participant participant : innerProjectProperty.get().getAllParticipants()){
            for(Page page : participant.getPages()){
                removeLooseSegmentations(page);
                removeLooseTopicIDsInSegments(page);
            }
        }
    }

    //TODO: Consider moving these to OPage;
    private void removeLooseSegmentations(Page page){
        Map<String, Set<Segment>> pageSegmentations = page.getSegmentationsMap();
        List<String> looseSetIDs = new LinkedList<>();
        for(String setID : pageSegmentations.keySet()){
            if(!getTopicSetIDs().contains(setID)){
                looseSetIDs.add(setID);
            }
        }
        looseSetIDs.forEach(s -> pageSegmentations.remove(s));
    }

    private void removeLooseTopicIDsInSegments(Page page){
        page.getSegmentationsMap().entrySet().forEach(entry ->{
            entry.getValue().parallelStream().forEach(segment -> {
                List<String>looseTopicIDs = new LinkedList<>();
                for(String topicID : segment.getAnnotationsMap().keySet()){
                    if(!getObservableTopicSet(entry.getKey()).getTopicIDs().contains(topicID)){
                        looseTopicIDs.add(topicID);
                    }
                }
                looseTopicIDs.forEach(s -> segment.removeAnnotation(s));
            });
        });
    }
}
