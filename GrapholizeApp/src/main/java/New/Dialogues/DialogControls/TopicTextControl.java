package New.Dialogues.DialogControls;

import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class TopicTextControl extends HBox {
    private Label label_topic;
    private TextField textField_topicText;
    private Topic t;

    public TopicTextControl(Optional<Segment> s, Topic t){
        this.t = t;
        this.label_topic = new Label(t.toString());
        this.textField_topicText = new TextField();
        if (s.isPresent()) {
            textField_topicText.setText(s.get().getAnnotation(t.getTopicID()));
        } else {
            textField_topicText.setText("");
        }
        getChildren().addAll(label_topic, textField_topicText);
    }

    public Label getLabel_topic() {
        return label_topic;
    }

    public TextField getTextField_topicText() {
        return textField_topicText;
    }

    public Topic getTopic() {
        return t;
    }

    public String getTopicID(){
        return t.getTopicID();
    }

    public String getTextFieldText(){
        return textField_topicText.getText();
    }
}
