package Model.Entities;

import Model.PageMetaData;

public class Page {
    private final PageMetaData pageMetaData;
    private final Stroke[] strokes;

    public Page (PageMetaData pageMetaData, Stroke[] strokes){
        this.pageMetaData = pageMetaData;
        this.strokes = strokes;
    }

    public PageMetaData getPageMetaData() {
        return pageMetaData;
    }

    public Stroke[] getStrokes() {
        return strokes;
    }
}
