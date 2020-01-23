package New.Model.Entities;

public class Topic {
    private String topicName;
    private final String topicID;

    public Topic(String topic, String id){
        this.topicName = topic;
        this.topicID = id;
    }

    public String getTopicID() {
        return topicID;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
