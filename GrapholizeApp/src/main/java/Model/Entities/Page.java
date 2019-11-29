package Model.Entities;

import util.Import.CompressedPage;
import util.Import.CompressedStroke;

import java.util.LinkedList;
import java.util.List;

public class Page {
    private final PageMetaData pageMetaData;
    private final List<Stroke> strokes;

    public Page (PageMetaData pageMetaData, List<Stroke> strokes){
        this.pageMetaData = pageMetaData;
        this.strokes = strokes;
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
    }

    public PageMetaData getPageMetaData() {
        return pageMetaData;
    }

    public List<Stroke> getStrokes() {
        return strokes;
    }
}
