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

    public void insertOrEditParticipant(Participant p){
        //TODO: are the ID's unmodifiable?
        participants.put(p.getID(), p);
    }

    public void insertOrEditTimeLineTag(TimeLineTag t){
        projectTags.put(t.getTag(), t);
    }

}
