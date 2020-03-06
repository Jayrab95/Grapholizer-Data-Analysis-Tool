package New.Observables;

import New.Model.Entities.Topic;
import New.Model.Entities.TopicSet;
import New.util.ColorConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ObservableTopicSet {
    private TopicSet inner;

    private StringProperty tagProperty;
    private ObjectProperty<Color> colorProperty;
    private StringProperty mainTopicIDProperty;
    private ObservableList<Topic> topicsObservableList;

    public ObservableTopicSet(TopicSet inner){
        this.inner = inner;
        this.tagProperty = new SimpleStringProperty(inner.getTag());
        this.tagProperty.addListener((observable, oldValue, newValue) -> inner.setTag(newValue));
        this.colorProperty = new SimpleObjectProperty<>(ColorConverter.convertModelColorToJavaFXColor(inner.getSimpleColor()));
        this.colorProperty.addListener((observable, oldValue, newValue) -> inner.setSimpleColor(
                ColorConverter.convertJavaFXColorToModelColor(newValue)));
        this.mainTopicIDProperty = new SimpleStringProperty(inner.getMainTopicID());
        this.mainTopicIDProperty.addListener((observable, oldValue, newValue) -> inner.setMainTopic(newValue));
        this.topicsObservableList = FXCollections.observableList(inner.getTopics().stream().collect(Collectors.toList()));
        this.topicsObservableList.addListener(new ListChangeListener<Topic>() {
            @Override
            public void onChanged(Change<? extends Topic> c) {
                while(c.next()){
                    inner.putAll((List<Topic>) c.getAddedSubList());
                    inner.removeAll((List<Topic>) c.getRemoved());
                }
            }
        });
    }

    public TopicSet getInner() {
        return inner;
    }

    public String getTopicSetID(){
        return inner.getTagID();
    }

    public String getTag() {
        return tagProperty.get();
    }

    public StringProperty getNameProperty() {
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

    public void setMainTopicID(String mainTopicID){
        mainTopicIDProperty.set(mainTopicID);
    }

    public ObservableList<Topic> getTopicsObservableList() {
        return topicsObservableList;
    }

    public void addTopic(Topic t){
        topicsObservableList.add(t);
        //inner.addTopic(t);
    }

    public void removeTopic(Topic t){
        topicsObservableList.remove(t);
        //inner.removeTopic(t);
    }

    public String generateTopicId(String topicName){
        return inner.generateTopicID(topicName);
    }

    public Collection<String> getTopicIDs(){
        return inner.getTopicIDs();
    }
}
