package New.Execptions;

public class SegmentationNameNotFoundException extends SegmentationNameException {
    public SegmentationNameNotFoundException(String tag){
        super(tag);
    }

    public String toString(){
        return "The timeline tag " + timeLineTag + " was not found in the Project tags.";
    }
}
