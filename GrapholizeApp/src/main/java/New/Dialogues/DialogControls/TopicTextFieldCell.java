package New.Dialogues.DialogControls;

import New.Model.Entities.Topic;
import javafx.scene.control.cell.TextFieldListCell;

public class TopicTextFieldCell extends TextFieldListCell<Topic> {
    private Topic topic;

    public TopicTextFieldCell(Topic t){
        this.topic = t;
    }

    public Topic getTopic() {
        return topic;
    }
}
