package New.Execptions;

public abstract class SegmentationNameException extends Exception {

    protected String timeLineTag;

    public SegmentationNameException(String tag){
        this.timeLineTag = tag;
    }

    public abstract String toString();
}
