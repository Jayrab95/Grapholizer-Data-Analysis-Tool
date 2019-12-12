package Execptions;

public class TimeLineTagEmptyException extends TimeLineTagException {
    public TimeLineTagEmptyException(String tag) {
        super(tag);
    }
    public String toString(){
        return "The tag cannot be empty.";
    }
}
