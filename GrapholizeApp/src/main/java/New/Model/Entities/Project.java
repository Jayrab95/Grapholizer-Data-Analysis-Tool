package New.Model.Entities;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Project {
    private Map<String, Participant> participants;
    private Map<String, SuperSet> projectTags;

    public Project(List<Participant> participants, List<SuperSet> superSets){
        this.participants = participants.stream()
                .collect(Collectors.toMap(p -> p.getID(), p -> p));
        this.projectTags = superSets.stream()
                .collect(Collectors.toMap(t -> t.getTagID(), t -> t));
    }

    public Map<String, Participant> getParticipantsMap(){
        return participants;
    }

    public Map<String, SuperSet> getProjectTagsMap(){
        return projectTags;
    }

    public Participant getParticipant(String participantKey){
        return participants.get(participantKey);
    }

    public Set<String>getParticipantIDs(){
        return participants.keySet();
    }

    public SuperSet getTopicSet(String topicSetID){
        return projectTags.get(topicSetID);
    }

    public Set<String> getTopicSetIDs(){return projectTags.keySet();}

    public Collection<Participant> getAllParticipants(){
        return participants.values();
    }

    public Collection<SuperSet> getAllTopicSets(){
        return projectTags.values();
    }

    public void putTopicSet(SuperSet ts){
        projectTags.put(ts.getTagID(),ts);
    }

    public void putAllTopicSets(Map<String, SuperSet> map){
        projectTags.putAll(map);
    }

    public SuperSet removeTopicSet(String id){
        return projectTags.remove(id);
    }

}
