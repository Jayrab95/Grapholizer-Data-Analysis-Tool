package New.Model.Entities;

import New.util.Import.model.CompressedPage;
import New.util.Import.model.CompressedStroke;

import java.util.*;

public class Page {
    private transient final PageMetaData pageMetaData;
    private transient final List<Stroke> strokes;
    private final String pageID;

    private Map<String, List<Segment>> timeLines;

    //TODO: Load page annotations and add them to map
    public Page (PageMetaData pageMetaData, List<Stroke> strokes, String pageID){
        this.pageID = pageID;
        this.pageMetaData = pageMetaData;
        this.strokes = strokes;
        timeLines = new HashMap<>();
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
        timeLines = new HashMap<>();
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

    public double getDuration(){
        return strokes.get(strokes.size()-1).getTimeEnd();
    }

    public Map<String, List<Segment>> getTimeLines(){return this.timeLines;}


    //TODO: This is a quick build around perhaps it would make sense to add in a stroke segmentation per default.
    public List getStrokeSegments(){
        List<Segment> res = new LinkedList();
        for(Stroke s : getStrokes()){
            res.add(new Segment(s.getTimeStart(), s.getTimeEnd()));
        }
        return res;
    }

    public List<Segment> getSegmentation(String key){
        //TODO: quick build around
        if(key.equals("Stroke duration")){return getStrokeSegments();}
        else if(!timeLines.keySet().contains(key)){
            timeLines.put(key, new LinkedList<>());
        }
        return timeLines.get(key);
    }

}
