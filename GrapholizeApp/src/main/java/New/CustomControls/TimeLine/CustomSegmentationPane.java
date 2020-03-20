package New.CustomControls.TimeLine;

import New.Controllers.CustomTimeLineController;
import New.CustomControls.Containers.SegmentationContainer;
import New.CustomControls.Annotation.MutableSegmentRectangle;
import New.Dialogues.DialogControls.TopicTextControl;
import New.Dialogues.FilterSelectDialog;
import New.Dialogues.SegmentDialog;
import New.Execptions.NoTimeLineSelectedException;
import New.Model.Entities.Segment;
import New.Observables.ObservableSegment;
import New.Observables.ObservableDot;
import New.Observables.ObservablePage;
import New.Observables.ObservableSuperSet;
import New.util.DialogGenerator;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomSegmentationPane extends SelectableSegmentationPane {

    public static final String TXT_COPYANNOTATION_TITLE = "Copy selected segments";
    public static final String TXT_COPYANNOTATION_HEADER = "Copy selected segments into this segmentation";
    public static final String TXT_COPYANNOTATION_TEXT =
            "The selected annotations will be copied into this segmentation. \n"
            + "You may choose to combine the selected segments into a single annotations. If so, you may enter a new Annotation text.";
    public static final String TXT_DOTANNOTATION_TITLE = "Create segment(s) out of selected dots";
    public static final String TXT_DOTANNOTATION_HEADER = "Create segment(s) out of selected dots";
    public static final String TXT_DOTANNOTATION_TEXT = "This dialog will create new segments out of the currently selected dots on the canvas and put them into this segmentation.\n"
            + "Per default, these segments are separated according to the strokes that the selected dots belong to. "
            + "You may choose to combine the selected annotations into a single segment. If so, you may enter a new Annotation text.";
    public static final String TXT_COPYANNOTATION_DEFAULTVAL = "New combined annotation";
    public static final String TXT_COLLIDEHANDLER_TITLE = "Annotation copy error";
    public static final String TXT_COLLIDEHANDLER_HEADER = "Annotation copy error";
    public static final String TXT_COLLIDEHANDLER_MSG =
            "One or more annotations that you are attempting to copy would collide with other existing annotations. \n"
            +"Would you like to create a new timeline for these new annotations?";
    public static final String TXT_DELETEALLSELECTED_TITLE = "Delete all selected segments";
    public static final String TXT_DELETEALLSELECTED_HEADER = "Delete all selected segments";
    public static final String TXT_DELETEALLSELECTED_TEXT = "Are you sure you want to delete all selected segments? This action cannot be undone.";


    private double[] dragBounds;

    private ObservableSuperSet observableSuperSet;
    private ObservablePage observablePage;
    private CustomTimeLineController customTimeLineController;
    private ContextMenu contextMenu;

    public CustomSegmentationPane(double width, double height, DoubleProperty scaleProp, ObservableSuperSet tag, ObservablePage observablePage, SegmentationContainer parent) {
        super(width, height, scaleProp, tag.getNameProperty(), parent, tag.getTopicSetID());
        this.observableSuperSet = tag;
        customTimeLineController = new CustomTimeLineController(tag, observablePage, parent);

        dragBounds = new double[2];
        this.observablePage = observablePage;

        this.contextMenu = generateContextMenu();
        generateSegmentRectangles();



    }

    public CustomSegmentationPane(double width, double height, DoubleProperty scaleProp, ObservableSuperSet tag, ObservablePage observablePage, SegmentationContainer parent, Set<Segment> segments) {
        this(width, height, scaleProp, tag, observablePage, parent);
        addAnnotations(segments);
    }

    public CustomSegmentationPane(double width, double height, DoubleProperty scaleProp, ObservableSuperSet tag, ObservablePage observablePage, SegmentationContainer parent, Segment a) {
        this(width, height, scaleProp, tag, observablePage, parent);
        addAnnotation(a);
    }

    private void generateSegmentRectangles(){
        for(Segment s : this.observablePage.getPageProperty().get().getSegmentation(this.observableSuperSet.getTopicSetID())){
            ObservableSegment oSegment = new ObservableSegment(s, observableSuperSet);
            MutableSegmentRectangle mov = new MutableSegmentRectangle(
                    observableSuperSet.getColorProperty(),
                    scale,
                    oSegment,
                    this,
                    observablePage,
                    observableSuperSet);
            getChildren().addAll(mov, mov.getDisplayedTextLabel());
            this.observableSegments.add(oSegment);
        }
    }

    public String getTimeLineName(){
        return timeLineName.get();
    }

    public ObservableSuperSet getObservableSuperSet() {
        return observableSuperSet;
    }

    private void addAnnotation(Segment a){
        customTimeLineController.addAnnotation(a);
        ObservableSegment oSegment = new ObservableSegment(a, observableSuperSet);
        MutableSegmentRectangle mov = new MutableSegmentRectangle(
                observableSuperSet.getColorProperty(),
                scale,
                oSegment,
                this,
                observablePage,
                observableSuperSet);
        getChildren().addAll(mov, mov.getDisplayedTextLabel());
        this.observableSegments.add(oSegment);
    }

    private void addAnnotations(Set<Segment> segments){
        for(Segment a : segments){
            addAnnotation(a);
        }
    }

    /**
     * Sets the bounds for the drag function.
     * @param xPosition Current xPosition on MousePress.
     */
    public double[] getBounds(double xPosition){
        double[] unscaledBounds = customTimeLineController.getDragBounds(xPosition / scale.get());
        return new double[]{unscaledBounds[0] * scale.get(), unscaledBounds[1] * scale.get()};
        /*
        //TODO: Move into controller
        double lowerBounds = 0;
        double upperBounds = getWidth();
        //The children list is not sorted and can also include handles of annotations
        for(Node n : getChildren()){
            if(n instanceof SegmentRectangle){
                SegmentRectangle rect = (SegmentRectangle)n;
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

         */
    }

    private ContextMenu generateContextMenu(){
        //TODO: Find better way to extend context menu functionality
        MenuItem item_filterSelect = new MenuItem("Filter select");
        item_filterSelect.setOnAction(event -> handleFilterSelectClick());
        MenuItem item_createNegativeTimeLine = new MenuItem("Create negative segmentation out of this segmentation");
        item_createNegativeTimeLine.setOnAction(event -> handelContextCreateNegativeTimeLineClick());
        MenuItem item_copyAnnotations = new MenuItem("Copy selected segments into this timeline");
        item_copyAnnotations.setOnAction(event -> handleContextCopyAnnotationsClick());
        item_copyAnnotations.disableProperty().bind(selectedSegmentationIsNullProperty());
        MenuItem item_copyNegativeSegments = new MenuItem("Paste negative segments from selected segmentation into this segmentation");
        item_copyNegativeSegments.setOnAction(event -> copyNegativeSegmentsIntoSegmentation());
        item_copyNegativeSegments.disableProperty().bind(selectedSegmentationIsNullProperty());
        MenuItem item_createAnnotationsOutOfDots = new MenuItem("Create segments out of selected dots");
        item_createAnnotationsOutOfDots.setOnAction(event -> createAnnotationFromDotsDialogue());
        MenuItem item_editTimeLine = new MenuItem("Edit super set");
        item_editTimeLine.setOnAction(event -> customTimeLineController.editTimeLine());
        MenuItem item_removeTimeLine = new MenuItem("Remove super set");
        item_removeTimeLine.setOnAction(event -> customTimeLineController.removeTimeLine(this));
        return new ContextMenu(item_filterSelect, item_createNegativeTimeLine, item_editTimeLine, item_copyAnnotations, item_createAnnotationsOutOfDots, item_copyNegativeSegments, item_removeTimeLine);
    }

    private void handleFilterSelectClick(){
        Optional<Map<String, String>> o =  new FilterSelectDialog(observableSuperSet.getInner()).showAndWait();
        if(o.isPresent()){
            this.setTimeLineSelected(true);
            List<MutableSegmentRectangle> children = getChildren().stream()
                    .filter(node -> node instanceof MutableSegmentRectangle)
                    .map(node -> (MutableSegmentRectangle) node)
                    .collect(Collectors.toList());
            customTimeLineController.filterSelect(this, o.get(), children);
        }
    }

    private void handelContextCreateNegativeTimeLineClick(){
        customTimeLineController.createNegativeTimeLine();
    }


    private void handleContextCopyAnnotationsClick(){
        createCopyAnnotationDialogue();
    }

    //Source: https://coderanch.com/t/689100/java/rectangle-dragging-image

    @Override
    public void handleTimelineMousePress(MouseEvent event){
        if(event.getButton() == MouseButton.SECONDARY){
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
            event.consume();
        }
        else{
            super.handleTimelineMousePress(event);
            /*
            this.setTimeLineSelected(true);


            //Prepare selection
            getChildren().add(selection);
            selection.setWidth(0);
            selection.setHeight(getHeight());

            anchor.setX(event.getX());
            anchor.setY(0);

            selection.setX(event.getX());
            selection.setY(0);

            if(event.isControlDown()){
                selectMode = true;
                selection.setFill(new Color(0,0,1,0.5)); //transparent blue
            }

             */
            if(!selectMode){
                dragBounds = getBounds(event.getX());
                selection.setFill(null); // transparent
                selection.setStroke(Color.BLACK); // border
                selection.getStrokeDashArray().add(10.0);
            }

        }
    }

    @Override
    protected void handleTimelineMouseDrag(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(selectMode || (event.getX() > dragBounds[0] && event.getX() < dragBounds[1]) ){
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

    @Override
    protected void handleTimelineMouseRelease(MouseEvent e){
        if(e.getButton().equals(MouseButton.PRIMARY)){
                if(selection.getWidth() > 0){
                    //Call annotation creation dialogue
                    double timeStart = selection.getX() / scale.get();
                    double timeStop = (selection.getX() + selection.getWidth()) / scale.get();

                    if(selectMode){
                        observableSegments.stream()
                                .filter(observableSegment -> observableSegment.isWithinTimeRange(timeStart, timeStop))
                                .forEach(observableSegment -> observableSegment.setSelected(true));
                    }

                    else {
                        Segment s = new Segment(timeStart, timeStop);
                        SegmentDialog dialog = new SegmentDialog(
                                "New segment",
                                "Create new segment",
                                "Enter a text for your annotation. (The annotation text can also be empty).",
                                observableSuperSet.getTopicsObservableList(),
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
                }
            }
            //Reset SelectionRectangle
            selection.setWidth(0);
            selection.setHeight(0);
            getChildren().remove(selection);
            selectMode = false;
    }

    public void copyNegativeSegmentsIntoSegmentation(){
        Set<Segment> negativeSegments = customTimeLineController.getNegativeSegmentsFromSelectedSegmentation();
        if(customTimeLineController.setCollidesWithOtherAnnotations(negativeSegments)){
            if(DialogGenerator.confirmationDialogue(TXT_COLLIDEHANDLER_TITLE, TXT_COLLIDEHANDLER_HEADER, TXT_COLLIDEHANDLER_MSG)){
                customTimeLineController.createNewTimeLineOutOfSet(negativeSegments);
            }
        }
        else{
            addAnnotations(negativeSegments);
        }
    }

    public void createCopyAnnotationDialogue()  {

        SegmentDialog dialog = new SegmentDialog(TXT_DOTANNOTATION_TITLE, TXT_DOTANNOTATION_HEADER, TXT_DOTANNOTATION_TEXT, observableSuperSet.getTopicsObservableList(), Optional.empty(), true);

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
                            customTimeLineController.createNewTimeLine(optional, true);
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
        SegmentDialog dialog = new SegmentDialog(TXT_DOTANNOTATION_TITLE, TXT_DOTANNOTATION_HEADER, TXT_DOTANNOTATION_TEXT, observableSuperSet.getTopicsObservableList(), Optional.empty(), true);
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
                        customTimeLineController.createNewTimeLine(annotation, false);
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
                        for(List<ObservableDot> segment : observablePage.getSelectedDotSections()){
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

    public void deleteSegment(MutableSegmentRectangle a, ObservableSegment observableSegment){
        getChildren().removeAll(a, a.getLeft(), a.getRight(), a.getDisplayedTextLabel());
        this.observableSegments.remove(observableSegment);
        customTimeLineController.removeAnnotation(observableSegment);
    }

}
