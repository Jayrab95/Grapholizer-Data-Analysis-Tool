package New.Model.Entities;

import New.util.Import.CompressedPage;
import New.util.Import.CompressedStroke;

import java.util.*;

public class Page {
    private transient final PageMetaData pageMetaData;
    private transient final List<Stroke> strokes;

    private Map<String, List<Segment>> timeLines;

    //TODO: Load page annotations and add them to map
    public Page (PageMetaData pageMetaData, List<Stroke> strokes){
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
    }

    public PageMetaData getPageMetaData() {
        return pageMetaData;
    }

    public List<Stroke> getStrokes() {
        return strokes;
    }

    public double getDuration(){
        return strokes.get(strokes.size()-1).getTimeEnd();
    }

    public Map<String, List<Segment>> getTimeLines(){return this.timeLines;}

    public List<Segment> getTimeLine(String key){
        if(!timeLines.keySet().contains(key)){
            timeLines.put(key, new LinkedList<>());
        }
        return timeLines.get(key);
    }

}
