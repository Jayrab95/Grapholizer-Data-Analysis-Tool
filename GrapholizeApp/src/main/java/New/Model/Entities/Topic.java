package New.Model.Entities;

/**
 * A topic denotes  a specific characteristic a user wants to examine per segment on a segmentation.
 * The Topic class consists of two simple strings, one being a name for the topic, which is mutable
 * and an immutable topicID string
 */
public class Topic {
    private String topicName;
    private final String topicID;

    public Topic(String topic, String id){
        this.topicName = topic;
        this.topicID = id;
    }

    /**
     * @return the topicID of this topic
     */
    public String getTopicID() {
        return topicID;
    }

    /**
     * @return The topic name (display name) of this topic
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     * Sets the topic name to the given string
     * @param newTopicName new name for topic
     */
    public void setTopicName(String newTopicName) {
        this.topicName = newTopicName;
    }

    /**
     * @return the name of this topic
     */
    @Override
    public String toString() {
        return topicName;
    }
}
