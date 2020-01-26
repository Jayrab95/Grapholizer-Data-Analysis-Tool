package New.Model.Entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TopicSet {
    private final String tagID;
    private String tag;
    private SimpleColor simpleColor;
    private String mainTopicID;
    private final List<Topic> topics;

    public TopicSet(String tag, SimpleColor simpleColor, List<Topic> topics, String maintopicID, String tagID){
        this.tag = tag;
        this.simpleColor = simpleColor;
        this.topics = new LinkedList<>();
        this.topics.addAll(topics);
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

    public List<Topic> getTopics() {
        return topics;
    }

    public void addTopic(Topic t){
        this.topics.add(t);
    }

    public void removeTopic(Topic t){
        this.topics.remove(t);
    }


}
