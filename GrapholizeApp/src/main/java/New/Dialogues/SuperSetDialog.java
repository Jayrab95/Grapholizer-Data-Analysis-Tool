package New.Dialogues;

import New.Execptions.NoTopicsException;
import New.Model.Entities.SimpleColor;
import New.Model.Entities.Topic;
import New.Model.Entities.SuperSet;
import New.util.ColorConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.UUID;

public class SuperSetDialog extends Dialog<SuperSet> {

    private ButtonType buttonTypeOK;
    private ButtonType buttonTypeCancel;
    private StringProperty topicSetNameProperty;
    private ObservableList topics;
    private ObjectProperty<Color> topicSetColor;
    private ObjectProperty<Topic> mainTopic;


    public SuperSetDialog(String title, String header, String text, Optional<SuperSet> optional){
        setTitle(title);
        setHeaderText(header);
        setContentText(text);
        initializeDialog(optional);
    }

    private void initializeDialog(Optional<SuperSet> topicSetOptional){
        TextField textField_topicSetName = new TextField();
        ColorPicker colorPicker = new ColorPicker();
        if(topicSetOptional.isPresent()){
            textField_topicSetName.setText(topicSetOptional.get().getTag());
            colorPicker.setValue(ColorConverter.convertModelColorToJavaFXColor(topicSetOptional.get().getSimpleColor()));
        }
        else{
            textField_topicSetName.setText("New set");
            colorPicker.setValue(Color.CYAN);
        }
        this.topicSetNameProperty = new SimpleStringProperty();
        this.topicSetNameProperty.bind(textField_topicSetName.textProperty());
        this.topicSetColor = new SimpleObjectProperty<>();
        this.topicSetColor.bind(colorPicker.valueProperty());

        //https://coderanch.com/t/603701/java/Tableview-TextField-Javafx
        TableView<Topic> tableView_Topics = new TableView<>();
        tableView_Topics.setEditable(true);

        TableColumn topicColumn = new TableColumn("Topics");
        topicColumn.setCellValueFactory(new PropertyValueFactory<Topic, TextField>("topicName"));
        topicColumn.setMinWidth(tableView_Topics.getWidth());

        ComboBox<Topic> comboBox_mainTopic = new ComboBox<>();
        tableView_Topics.getItems().addListener(new ListChangeListener<Topic>() {
            @Override
            public void onChanged(Change<? extends Topic> c) {
                while(c.next()){
                    comboBox_mainTopic.getItems().addAll(c.getAddedSubList());
                    comboBox_mainTopic.getItems().removeAll(c.getRemoved());
                }
                if(comboBox_mainTopic.getSelectionModel().isEmpty()){
                    comboBox_mainTopic.getSelectionModel().select(0);
                }
            }
        });




        mainTopic = new SimpleObjectProperty<>();
        mainTopic.bind(comboBox_mainTopic.valueProperty());

        tableView_Topics.getColumns().add(topicColumn);

        if(topicSetOptional.isPresent()){
            tableView_Topics.getItems().addAll(topicSetOptional.get().getTopics());
            comboBox_mainTopic.setValue(topicSetOptional.get().getMainTopic());
        }
        this.topics = tableView_Topics.getItems();

        Label label = new Label("Topic name");
        TextField textField_topicName = new TextField();
        Button btn_AddTopic = new Button("Add Topic");
        btn_AddTopic.setOnAction(event -> {
            if(!(textField_topicName.getText().isBlank() || tableView_Topics.getItems().stream().anyMatch(t -> t.toString().equals(textField_topicName.getText())))){
                String topicID = String.format("%s_%s",textField_topicName.getText(), UUID.randomUUID().toString());
                /*
                int i = 0;
                boolean loop = true;
                while(loop){
                    //Stream requires a temp variable otherwise compiler complains. (Also avoids potential concurrency issues)
                    int finalI = i;
                    loop = tableView_Topics.getItems().stream().anyMatch(t -> t.getTopicID().equals(String.format("%s_%d", topicID, finalI)));
                    if(loop){i++;}
                }

                 */
                Topic t = new Topic(textField_topicName.getText(),topicID );
                tableView_Topics.getItems().add(t);
            }
        });

        Label label_AvailableTopics = new Label("Available topics");

        Button btn_deleteSelectedTopics = new Button("Delete selected topics");
        btn_deleteSelectedTopics.disableProperty().bind(tableView_Topics.getSelectionModel().selectedItemProperty().isNull());
        btn_deleteSelectedTopics.setOnAction(event -> {
            tableView_Topics.getItems().removeAll(tableView_Topics.getSelectionModel().getSelectedItems());
        });

        GridPane grid = new GridPane();
        grid.add(new Label("Topic set name"), 1, 1);
        grid.add(textField_topicSetName, 2, 1);
        grid.add(new Label("Timeline color:"), 1, 2);
        grid.add(colorPicker,2,2 );
        grid.add(label, 1,3);
        grid.add(textField_topicName, 2,3);
        grid.add(btn_AddTopic, 1,4);
        grid.add(label_AvailableTopics, 1, 5);
        grid.add(tableView_Topics, 1, 6);
        grid.add(btn_deleteSelectedTopics, 1, 7);
        grid.add(new Label("Main topic:"), 1, 8);
        grid.add(comboBox_mainTopic, 2, 8);

        buttonTypeOK = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(buttonTypeOK, buttonTypeCancel);
    }

    public ButtonType getButtonTypeOK() {
        return buttonTypeOK;
    }

    public ButtonType getButtonTypeCancel() {
        return buttonTypeCancel;
    }

    public Button okButton(){
        return (Button) getDialogPane().lookupButton(buttonTypeOK);
    }

    public String getTopicSetNameProperty() {
        return topicSetNameProperty.get();
    }

    public String getTopicSetText(){
        return topicSetNameProperty.get();
    }

    public boolean topicsDefined() throws NoTopicsException {
        if(topics.size() > 0){
            return true;
        }
        throw new NoTopicsException(getTopicSetText());
    }

    public SuperSet getTopicSet(){
        String id = String.format("%s_%s", topicSetNameProperty.get(), UUID.randomUUID().toString());
        String name = topicSetNameProperty.get();
        SimpleColor color = ColorConverter.convertJavaFXColorToModelColor(topicSetColor.get());
        SuperSet res = new SuperSet(name, color, topics, mainTopic.get().getTopicID(), id);
        return res;
    }


}
