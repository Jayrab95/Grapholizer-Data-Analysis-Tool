package New.Model.Entities;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A superset represents a collection of topics that can be examined in the same segment.
 * the topics are stored in a map where the key is the topicID and the value is the topic object.
 */
public class SuperSet {
    private final String superSetID;
    private String superSetName;
    private SimpleColor simpleColor;
    private String mainTopicID;
    private final Map<String, Topic> topicsMap;

    public SuperSet(String superSetName, SimpleColor simpleColor, List<Topic> topicsMap, String maintopicID, String superSetID){
        this.superSetName = superSetName;
        this.simpleColor = simpleColor;
        this.topicsMap = new HashMap<>();
        for(Topic t : topicsMap){
            this.topicsMap.put(t.getTopicID(), t);
        }
        this.mainTopicID = maintopicID;
        this.superSetID = superSetID;
    }

    public String getSuperSetID() {
        return superSetID;
    }

    public String getSuperSetName() {
        return superSetName;
    }

    public void setSuperSetName(String superSetName) {
        this.superSetName = superSetName;
    }

    public SimpleColor getSimpleColor() {
        return simpleColor;
    }

    public void setSimpleColor(SimpleColor simpleColor) {
        this.simpleColor = simpleColor;
    }

    public String getMainTopicID() {
        return mainTopicID;
    }

    public void setMainTopic(String mainTopic) {
        this.mainTopicID = mainTopic;
    }

    /**
     * @return an unmodifiable Collection of topics
     */
    public Collection<Topic> getTopics() {
        return Collections.unmodifiableCollection(topicsMap.values()).stream().sorted(Comparator.comparing(Topic::getTopicName)).collect(Collectors.toList());
    }

    public void putAll(List<Topic> topics){
        for(Topic t : topics){
            this.topicsMap.put(t.getTopicID(), t);
        }
    }

    public void removeAll(List<Topic> topics){
        for(Topic t : topics){
            this.topicsMap.remove(t.getTopicID());
        }
    }

    public Topic getMainTopic(){
        return topicsMap.get(mainTopicID);
    }

    public Topic getTopic(String topicID){
        return topicsMap.get(topicID);
    }

    public Optional<Topic> getTopicWithName(String topicName){
        return topicsMap.values().stream().filter(topic -> topic.getTopicName().equals(topicName)).findFirst();
    }

    public void addTopic(Topic t){
        this.topicsMap.put(t.getTopicID(), t);
    }

    public void removeTopic(Topic t){
        this.topicsMap.remove(t.getTopicID());
    }

    /**
     * Generates and returns an ID string for the given topic.
     * This id has the format of [topicName]_[UUID].
     * @param topicName topic name out of which an ID is created
     * @return a topic id.
     */
    public String generateTopicID(String topicName){
        return String.format("%s_%s", topicName, UUID.randomUUID().toString());
    }

    public Collection<String> getTopicIDs(){
        return Collections.unmodifiableCollection(topicsMap.keySet());
    }

    /**
     * @return the name of this super set
     */
    @Override
    public String toString() {
        return superSetName;
    }
}
