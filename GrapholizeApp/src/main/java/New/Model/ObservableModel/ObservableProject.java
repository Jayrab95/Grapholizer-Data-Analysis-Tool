package New.Model.ObservableModel;

import Execptions.TimeLineTagEmptyException;
import Execptions.TimeLineTagException;
import Execptions.TimelineTagNotUniqueException;
import New.Model.Entities.SimpleColor;
import New.Model.Entities.Participant;
import New.Model.Entities.Project;
import New.Model.Entities.TimeLineTag;

public class ObservableProject {
    private Project inner;

    public ObservableProject(Project inner){
        this.inner = inner;
    }

    public ObservableParticipant getParticipant(String key){
        return new ObservableParticipant(inner.getParticipant(key));
    }

    public ObservableTimeLineTag getTimeLineTag(String tag){
        return new ObservableTimeLineTag(inner.getTimeLineTag(tag));
    }

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

    public void insertTimeLineTag(TimeLineTag t) throws TimeLineTagException {
        checkIfTagIsValid(t.getTag());
        inner.getProjectTagsMap().put(t.getTag(), t);
    }

    public boolean editTimeLineTag(String oldTag, String newTagName, SimpleColor newSimpleColor) throws TimeLineTagException{
        TimeLineTag oldTimeLineTag = inner.getTimeLineTag(oldTag);
        if(oldTimeLineTag != null){
            if(!oldTag.equals(newTagName)){
                checkIfTagIsValid(newTagName);
                inner.getProjectTagsMap().remove(oldTag);
                oldTimeLineTag.setTag(newTagName);
                inner.getProjectTagsMap().put(newTagName, oldTimeLineTag);
            }
            oldTimeLineTag.setSimpleColor(newSimpleColor);
            return true;
        }
        return false;
    }

    private void checkIfTagIsValid(String key) throws TimeLineTagException {
        boolean exists = timeLineTagExists(key);
        boolean notEmpty = timeLineTagIsNotEmpty(key);
    }

    public TimeLineTag removeTimeLineTag(String key){
        return inner.getProjectTagsMap().remove(key);
    }

    public boolean timeLineTagExists(String tag) throws TimeLineTagException{
        if(inner.getProjectTagsMap().keySet().contains(tag)){
            throw new TimelineTagNotUniqueException(tag);
        }
        return true;
    }

    public boolean timeLineTagIsNotEmpty(String tag) throws TimeLineTagException{
        if(tag.isBlank()){
            throw new TimeLineTagEmptyException(tag);
        }
        return true;
    }
}
