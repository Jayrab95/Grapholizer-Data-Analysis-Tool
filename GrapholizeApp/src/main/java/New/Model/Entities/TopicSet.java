package New.Model.Entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TopicSet {
    private String tag;
    private SimpleColor simpleColor;
    private String mainTopicID;
    private final List<Topic> topics;
    private final Map<String, List<Segment>> pageSegmentsMap;

    public TopicSet(String tag, SimpleColor simpleColor) {
        this.tag = tag;
        this.simpleColor = simpleColor;
        this.topics = new LinkedList<>();
        this.pageSegmentsMap = new HashMap<>();
    }

    public TopicSet(String tag, SimpleColor simpleColor, List<Topic> topics, Map<String, List<Segment>> pageSegmentsMap){
        this(tag, simpleColor);
        this.topics.addAll(topics);
        this.pageSegmentsMap.putAll(pageSegmentsMap);
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

    public Map<String, List<Segment>> getPageSegmentsMap() {
        return pageSegmentsMap;
    }

    public List<Segment> getPageSegmentsForPage(String pageID){
        if(!this.pageSegmentsMap.containsKey(pageID)){
            this.pageSegmentsMap.put(pageID, new LinkedList<>());
        }
        return this.pageSegmentsMap.get(pageID);
    }

    public void addSegment(String pageID, Segment a){
        this.getPageSegmentsForPage(pageID).add(a);
    }

    public void removeSegment(String pageID, Segment a){
        this.getPageSegmentsForPage(pageID).remove(a);
    }



}
