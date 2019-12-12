package Execptions;

public abstract class TimeLineTagException extends Exception {

    protected String timeLineTag;

    public TimeLineTagException(String tag){
        this.timeLineTag = tag;
    }

    public abstract String toString();
}
