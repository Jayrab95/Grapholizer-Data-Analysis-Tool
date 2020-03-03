package New.Model.Entities;

import New.util.Import.model.CompressedPage;
import New.util.Import.model.CompressedStroke;

import java.util.*;

public class Page {
    private transient final PageMetaData pageMetaData;
    private transient final List<Stroke> strokes;
    private final String pageID;

    private Map<String, Set<Segment>> segmentationsMap;

    //TODO: Load page annotations and add them to map
    public Page (PageMetaData pageMetaData, List<Stroke> strokes, String pageID){
        this.pageID = pageID;
        this.pageMetaData = pageMetaData;
        this.strokes = strokes;
        segmentationsMap = new HashMap<>();
    }

    public Page (CompressedPage cp) {
        //TODO: Lukas Width und Height müssen durch die Book Nummber bestimmt werden
        long initialTimestamp = cp.Strokes.get(0).TimeStart;
        this.pageMetaData = new PageMetaData(0, cp.Number, cp.Book
                , 63.273216f, 88.582596f
                , initialTimestamp, initialTimestamp,0, cp.Strokes.size());
        strokes = new LinkedList<>();
        for (CompressedStroke cstroke : cp.Strokes) {
            strokes.add(new Stroke(cstroke, initialTimestamp));
        }
        segmentationsMap = new HashMap<>();
        //TODO: get ID out of compressed Page!
        this.pageID = "";
    }

    public PageMetaData getPageMetaData() {
        return pageMetaData;
    }

    public List<Stroke> getStrokes() {
        return strokes;
    }

    public String getPageID() {
        return pageID;
    }

    /**
     * Returns the total duration of this page, defined as the timeEnd timestamp of the last stroke of this page.
     * @return the total duration of this page.
     */
    public double getPageDuration(){
        return strokes.get(strokes.size()-1).getTimeEnd();
    }

    public Map<String, Set<Segment>> getSegmentationsMap(){return this.segmentationsMap;}

    /**
     * Returns the segmentation which belongs to the given super set key. The returned segmentation is stored in a TreeSet<Segment>, meaning that
     * the segments are sorted by their start time.
     * If there is currently no entry for the given super set key, a new entry is created and stored in the segmentation map(new, empty TreeSet).
     * @param key the super set ID which serves as the key of the segmentation.
     * @return An existing segmentation if the map contains a segmentation under the given key, or a new empty segmentation if no prior segmentation has been defined under the given key.
     */
    public Set<Segment> getSegmentation(String key){
        if(!segmentationsMap.keySet().contains(key)){
            segmentationsMap.put(key, new TreeSet<>());
        }
        return segmentationsMap.get(key);
    }

    public void putSegmentIntoSegmentation(String setID, Segment s){
        getSegmentation(setID).add(s);
    }

    public void removeSegmentFromSegmentation(String setID, Segment s){
        getSegmentation(setID).remove(s);
    }

}
