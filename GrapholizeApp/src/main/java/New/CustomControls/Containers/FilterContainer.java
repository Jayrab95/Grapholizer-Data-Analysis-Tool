package New.CustomControls.Containers;

import New.Filters.Filter;
import New.Observables.ObservableFilterCollection;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;

public class FilterContainer extends HBox {
    public FilterContainer(ObservableFilterCollection filterCollection){
        for(Filter f : filterCollection.getFilters()){
            CheckBox r = new CheckBox(f.getFilterName());
            f.getFilterActiveProperty().bind(r.selectedProperty());
            getChildren().add(r);
        }
    }
}
