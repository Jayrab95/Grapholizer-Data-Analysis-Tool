package New.CustomControls.Containers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class ContentSwitcher extends HBox {
    private StringProperty selectedParticipant;
    private IntegerProperty selectedPage;
    private ComboBox<String> participants;
    private ComboBox<Integer> pages;
}
