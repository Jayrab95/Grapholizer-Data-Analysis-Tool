package New.Observables;

import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import New.Model.Entities.TopicSet;
import New.util.ColorConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class ObservableTopicSet {
    private TopicSet inner;

    private StringProperty tagProperty;
    private ObjectProperty<Color> colorProperty;
    private StringProperty mainTopicIDProperty;
    private ObservableList<Topic> topicsObservableList;

    public ObservableTopicSet(TopicSet inner){
        this.inner = inner;
        tagProperty = new SimpleStringProperty(inner.getTag());
        tagProperty.addListener((observable, oldValue, newValue) -> inner.setTag(newValue));
        colorProperty = new SimpleObjectProperty<>(ColorConverter.convertModelColorToJavaFXColor(inner.getSimpleColor()));
        colorProperty.addListener((observable, oldValue, newValue) -> inner.setSimpleColor(
                ColorConverter.convertJavaFXColorToModelColor(newValue)));
        mainTopicIDProperty = new SimpleStringProperty(inner.getMainTopicID());
        mainTopicIDProperty.addListener((observable, oldValue, newValue) -> inner.setMainTopic(newValue));
        topicsObservableList = FXCollections.observableList(inner.getTopics());
    }

    public String getTag() {
        return tagProperty.get();
    }

    public StringProperty getTagProperty() {
        return tagProperty;
    }

    public void setTag(String tagProperty) {
        this.tagProperty.set(tagProperty);
    }

    public Color getColor() {
        return colorProperty.get();
    }

    public ObjectProperty<Color> getColorProperty() {
        return colorProperty;
    }

    public void setColor(Color color) {
        this.colorProperty.set(color);
    }

    public String getMainTopicID() {
        return mainTopicIDProperty.get();
    }

    public StringProperty getMainTopicIDProperty(){
        return mainTopicIDProperty;
    }

    public ObservableList<Topic> getTopicsObservableList() {
        return topicsObservableList;
    }

    public void setMainTopicID(String topicID){
        mainTopicIDProperty.set(topicID);
    }

    public void addTopic(Topic t){
        topicsObservableList.add(t);
        inner.addTopic(t);
    }

    public void removeTopic(Topic t){
        topicsObservableList.remove(t);
        inner.removeTopic(t);
    }

}
