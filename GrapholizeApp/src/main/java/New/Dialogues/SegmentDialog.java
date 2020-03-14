package New.Dialogues;

import New.Dialogues.DialogControls.TopicTextControl;
import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SegmentDialog extends Dialog<Segment> {

    private List<TopicTextControl> controls;
    private ButtonType buttonTypeOK;
    private ButtonType buttonTypeCancel;
    private BooleanProperty combined;
    private CheckBox cbox_Combined;

    public SegmentDialog(String title, String header, String dialogText, List<Topic> topics, Optional<Segment> s, boolean copy){
        controls = new LinkedList<>();
        combined = new SimpleBooleanProperty(false);
        cbox_Combined = new CheckBox("Combine segments");
        combined.bind(cbox_Combined.selectedProperty());

        setTitle(title);
        setHeaderText(header);
        setContentText(dialogText);


        GridPane grid = new GridPane();
        if(copy){
            grid.add(cbox_Combined, 0, 0);
        }

        for (int i = 0; i < topics.size(); i++) {
            TopicTextControl ttc = new TopicTextControl(s, topics.get(i));
            if(copy){
                ttc.textFieldDisableProperty().bind(cbox_Combined.selectedProperty().not());
            }

            grid.add(ttc.getLabel_topic(), 0, i+1);
            grid.add(ttc.getTextField_topicText(), 1, i+1);

            controls.add(ttc);
        }
        getDialogPane().setContent(grid);

        buttonTypeOK = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        getDialogPane().getButtonTypes().addAll(buttonTypeOK, buttonTypeCancel);
    }

    public List<TopicTextControl> getControls() {
        return controls;
    }

    public ButtonType getButtonTypeOK() {
        return buttonTypeOK;
    }

    public ButtonType getButtonTypeCancel() {
        return buttonTypeCancel;
    }

    public boolean isCombined() {
        return combined.get();
    }
}
