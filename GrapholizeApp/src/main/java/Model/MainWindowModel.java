package Model;

import Model.Entities.Page;
import Model.Entities.Stroke;
import Observables.ObservableStroke;

import java.util.List;

public class MainWindowModel {
    //Move aspects of PageMetaData to BookMetAdata
    private PageMetaData pageMetaData;
    List<ObservableStroke> observableStrokes;

    public MainWindowModel(PageMetaData pmd, Stroke[] strokes){
        this.pageMetaData = pmd;
        

    }

}
