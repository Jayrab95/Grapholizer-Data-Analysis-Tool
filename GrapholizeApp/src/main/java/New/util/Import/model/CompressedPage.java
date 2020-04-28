package New.util.Import.model;

import New.Model.Entities.Page;
import New.Model.Entities.Stroke;

import java.util.LinkedList;
import java.util.List;

public class CompressedPage {
    public int Section;

    public int Owner;

    public int Book;

    public int Number;

    public float X1;

    public float Y1;

    public float X2;

    public float Y2;

    public float MarginL;

    public float MarginR;

    public float MarginT;

    public float MarginB;

    public List<CompressedStroke> Strokes;

    public CompressedPage(Page page) {
        Section = 0;
        Owner = 0;
        Book = page.getPageMetaData().getNoteID();
        Number = page.getPageMetaData().getPageNumber();
        X1 = 0;
        Y1 = 0;
        X2 = 0;
        Y2 = 0;
        MarginL = 0;
        MarginR = 0;
        MarginT = 0;
        MarginB = 0;
        Strokes = new LinkedList<>();
        for (Stroke temp : page.getStrokes()) {
            Strokes.add(new CompressedStroke(temp));
        }
    }
}
