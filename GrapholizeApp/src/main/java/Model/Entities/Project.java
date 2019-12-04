package Model.Entities;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Project {
    private Map<String, Participant> participants;
    private Map<String, TimeLineTag> projectTags;

    public Project(List<Participant> participants, List<TimeLineTag> tags){
        this.participants = participants.stream()
                .collect(Collectors.toMap(p -> p.getID(), p -> p));
        this.projectTags = tags.stream()
                .collect(Collectors.toMap(t -> t.getTag(), t -> t));
    }

    public Map<String, Participant> getParticipantsMap(){
        return Collections.unmodifiableMap(participants);
    }

    public Map<String, TimeLineTag> getProjectTagsMap(){
        return Collections.unmodifiableMap(projectTags);
    }

    public Participant getParticipant(String participantKey){
        return participants.get(participantKey);
    }

    public Set<String>getParticipantIDs(){
        return Collections.unmodifiableSet(participants.keySet());
    }

    public TimeLineTag getTimeLineTag(String tagKey){
        return projectTags.get(tagKey);
    }

    public boolean insertParticipant(Participant p){
        if(containsParticipantId(p.getID())){return false;}
        //TODO: are the ID's unmodifiable?
        participants.put(p.getID(), p);
        return true;
    }

    public boolean editParticipant(Participant oldParticipant, String name){
        if(!oldParticipant.getID().equals(name)){
            if(containsParticipantId(name)){
                return false;
            }
            participants.remove(oldParticipant.getID());
            oldParticipant.setID(name);
            participants.put(name, oldParticipant);
        }
        return true;
    }

    public Participant removeParticipant(String key){
        return participants.remove(key);
    }

    public boolean containsParticipantId(String id){
        return participants.keySet().contains(id);
    }

    public boolean insertTimeLineTag(TimeLineTag t){
        if(timeLineTagExists(t.getTag())){return false;}
        projectTags.put(t.getTag(), t);
        return true;
    }

    public boolean editTimeLineTag(TimeLineTag oldTag, String newTagName, Color newColor){
        if(!oldTag.getTag().equals(newTagName)){
            if(timeLineTagExists(newTagName)){
                return false;
            }
            projectTags.remove(oldTag.getTag());
            oldTag.setTag(newTagName);
            projectTags.put(newTagName, oldTag);
        }
        oldTag.setColor(newColor);
        return true;
    }

    public TimeLineTag removeTimeLineTag(String key){
        return projectTags.remove(key);
    }

    public boolean timeLineTagExists(String tag){
        return projectTags.keySet().contains(tag);
    }

}
