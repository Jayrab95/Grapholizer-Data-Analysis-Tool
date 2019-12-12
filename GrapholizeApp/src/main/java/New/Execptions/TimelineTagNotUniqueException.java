package New.Execptions;

public class TimelineTagNotUniqueException extends TimeLineTagException {

    public TimelineTagNotUniqueException(String tag) {
        super(tag);
    }

    public String toString(){
        return "The tag " + timeLineTag + " already exists.";
    }
}
