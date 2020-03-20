package New.CustomControls.Containers;

import New.Filters.Filter;
import New.Observables.ObservableFilterCollection;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class FilterContainer extends HBox {
    public FilterContainer(ObservableFilterCollection filterCollection){
        ToggleGroup group = new ToggleGroup();
        for(Filter f : filterCollection.getFilters()){
            RadioButton b = new RadioButton(f.getFilterName());
            f.getFilterActiveProperty().bind(b.selectedProperty());
            b.setToggleGroup(group);
            getChildren().add(b);
        }
    }
}
