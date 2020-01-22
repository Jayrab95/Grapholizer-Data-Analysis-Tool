package New.CustomControls.TimeLine;

import New.Controllers.CustomTimeLineController;
import New.CustomControls.Containers.TimeLineContainer;
import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.Annotation.MovableAnnotationRectangle;
import New.Execptions.NoTimeLineSelectedException;
import New.Interfaces.Observer.PageObserver;
import New.Model.Entities.Annotation;
import New.Observables.ObservableAnnotation;
import New.Observables.ObservableDot;
import New.Observables.ObservablePage;
import New.Observables.ObservableTimeLineTag;
import New.util.DialogGenerator;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Optional;

public class CustomTimeLinePane extends SelectableTimeLinePane implements PageObserver {

    public static final String TXT_COPYANNOTATION_TITLE = "Copy selected annotations";
    public static final String TXT_COPYANNOTATION_HEADER = "Copy selected annotations into timeline %s";
    public static final String TXT_COPYANNOTATION_TEXT =
            "The selected annotations will be copied into the timeline %s. \n"
            + "You may choose to combine the selected annotations into a single annotations. If so, you may enter a new Annotation text.";
    public static final String TXT_DOTANNOTATION_TITLE = "Create annotations out of selected dots";
    public static final String TXT_DOTANNOTATION_HEADER = "Create annotations out of selected dots into timeline %s";
    public static final String TXT_DOTANNOTATION_TEXT = "This dialog will create new annotations out of the currently selected dots on the canvas and put them into the timeline %s.\n"
            + "Per default, these annotations are separated according to the strokes that the selected dots belong to. "
            + "You may choose to combine the selected annotations into a single annotations. If so, you may enter a new Annotation text.";
    public static final String TXT_COPYANNOTATION_DEFAULTVAL = "New combined annotation";
    public static final String TXT_COLLIDEHANDLER_TITLE = "Annotation copy error";
    public static final String TXT_COLLIDEHANDLER_HEADER = "Annotation copy error";
    public static final String TXT_COLLIDEHANDLER_MSG =
            "One or more annotations that you are attempting to copy would collide with other existing annotations. \n"
            +"Would you like to create a new timeline for these new annotations?";

    private Light.Point anchor;
    private Rectangle selection;
    private double[] dragBounds;
    private ObservableTimeLineTag timeLineTag;
    private ObservablePage p;
    private CustomTimeLineController customTimeLineController;
    private ContextMenu contextMenu;

