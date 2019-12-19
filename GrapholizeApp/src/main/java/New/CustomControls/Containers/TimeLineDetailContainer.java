package New.CustomControls.Containers;

import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Model.ObservableModel.ObservablePage;
import javafx.scene.layout.VBox;

public class TimeLineDetailContainer extends VBox {

    private SelectableTimeLinePane inspectedTimeLine;
    ObservablePage activePage;

    public TimeLineDetailContainer(SelectableTimeLinePane inspectedTimeLine, ObservablePage activePage){
        this.inspectedTimeLine = inspectedTimeLine;
        this.activePage = activePage;

    }

    private void generateDetailContainer(){
        getChildren().add(inspectedTimeLine);

    }
}
