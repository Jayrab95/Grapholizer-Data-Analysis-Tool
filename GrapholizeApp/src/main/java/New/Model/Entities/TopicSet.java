package New.Model.Entities;

import java.util.*;
import java.util.stream.Collectors;

public class TopicSet {
    private final String tagID;
    private String tag;
    private SimpleColor simpleColor;
    private String mainTopicID;
    private final Map<String, Topic> topicsMap;

    public TopicSet(String tag, SimpleColor simpleColor, List<Topic> topicsMap, String maintopicID, String tagID){
        this.tag = tag;
        this.simpleColor = simpleColor;
        this.topicsMap = new HashMap<>();
        for(Topic t : topicsMap){
            this.topicsMap.put(t.getTopicID(), t);
        }
        this.mainTopicID = maintopicID;
        this.tagID = tagID;
    }

    public String getTagID() {
        return tagID;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public void addTopic(Topic t){
        this.topicsMap.put(t.getTopicID(), t);
    }

    public void removeTopic(Topic t){
        this.topicsMap.remove(t.getTopicID());
    }

    @Override
    public String toString() {
        return tag;
    }
}
