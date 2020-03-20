package New.Dialogues.DialogControls;

import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.Optional;

/**
 * The TopicTextControl is an HBox which contains a label for the topic name and a textbox
 * which the user can fill out for the annotation.
 * The class contains accessors to retrieve the annotation value and the individual graphical components.
 */
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

    public BooleanProperty textFieldDisableProperty(){
        return this.textField_topicText.disableProperty();
    }
}
