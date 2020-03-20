package New.Model.Entities;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Project class holds the information of the input file by holding a reference to each participant
 * as well as a reference to each super set that was defined for the project.
 */
public class Project {
    private Map<String, Participant> participants;
    private Map<String, SuperSet> superSets;

    public Project(List<Participant> participants, List<SuperSet> superSets){
        this.participants = participants.stream()
                .collect(Collectors.toMap(p -> p.getID(), p -> p));
        this.superSets = superSets.stream()
                .collect(Collectors.toMap(t -> t.getSuperSetID(), t -> t));
    }

    public Map<String, Participant> getParticipantsMap(){
        return participants;
    }

    public Map<String, SuperSet> getProjectTagsMap(){
        return superSets;
    }

    public Participant getParticipant(String participantKey){
        return participants.get(participantKey);
    }

    public Set<String>getParticipantIDs(){
        return participants.keySet();
    }

    public SuperSet getTopicSet(String topicSetID){
        return superSets.get(topicSetID);
    }

    public Set<String> getTopicSetIDs(){return superSets.keySet();}

    public Collection<Participant> getAllParticipants(){
        return participants.values();
    }

    public Collection<SuperSet> getAllTopicSets(){
        return superSets.values();
    }

    public void putSuperSet(SuperSet ts){
        superSets.put(ts.getSuperSetID(),ts);
    }

    public void putAllTopicSets(Map<String, SuperSet> map){
        superSets.putAll(map);
    }

    public SuperSet removeTopicSet(String id){
        return superSets.remove(id);
    }

}
