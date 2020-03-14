package New.Dialogues;

import New.CustomControls.Containers.TimeLineContainer;
import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import New.Model.Entities.TopicSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterForSegmentsDialog extends Dialog {

    private static String TXT_TITLE = "Filter for annotations";
    private static String TXT_HEADER = "Filter for annotations with specific text.";
    private static String TXT_CONTENT = "Select a topic set and a specific topic and enter a filter text. The new segmentation will contain an individual copy of segments that meet the filter requirements.";
    private ComboBox<TopicSet> comboBox_TopicSets;
    private GridPane grid;
    private ButtonType buttonType_ok;
    private ButtonType buttonType_Cancel;
    private List<FilterEntry> filterEntries;

    public FilterForSegmentsDialog(List<TopicSet> topicSets){

        setTitle(TXT_TITLE);
        setHeaderText(TXT_HEADER);
        setContentText(TXT_CONTENT);
        comboBox_TopicSets = new ComboBox<>();
        comboBox_TopicSets.getItems().addAll(filteredTopicSets(topicSets));
        grid = new GridPane();

        comboBox_TopicSets.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                initGridContent(newValue);
            }
        });
        comboBox_TopicSets.getSelectionModel().select(0);

        buttonType_ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        buttonType_Cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(buttonType_ok, buttonType_Cancel);
    }

    private List<TopicSet> filteredTopicSets(List<TopicSet> sets){
        return sets.stream().filter(set -> set.getTopics().size() > 0).collect(Collectors.toList());
    }

    public String getSelectedTopicSet(){
        return comboBox_TopicSets.getSelectionModel().getSelectedItem().toString();
    }

    public ButtonType getButtonType_ok() {
        return buttonType_ok;
    }

    public ButtonType getButtonType_Cancel() {
        return buttonType_Cancel;
    }

    public FilterDialogResult getDialogResult(){
        return new FilterDialogResult();
    }


    private void initGridContent(TopicSet ts){
        filterEntries = new LinkedList<>();
        Iterator<Topic> it = ts.getTopics().iterator();
        int row = 2;
        grid.getChildren().clear();
        grid.add(new Label("Topic set:"), 0,0);
        grid.add(comboBox_TopicSets, 1, 0);
        grid.add(new Label("Topic name"), 0, 1);
        grid.add(new Label("Import?"), 1, 1);
        grid.add(new Label("Filter?"), 2, 1);
        grid.add(new Label("Filter text"), 3, 1);

        while(it.hasNext()){
            Topic t = it.next();

            CheckBox checkbox_copy = new CheckBox();
            CheckBox checkBox_filter = new CheckBox();
            Label label_topicName = new Label(t.toString());
            TextField textField_filterText = new TextField();
            textField_filterText.disableProperty().bind(checkBox_filter.selectedProperty().not());

            grid.add(label_topicName, 0, row);
            grid.add(checkbox_copy, 1, row);
            grid.add(checkBox_filter, 2, row);
            grid.add(textField_filterText, 3, row);
            filterEntries.add(new FilterEntry(t, checkbox_copy.selectedProperty(), checkBox_filter.selectedProperty(), textField_filterText.textProperty()));
            row++;
        }
    }

    public class FilterEntry{
        private final Topic topic;
        private final BooleanProperty importProperty;
        private final BooleanProperty filterProperty;
        private final StringProperty filterTextProperty;
        FilterEntry(Topic t, BooleanProperty importProperty, BooleanProperty filterProperty, StringProperty filterTextProperty){
            this.topic = t;
            this.importProperty = new SimpleBooleanProperty(importProperty.get());
            this.importProperty.bind(importProperty);
            this.filterProperty = new SimpleBooleanProperty(filterProperty.get());
            this.filterProperty.bind(filterProperty);
            this.filterTextProperty = new SimpleStringProperty(filterTextProperty.get());
            this.filterTextProperty.bind(filterTextProperty);
        }

        public Topic getTopic() {
            return topic;
        }

        public String getFilterText() {
            return filterTextProperty.get();
        }

        public boolean isImport() {
            return importProperty.get();
        }

        public boolean isFilter() {
            return filterProperty.get();
        }
    }

    public class FilterDialogResult{
        TopicSet selectedSet;
        List<FilterEntry> entries;

        FilterDialogResult(){
            this.selectedSet = comboBox_TopicSets.getValue();
            this.entries=filterEntries;
        }
    }
}
