package New.Execptions;

public class NoTopicsException extends TimeLineTagException {

    public NoTopicsException(String tag) {
        super(tag);
    }

    @Override
    public String toString() {
        return "A super set requires at least one topic.";
    }
}
