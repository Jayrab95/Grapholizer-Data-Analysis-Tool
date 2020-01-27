package New.Model.Entities;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Project {
    private Map<String, Participant> participants;
    private Map<String, TopicSet> projectTags;

    public Project(List<Participant> participants, List<TopicSet> topicSets){
        this.participants = participants.stream()
                .collect(Collectors.toMap(p -> p.getID(), p -> p));
        this.projectTags = topicSets.stream()
                .collect(Collectors.toMap(t -> t.getTagID(), t -> t));
    }

    public Map<String, Participant> getParticipantsMap(){
        return participants;
    }

    public Map<String, TopicSet> getProjectTagsMap(){
        return projectTags;
    }

    public Participant getParticipant(String participantKey){
        return participants.get(participantKey);
    }

    public Set<String>getParticipantIDs(){
        return participants.keySet();
    }

    public TopicSet getTopicSet(String topicSetID){
        return projectTags.get(topicSetID);
    }

    public Set<String> getTopicSetIDs(){return projectTags.keySet();}


}
