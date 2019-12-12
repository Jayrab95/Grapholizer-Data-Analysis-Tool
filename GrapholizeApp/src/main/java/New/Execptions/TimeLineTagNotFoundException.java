package New.Execptions;

public class TimeLineTagNotFoundException extends TimeLineTagException{
    public TimeLineTagNotFoundException(String tag){
        super(tag);
    }

    public String toString(){
        return "The timeline tag " + timeLineTag + " was not found in the Project tags.";
    }
}
