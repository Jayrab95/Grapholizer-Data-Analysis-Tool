package New.Execptions;

public class SegmentationNameNotUniqueException extends SegmentationNameException {

    public SegmentationNameNotUniqueException(String tag) {
        super(tag);
    }

    public String toString(){
        return "The tag " + timeLineTag + " already exists.";
    }
}
