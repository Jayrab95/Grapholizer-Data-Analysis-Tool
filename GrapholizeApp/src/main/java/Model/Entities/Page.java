package Model.Entities;

import Model.PageMetaData;

import java.util.List;

public class Page {
    private final PageMetaData pageMetaData;
    private final List<Stroke> strokes;

    public Page (PageMetaData pageMetaData, List<Stroke> strokes){
        this.pageMetaData = pageMetaData;
        this.strokes = strokes;
    }

    public PageMetaData getPageMetaData() {
        return pageMetaData;
    }

    public List<Stroke> getStrokes() {
        return strokes;
    }
}