    public CustomTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTimeLineTag tag, ObservablePage p, TimeLineContainer parent) {
        super(width, height, scaleProp, tag.getTagProperty(), parent);
        this.timeLineTag = tag;
        customTimeLineController = new CustomTimeLineController(tag, p, parent);
        anchor = new Light.Point();
        selection = new Rectangle();
        dragBounds = new double[2];
        this.p = p;

        this.contextMenu = generateContextMenu();

        //this.setOnMouseClicked(event -> handleMouseClick(event));
        this.setOnMousePressed(event -> handleTimelineMousePress(event));
        this.setOnMouseDragged(event -> handleTimelineMouseDrag(event));
        this.setOnMouseReleased(event -> handleTimelineMouseRelease(event));

        //p.addObserver(this);
    }

    public CustomTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTimeLineTag tag, ObservablePage p, TimeLineContainer parent, Annotation[] annotations) {
        this(width, height, scaleProp, tag, p, parent);
        addAnnotations(annotations);
    }

    public CustomTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTimeLineTag tag, ObservablePage p, TimeLineContainer parent, List<AnnotationRectangle> annotations) {
        this(width, height, scaleProp, tag, p, parent);
        addAnnotations(annotations);
    }

    public CustomTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTimeLineTag tag, ObservablePage p, TimeLineContainer parent, Annotation a) {
        this(width, height, scaleProp, tag, p, parent);
        addAnnotation(a);
    }

    private void reloadTimeLine(List<ObservableAnnotation> annotations, double newWidth){
        setWidth(newWidth * scale.get());
        for(ObservableAnnotation a : annotations){
            getChildren().add(new MovableAnnotationRectangle(
                    timeLineTag.getColorProperty(),
                    scale,
                    a,
                    this,
                    p));
        }
    }

    public String getTimeLineName(){
        return timeLineName.get();
    }

    private void addAnnotation(Annotation a){
        customTimeLineController.addAnnotation(a);
        getChildren().add(new MovableAnnotationRectangle(
                timeLineTag.getColorProperty(),
                scale,
                new ObservableAnnotation(a),
                this,
                p));
    }

    private void addAnnotations(List<AnnotationRectangle> annotations){
        for(AnnotationRectangle a : annotations){
            Annotation newAnnotation = new Annotation(a.getText(), a.getTimeStart(), a.getTimeStop());
            addAnnotation(newAnnotation);
        }
    }

    private void addAnnotations(Annotation[] annotations){
        for(Annotation a : annotations){
            addAnnotation(a);
        }
    }

    private void removeAnnotation(AnnotationRectangle rect, ObservableAnnotation a){
        getChildren().remove(rect);
    }

    /**
     * Sets the bounds for the drag function.
     * @param xPosition Current xPosition on MousePress.
     */
    public double[] getBounds(double xPosition){
        //TODO: Move into controller
        double lowerBounds = 0;
        double upperBounds = getWidth();
        //The children list is not sorted and can also include handles of annotations
        for(Node n : getChildren()){
            if(n instanceof AnnotationRectangle){
                AnnotationRectangle rect = (AnnotationRectangle)n;
                double nTimeStart = rect.getX();
                double nTimeStop = rect.getX() + rect.getWidth();
                if(nTimeStop < xPosition && nTimeStop > lowerBounds) {
                    lowerBounds = nTimeStop;
                }
                if(nTimeStart > xPosition && nTimeStart < upperBounds) {
                    upperBounds = nTimeStart;
                }
            }

        }
        return new double[]{lowerBounds, upperBounds};
    }

    private ContextMenu generateContextMenu(){
        //TODO: Find better way to extend context menu functionality
        MenuItem item_createNewTimeLine = new MenuItem("Create new time line out of selected elements");
        item_createNewTimeLine.setOnAction(event -> handleContextCreateNewTimeLineClick());
        MenuItem item_copyAnnotations = new MenuItem("Copy selected annotations into this timeline");
        item_copyAnnotations.setOnAction(event -> handleContextCopyAnnotationsClick());
        MenuItem item_createAnnotationsOutOfDots = new MenuItem("Create annotations out of selected dots");
        item_createAnnotationsOutOfDots.setOnAction(event -> createAnnotationFromDotsDialogue());
        MenuItem item_editTimeLine = new MenuItem("Edit timeline tag");
        item_editTimeLine.setOnAction(event -> customTimeLineController.editTimeLine());
        MenuItem item_removeTimeLine = new MenuItem("Remove this timeline");
        item_removeTimeLine.setOnAction(event -> customTimeLineController.removeTimeLine(this));
        return new ContextMenu(item_createNewTimeLine, item_editTimeLine, item_copyAnnotations, item_createAnnotationsOutOfDots, item_removeTimeLine);
    }

    private void handleContextCreateNewTimeLineClick(){
        try{
            customTimeLineController.createNewTimeLine();
        }
        catch(NoTimeLineSelectedException ex){
            DialogGenerator.simpleErrorDialog("Creation Error","Error while creating timeline", ex.toString());
        }
    }

    private void handleContextCopyAnnotationsClick(){
        createCopyAnnotationDialogue();
    }

    //Source: https://coderanch.com/t/689100/java/rectangle-dragging-image
    private void handleTimelineMousePress(MouseEvent event){
        System.out.println("Mousepress in CTLP");
        if(event.getButton() == MouseButton.SECONDARY){
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
            event.consume();
        }
        else{
            //If an element was clicked => set Selected Element. The element can now be dragged.
            //Reset selection

            dragBounds = getBounds(event.getX());

            //Prepare selection
            getChildren().add(selection);
            selection.setWidth(0);
            selection.setHeight(getHeight());

            anchor.setX(event.getX());
            anchor.setY(0);

            selection.setX(event.getX());
            selection.setY(0);

            selection.setFill(null); // transparent
            selection.setStroke(Color.BLACK); // border
            selection.getStrokeDashArray().add(10.0);
        }
    }

    private void handleTimelineMouseDrag(MouseEvent event){
        if(event.getX() > dragBounds[0] && event.getX() < dragBounds[1]){
            selection.setWidth(Math.abs(event.getX() - anchor.getX()));
            selection.setX(Math.min(anchor.getX(), event.getX()));
        }
        else{ //Out of bounds (Collision with other element or timeline border)
            double temporaryOutOfBoundsPos = (event.getX() < dragBounds[0] ? dragBounds[0] : dragBounds[1]) + 0.1;
            selection.setWidth(Math.abs(temporaryOutOfBoundsPos - anchor.getX()));
            selection.setX(Math.min(anchor.getX(), temporaryOutOfBoundsPos));
        }
    }

    private void handleTimelineMouseRelease(MouseEvent e){
        if(selection.getWidth() > 0){
            //Call annotation creation dialogue
            Optional<String> s = DialogGenerator.simpleTextInputDialog(
                    "New annotation",
                    "Create new annotation",
                    "Enter a text for your annotation. (The annotation text can also be empty).",
                    "Annotation text");
            if(s.isPresent()){
                double timeStart = selection.getX() / scale.get();
                double timeStop = (selection.getX() + selection.getWidth()) / scale.get();
                Annotation newAnnotation = new Annotation(s.get(),timeStart, timeStop);
                addAnnotation(newAnnotation);
            }
            else{
                System.out.println("Annotation creation aborted.");
            }
        }

        //Reset SelectionRectangle
        selection.setWidth(0);
        selection.setHeight(0);
        getChildren().remove(selection);
    }



    public void createCopyAnnotationDialogue()  {
        //Dialog dialog = annotationCopyDialog(TXT_COPYANNOTATION_TITLE, String.format(TXT_COPYANNOTATION_HEADER, timeLineTag.getTag()), String.format(TXT_COPYANNOTATION_TEXT, timeLineTag.getTag()));

        Dialog dialog = new Dialog<>();
        dialog.setTitle(TXT_COPYANNOTATION_TITLE);
        dialog.setHeaderText(String.format(TXT_COPYANNOTATION_HEADER, timeLineTag.getTag()));
        dialog.setContentText(String.format(TXT_COPYANNOTATION_TEXT, timeLineTag.getTag()));

        CheckBox cbox_joinedAnnotation = new CheckBox("Combine selected elements into one annotation");

        Label label_AnnotationText = new Label("Annotation text: (Only applied if combine option is selected.)");
        TextField textField_annotationText = new TextField(TXT_COPYANNOTATION_DEFAULTVAL);
        textField_annotationText.disableProperty().bind(cbox_joinedAnnotation.selectedProperty().not());


        GridPane grid = new GridPane();
        grid.add(cbox_joinedAnnotation, 1, 1);
        grid.add(label_AnnotationText, 1, 2);
        grid.add(textField_annotationText, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        dialog.setResultConverter(b -> {
            if (b == buttonTypeOk) {
                //If the cbox is selected, a new optional containing the boundaries of the new combined annotation is created.
                Optional<double[]> boundaries = cbox_joinedAnnotation.isSelected() ?
                        Optional.of(customTimeLineController.getCombinedAnnotationBoundaries()) :
                        Optional.empty();
                //collidesWithOtherElements is executed with the boundaries if they are available or with all selected elements otherwise.
                //If a collision was detected, a dialog is prompted which asks the user if they want to create a new timeline.
                //should they decline, the entire procees is aborted.
                if(customTimeLineController.copiedAnnotationsCollideWithOtherAnnotations(boundaries) && DialogGenerator.confirmationDialogue(TXT_COLLIDEHANDLER_TITLE, TXT_COLLIDEHANDLER_HEADER, TXT_COLLIDEHANDLER_MSG)){
                    Optional<Annotation> annotation = cbox_joinedAnnotation.isSelected() ?
                            Optional.of(new Annotation(textField_annotationText.getText(), boundaries.get()[0], boundaries.get()[1])) :
                            Optional.empty();
                    try {
                        customTimeLineController.createNewTimeLine(annotation);
                    } catch (NoTimeLineSelectedException ex) {
                        DialogGenerator.simpleErrorDialog("Creation Error","Error while creating timeline", ex.toString());
                    }
                }
                else{
                    //If no collisions were detected, copy the annotations into this timeline.
                    if (cbox_joinedAnnotation.isSelected()) {
                        addAnnotation(new Annotation(textField_annotationText.getText(), boundaries.get()[0], boundaries.get()[1]));
                    }
                    else {
                        addAnnotations(customTimeLineController.getSelectedAnnotations());
                    }
                }
            }

            return null;
        });

        dialog.showAndWait();
    }

    public void createAnnotationFromDotsDialogue()  {
        //TODO: Extract reusable logic into separate method or even class.
        //Dialog dialog = annotationCopyDialog(TXT_COPYANNOTATION_TITLE, String.format(TXT_COPYANNOTATION_HEADER, timeLineTag.getTag()), String.format(TXT_COPYANNOTATION_TEXT, timeLineTag.getTag()));

        Dialog dialog = new Dialog<>();
        dialog.setTitle(TXT_DOTANNOTATION_TITLE);
        dialog.setHeaderText(String.format(TXT_DOTANNOTATION_HEADER, timeLineTag.getTag()));
        dialog.setContentText(String.format(TXT_DOTANNOTATION_TEXT, timeLineTag.getTag()));

        CheckBox cbox_joinedAnnotation = new CheckBox("Combine selected elements into one annotation");

        Label label_AnnotationText = new Label("Annotation text: (Only applied if combine option is selected.)");
        TextField textField_annotationText = new TextField(TXT_COPYANNOTATION_DEFAULTVAL);
        textField_annotationText.disableProperty().bind(cbox_joinedAnnotation.selectedProperty().not());


        GridPane grid = new GridPane();
        grid.add(cbox_joinedAnnotation, 1, 1);
        grid.add(label_AnnotationText, 1, 2);
        grid.add(textField_annotationText, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        dialog.setResultConverter(b -> {
            if (b == buttonTypeOk) {
                //If the cbox is selected, a new optional containing the boundaries of the new combined annotation is created.
                Optional<double[]> boundaries = cbox_joinedAnnotation.isSelected() ?
                        Optional.of(customTimeLineController.getCombinedDotAnnotationBoundaries()) :
                        Optional.empty();
                //collidesWithOtherElements is executed with the boundaries if they are available or with all selected elements otherwise.
                //If a collision was detected, a dialog is prompted which asks the user if they want to create a new timeline.
                //should they decline, the entire procees is aborted.
                if(customTimeLineController.dotSegmentsCollideWithOtherAnnotations(boundaries) && DialogGenerator.confirmationDialogue(TXT_COLLIDEHANDLER_TITLE, TXT_COLLIDEHANDLER_HEADER, TXT_COLLIDEHANDLER_MSG)){
                    Optional<Annotation> annotation = cbox_joinedAnnotation.isSelected() ?
                            Optional.of(new Annotation(textField_annotationText.getText(), boundaries.get()[0], boundaries.get()[1])) :
                            Optional.empty();
                    try {
                        customTimeLineController.createNewTimeLine(annotation);
                    } catch (NoTimeLineSelectedException ex) {
                        DialogGenerator.simpleErrorDialog("Creation Error","Error while creating timeline", ex.toString());
                    }
                }
                else{
                    //If no collisions were detected, copy the annotations into this timeline.
                    if (cbox_joinedAnnotation.isSelected()) {
                        addAnnotation(new Annotation(textField_annotationText.getText(), boundaries.get()[0], boundaries.get()[1]));
                    }
                    else {
                        for(List<ObservableDot> segment : p.getSelectedDotSegments()){
                            addAnnotation(new Annotation(
                                    "Generated Annotation",
                                    segment.get(0).getTimeStamp(),
                                    segment.get(segment.size()-1).getTimeStamp()));
                        }
                    }
                }
            }

            return null;
        });

        dialog.showAndWait();
    }

    @Override
    public void update(ObservablePage sender) {
        getChildren().clear();
        totalLength.set(sender.getDuration());
        reloadTimeLine(sender.getTimeLineAnnotations(timeLineTag.getTag()), sender.getDuration());
    }
}
