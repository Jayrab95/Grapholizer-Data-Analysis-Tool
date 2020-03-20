package New.Model.Entities;

import New.util.Import.model.CompressedPage;
import New.util.Import.model.CompressedStroke;

import java.util.*;

/**
 * A Page object represents a logical page that a participant has written on.
 * The Page object holds the strokes that the participant wrote and a map
 * containing the segmentations for this page.
 */
public class Page {
    private transient final PageMetaData pageMetaData;
    private transient final List<Stroke> strokes;
    //The Page id currently has no real bearing. When using the CompressedPage constructor, the id is "".
    private final String pageID;

    /**
     * The key is the superSetID and the value is the segmentation (A TreeSet)
     */
    private Map<String, Set<Segment>> segmentationsMap;

    public Page (PageMetaData pageMetaData, List<Stroke> strokes, String pageID){
        this.pageID = pageID;
        this.pageMetaData = pageMetaData;
        this.strokes = strokes;
        segmentationsMap = new HashMap<>();
    }

    public Page (CompressedPage cp) {
        long initialTimestamp = cp.Strokes.get(0).TimeStart;
        this.pageMetaData = new PageMetaData(0, cp.Number, cp.Book
                , 63.273216f, 88.582596f
                , initialTimestamp, initialTimestamp,0, cp.Strokes.size());
        strokes = new LinkedList<>();
        for (CompressedStroke cstroke : cp.Strokes) {
            strokes.add(new Stroke(cstroke, initialTimestamp));
        }
        segmentationsMap = new HashMap<>();
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
     * Note that this set orders the segments chronologically after their timeStart attribute.
     * @param key the super set ID which serves as the key of the segmentation.
     * @return An existing, sorted segmentation if the map contains a segmentation under the given key, or a new empty segmentation if no prior segmentation has been defined under the given key.
     */
    public Set<Segment> getSegmentation(String key){
        if(!segmentationsMap.keySet().contains(key)){
            segmentationsMap.put(key, new TreeSet<>());
        }
        return segmentationsMap.get(key);
    }

    /**
     * Adds the segment into the segmentation with the given setID. If the id is not present, a new
     * empty segmentation is created. The segment will be added into that segmentation.
     * @param setID the segmentation the segment is supposed to be put into.
     * @param segment the segment that should be added.
     */
    public void putSegmentIntoSegmentation(String setID, Segment segment){
        getSegmentation(setID).add(segment);
    }

    /**
     * Removes the given segment from the segmentation with the given setID.
     * If no segmentation with the given id is present,  new empty segmentation is created.
     * @param setID the segmentation the segment is supposed to be deleted from.
     * @param segment segment that should be deleted
     */
    public void removeSegmentFromSegmentation(String setID, Segment segment){
        getSegmentation(setID).remove(segment);
    }

}
