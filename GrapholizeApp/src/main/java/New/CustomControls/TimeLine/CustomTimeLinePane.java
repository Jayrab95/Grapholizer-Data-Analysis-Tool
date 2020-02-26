package New.CustomControls.TimeLine;

import New.Controllers.CustomTimeLineController;
import New.CustomControls.Containers.TimeLineContainer;
import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.Annotation.MovableAnnotationRectangle;
import New.Dialogues.DialogControls.TopicTextControl;
import New.Dialogues.FilterSelectDialog;
import New.Dialogues.SegmentDialog;
import New.Execptions.NoTimeLineSelectedException;
import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import New.Observables.ObservableSegment;
import New.Observables.ObservableDot;
import New.Observables.ObservablePage;
import New.Observables.ObservableTopicSet;
import New.util.DialogGenerator;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomTimeLinePane extends SelectableTimeLinePane {

    public static final String TXT_COPYANNOTATION_TITLE = "Copy selected annotations";
    public static final String TXT_COPYANNOTATION_HEADER = "Copy selected annotations into this segmentation";
    public static final String TXT_COPYANNOTATION_TEXT =
            "The selected annotations will be copied into this segmentation. \n"
            + "You may choose to combine the selected annotations into a single annotations. If so, you may enter a new Annotation text.";
    public static final String TXT_DOTANNOTATION_TITLE = "Create annotations out of selected dots";
    public static final String TXT_DOTANNOTATION_HEADER = "Create annotations out of selected dots";
    public static final String TXT_DOTANNOTATION_TEXT = "This dialog will create new annotations out of the currently selected dots on the canvas and put them into this segmentation.\n"
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
    private ObservableTopicSet observableTopicSet;
    private ObservablePage p;
    private CustomTimeLineController customTimeLineController;
    private ContextMenu contextMenu;

    public CustomTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTopicSet tag, ObservablePage p, TimeLineContainer parent) {
        super(width, height, scaleProp, tag.getTagProperty(), parent, tag.getTopicSetID());
        this.observableTopicSet = tag;
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

    public CustomTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTopicSet tag, ObservablePage p, TimeLineContainer parent, Segment[] segments) {
        this(width, height, scaleProp, tag, p, parent);
        addAnnotations(segments);
    }

    public CustomTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTopicSet tag, ObservablePage p, TimeLineContainer parent, List<AnnotationRectangle> annotations) {
        this(width, height, scaleProp, tag, p, parent);
        addAnnotations(annotations);
    }

    public CustomTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTopicSet tag, ObservablePage p, TimeLineContainer parent, Segment a) {
        this(width, height, scaleProp, tag, p, parent);
        addAnnotation(a);
    }

    public String getTimeLineName(){
        return timeLineName.get();
    }

    public ObservableTopicSet getObservableTopicSet() {
        return observableTopicSet;
    }

    private void addAnnotation(Segment a){
        customTimeLineController.addAnnotation(a);
        MovableAnnotationRectangle mov = new MovableAnnotationRectangle(
                observableTopicSet.getColorProperty(),
                scale,
                new ObservableSegment(a, observableTopicSet),
                this,
                p,
                observableTopicSet);
        getChildren().addAll(mov, mov.getDisplayedText());
    }

    private void addAnnotations(List<AnnotationRectangle> annotations){
        for(AnnotationRectangle a : annotations){
            Segment newSegment = new Segment(a.getTimeStart(), a.getTimeStop());
            addAnnotation(newSegment);
        }
    }

    private void addAnnotations(Segment[] segments){
        for(Segment a : segments){
            addAnnotation(a);
        }
    }

    private void removeAnnotation(AnnotationRectangle rect, ObservableSegment a){
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
        MenuItem item_filterSelect = new MenuItem("Filter select");
        item_filterSelect.setOnAction(event -> handleFilterSelectClick());
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
        return new ContextMenu(item_filterSelect, item_createNewTimeLine, item_editTimeLine, item_copyAnnotations, item_createAnnotationsOutOfDots, item_removeTimeLine);
    }

    private void handleFilterSelectClick(){
        Optional<Map<String, String>> o =  new FilterSelectDialog(observableTopicSet.getInner()).showAndWait();
        if(o.isPresent()){
            List<MovableAnnotationRectangle> children = getChildren().stream()
                    .filter(node -> node instanceof MovableAnnotationRectangle)
                    .map(node -> (MovableAnnotationRectangle) node)
                    .collect(Collectors.toList());
            customTimeLineController.filterSelect(this, o.get(), children);
        }
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
        if(event.getButton().equals(MouseButton.PRIMARY)){
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
    }

    private void handleTimelineMouseRelease(MouseEvent e){
        if(e.getButton().equals(MouseButton.PRIMARY)){
            if(selection.getWidth() > 0){
                //Call annotation creation dialogue
                double timeStart = selection.getX() / scale.get();
                double timeStop = (selection.getX() + selection.getWidth()) / scale.get();
                Segment s = new Segment(timeStart, timeStop);
                SegmentDialog dialog = new SegmentDialog(
                        "New segment",
                        "Create new segment",
                        "Enter a text for your annotation. (The annotation text can also be empty).",
                        observableTopicSet.getTopicsObservableList(),
                        Optional.of(s),
                        false
                );
                dialog.setResultConverter(b -> {
                    if (b == dialog.getButtonTypeOK()) {
                        for(TopicTextControl ttc : dialog.getControls()){
                            s.putAnnotation(ttc.getTopicID(), ttc.getTextFieldText());
                        }
                        addAnnotation(s);
                    }
                    return null;
                });
                dialog.showAndWait();
            }

            //Reset SelectionRectangle
            selection.setWidth(0);
            selection.setHeight(0);
            getChildren().remove(selection);
        }

    }



    public void createCopyAnnotationDialogue()  {
        SegmentDialog dialog = new SegmentDialog(TXT_DOTANNOTATION_TITLE, TXT_DOTANNOTATION_HEADER, TXT_DOTANNOTATION_TEXT, observableTopicSet.getTopicsObservableList(), Optional.empty(), true);

        dialog.setResultConverter(b -> {
            if (b == dialog.getButtonTypeOK()) {
                //If the cbox is selected, a new optional containing the boundaries of the new combined annotation is created.
                Optional<double[]> boundaries = dialog.isCombined() ?
                        Optional.of(customTimeLineController.getCombinedAnnotationBoundaries()) :
                        Optional.empty();
                //collidesWithOtherElements is executed with the boundaries if they are available or with all selected elements otherwise.
                //If a collision was detected, a dialog is prompted which asks the user if they want to create a new timeline.
                //should they decline, the entire procees is aborted.
                if(customTimeLineController.copiedAnnotationsCollideWithOtherAnnotations(boundaries)){
                    if(DialogGenerator.confirmationDialogue(TXT_COLLIDEHANDLER_TITLE, TXT_COLLIDEHANDLER_HEADER, TXT_COLLIDEHANDLER_MSG)){
                        Optional<Segment> optional;
                        if(dialog.isCombined()){
                            Segment s = new Segment(boundaries.get()[0], boundaries.get()[1]);
                            for(TopicTextControl ttc : dialog.getControls()){
                                s.putAnnotation(ttc.getTopicID(), ttc.getTextFieldText());
                            }
                            optional = Optional.of(s);
                        }
                        else{
                            optional = Optional.empty();
                        }
                        try {
                            customTimeLineController.createNewTimeLine(optional);
                        } catch (NoTimeLineSelectedException ex) {
                            DialogGenerator.simpleErrorDialog("Creation Error","Error while creating timeline", ex.toString());
                        }
                    }
                }
                else{
                    //If no collisions were detected, copy the annotations into this timeline.
                    if (dialog.isCombined()) {
                        Segment s = new Segment(boundaries.get()[0], boundaries.get()[1]);
                        for(TopicTextControl ttc : dialog.getControls()){
                            s.putAnnotation(ttc.getTopicID(), ttc.getTextFieldText());
                        }
                        addAnnotation(s);
                    }
                    else {
                        //Add missing topics into this timeline
                        customTimeLineController.addMissingTopics(dialog.getControls().stream().map(TopicTextControl::getTopic).collect(Collectors.toList()));
                        addAnnotations(customTimeLineController.generateMissingSegments());
                    }
                }
            }

            return null;
        });

        dialog.showAndWait();
    }

    public void createAnnotationFromDotsDialogue()  {
        SegmentDialog dialog = new SegmentDialog(TXT_DOTANNOTATION_TITLE, TXT_DOTANNOTATION_HEADER, TXT_DOTANNOTATION_TEXT, observableTopicSet.getTopicsObservableList(), Optional.empty(), true);
        dialog.setResultConverter(b -> {
            if (b == dialog.getButtonTypeOK()) {
                //If the cbox is selected, a new optional containing the boundaries of the new combined annotation is created.
                Optional<double[]> boundaries = dialog.isCombined() ?
                        Optional.of(customTimeLineController.getCombinedDotAnnotationBoundaries()) :
                        Optional.empty();
                //collidesWithOtherElements is executed with the boundaries if they are available or with all selected elements otherwise.
                //If a collision was detected, a dialog is prompted which asks the user if they want to create a new timeline.
                //should they decline, the entire procees is aborted.
                if(customTimeLineController.dotSegmentsCollideWithOtherAnnotations(boundaries) && DialogGenerator.confirmationDialogue(TXT_COLLIDEHANDLER_TITLE, TXT_COLLIDEHANDLER_HEADER, TXT_COLLIDEHANDLER_MSG)){
                    Optional<Segment> annotation = dialog.isCombined() ?
                            Optional.of(new Segment(boundaries.get()[0], boundaries.get()[1])) :
                            Optional.empty();
                    try {
                        customTimeLineController.createNewTimeLine(annotation);
                    } catch (NoTimeLineSelectedException ex) {
                        DialogGenerator.simpleErrorDialog("Creation Error","Error while creating timeline", ex.toString());
                    }
                }
                else{
                    //If no collisions were detected, copy the annotations into this timeline.
                    if (dialog.isCombined()) {
                        Segment s = new Segment(boundaries.get()[0], boundaries.get()[1]);
                        for(TopicTextControl ttc : dialog.getControls()){
                            s.putAnnotation(ttc.getTopicID(), ttc.getTextFieldText());
                        }
                        addAnnotation(s);
                    }
                    else {
                        for(List<ObservableDot> segment : p.getSelectedDotSegments()){
                            addAnnotation(new Segment(
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

    public void deleteSegment(MovableAnnotationRectangle a, ObservableSegment observableSegment){
        getChildren().removeAll(a, a.getLeft(), a.getRight(), a.getDisplayedText());
        customTimeLineController.removeAnnotation(observableSegment);
    }

}
