package New.Model.Entities;

import Execptions.TimeLineTagEmptyException;
import Execptions.TimeLineTagException;
import Execptions.TimelineTagNotUniqueException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

}
