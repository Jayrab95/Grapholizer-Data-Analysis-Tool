package New.Execptions;

public class TopicEmptyOrIdenticalException extends Exception {
    @Override
    public String toString() {
        return "The topic name can't be empty or identical to another topic.";
    }
}
