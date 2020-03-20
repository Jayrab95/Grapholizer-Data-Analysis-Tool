package New.Dialogues;

import New.Model.Entities.Topic;
import New.Model.Entities.SuperSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.*;

/**
 * The FilterSelectDialog allows the user to select all segments that contain specific annotations.
 * The user selects, from which topics they would like to filter annotations and then enters a filter text
 * for each of the selected topics.
 * The dialog, upon successful completion returns a Map<String, String>. This map contains the topicIDs of each
 * selected topic as a key and the entered filter text as its respective value.
 */
public class FilterSelectDialog extends Dialog<Map<String, String>> {
    private final List<TopicFilter> topicFilters;

    private final static String TITLE = "Filter and select";
    private final static String HEADER= "Filter and select segments";
    private final static String TEXT = "Search for segments on the %s segmentation which have specific annotations. The filter is only applied for active topics.";

    private ButtonType buttonTypeOK;
    private ButtonType buttonTypeCancel;

    public FilterSelectDialog(SuperSet set) {
        this.topicFilters = new LinkedList<>();

        setTitle(TITLE);
        setHeaderText(HEADER);
        setContentText(String.format(TEXT, set.getSuperSetName()));

        GridPane grid = new GridPane();

        Iterator<Topic> topics = set.getTopics().iterator();
        int row = 0;
        while(topics.hasNext()){
            Topic t = topics.next();
            CheckBox box = new CheckBox(t.getTopicName());

            TextField textField_topicFilter = new TextField();
            textField_topicFilter.disableProperty().bind(box.selectedProperty().not());

            this.topicFilters.add(new TopicFilter(t.getTopicID(), box.selectedProperty(), textField_topicFilter.textProperty()));

            grid.add(box, 0, row);
            grid.add(textField_topicFilter, 1, row);
            row++;
        }
        buttonTypeOK = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(buttonTypeOK, buttonTypeCancel);

        setResultConverter(b -> {
            if (b == buttonTypeOK) {
                return generateFilterMap();
            }
            return null;
        });
    }


    private Map<String, String> generateFilterMap(){
        Map<String, String> res = new HashMap<>();
        for(TopicFilter p : topicFilters){
            if(p.isSelected()){
                res.put(p.getTopicID(), p.getFilterText());
            }
        }
        return res;
    }

    private class TopicFilter {
        private final String TopicID;
        private final BooleanProperty selected;
        private final StringProperty filterText;
        private TopicFilter(String topicID, BooleanProperty bp, StringProperty sp){
            this.TopicID = topicID;
            this.selected = new SimpleBooleanProperty(bp.get());
            this.selected.bind(bp);
            this.filterText = new SimpleStringProperty(sp.get());
            this.filterText.bind(sp);
        }

        public boolean isSelected() {
            return selected.get();
        }

        public String getFilterText() {
            return filterText.get();
        }

        public String getTopicID() {
            return TopicID;
        }
    }
}
