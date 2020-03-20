package New.Observables;

import New.Model.Entities.Topic;
import New.Model.Entities.SuperSet;
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
import java.util.stream.Collectors;

/**
 * The ObservableSuperSet is an Observable Model Object which wraps a superSet and exposes some of its
 * attributes as Object properties.
 * Any changes made to the properties are also made to the underlying wrapped object
 */
public class ObservableSuperSet {
    private SuperSet innerSuperSet;

    private StringProperty tagProperty;
    private ObjectProperty<Color> colorProperty;
    private StringProperty mainTopicIDProperty;
    private ObservableList<Topic> topicsObservableList;

    public ObservableSuperSet(SuperSet inner){
        this.innerSuperSet = inner;
        this.tagProperty = new SimpleStringProperty(inner.getSuperSetName());
        this.tagProperty.addListener((observable, oldValue, newValue) -> inner.setSuperSetName(newValue));
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

    public SuperSet getInner() {
        return innerSuperSet;
    }

    public String getTopicSetID(){
        return innerSuperSet.getSuperSetID();
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
    }

    public void removeTopic(Topic t){
        topicsObservableList.remove(t);
    }

    public String generateTopicId(String topicName){
        return innerSuperSet.generateTopicID(topicName);
    }

    public Collection<String> getTopicIDs(){
        return innerSuperSet.getTopicIDs();
    }
}
