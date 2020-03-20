package New.Observables;

import New.CustomControls.SegmentRectangles.SegmentRectangle;
import New.CustomControls.SegmentRectangles.MutableSegmentRectangle;
import New.CustomControls.SegmentRectangles.SelectableSegmentRectangle;
import New.CustomControls.SegmentationPanes.CustomSegmentationPane;
import New.CustomControls.SegmentationPanes.SelectableSegmentationPane;
import New.CustomControls.SegmentationPanes.UnmodifiableSelectableSegmentationPane;
import New.Execptions.NoSegmentationSelectedException;
import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The ObservableSegmentation is an observable singleton object that holds a reference to a
 * SelectableSegmentationPane. This denotes the currently active segmentation.
 * The listeners (other SelectableSegmentationPanes) are notified if a new segmentation
 * becomes the active segmentation and deselect all currently selected segments on their pane.
 * The ObservableSegmentation also contains logic for retrieving selected segments.
 * note that the reference can also be null, which means that no segmentation is currently selected.
 * This can be checked by using the "selectedSegmentationAvailable()" method.
 */
public class ObservableSegmentation {

    private ObjectProperty<SelectableSegmentationPane> innerSegmentation;

    /**
     * Constructor which instantiates a new empty objectProperty wrapper
     */
    public ObservableSegmentation(){
        this.innerSegmentation = new SimpleObjectProperty<>();
    }

    /**
     * Constructor which instantiates the ObjectProperty with the given reference
     * @param selectableSegmentationPane initial selectableSegmentationPane value for this wrapper
     */
    public ObservableSegmentation(SelectableSegmentationPane selectableSegmentationPane){
        this.innerSegmentation = new SimpleObjectProperty<>(selectableSegmentationPane);
    }

    /**
     * Returns the ObjectProperty wrapper
     * @return
     */
    public ObjectProperty<SelectableSegmentationPane> getSelectedSegmentationProperty(){
        return this.innerSegmentation;
    }

    /**
     * Returns the SelectableSegmentationPane that is currently stored in this Observable.
     * @return
     */
    public SelectableSegmentationPane getSelectedSegmentationPane() {
        return innerSegmentation.get();
    }

    /**
     * Checks if there is currently a selected segmentation available (inner reference is not null)
     * @return true if there is a selected segmentation available, false if the reference is currently null
     */
    public boolean selectedSegmentationAvailable(){
        return innerSegmentation.isNotNull().get();
    }

    /**
     * @return Set of selected SegmentRectangles on the currently active segmentation or an empty set
     * if no segmentation is selected.
     */
    public Set<SegmentRectangle> getSelectedSegmentRectangles() {
        //The children first need to be filtered to see whether or not they're actually rectangles (Can also be drag rect or label)
        //Then the nodes acquire the correct cast
        //finally, they're filtered for selection
        if(selectedSegmentationAvailable()){
            return innerSegmentation.get().getChildren().stream()
                    .filter(node -> node instanceof SelectableSegmentRectangle)
                    .map(node -> (SelectableSegmentRectangle)node)
                    .filter(SelectableSegmentRectangle::isSelected)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    /**
     * @return the set of selected ObservableSegments of the selected segmentation
     * @throws NoSegmentationSelectedException if no segmentation is currently selected
     */
    public TreeSet<ObservableSegment> getSelectedSegments() throws NoSegmentationSelectedException {
        if(selectedSegmentationAvailable()){
            return new TreeSet(innerSegmentation.get().getObservableSegments().stream()
                    .filter(observableSegment -> observableSegment.isSelected())
                    .collect(Collectors.toSet()));
        }
        throw new NoSegmentationSelectedException();
    }

    /**
     * Sets the active SegmentationPane to a different reference.
     * Note that this causes a notification round to all listeners, if the given reference does not
     * equal the reference that is currently already stores in the property
     * @param newActiveSegmentation new SegmentationPane reference
     */
    public void setSelectedTimeLine(SelectableSegmentationPane newActiveSegmentation){
        if(this.innerSegmentation.get() != newActiveSegmentation){
            this.innerSegmentation.set(newActiveSegmentation);
        }
    }

    /**
     * Returns the super set name of the active segmentation
     * @return see above
     */
    public String getSegmentationName(){
        return innerSegmentation.get().getTimeLineName();
    }

    /**
     * Returns an Optional which either contains the observableSuperSet of the selected segmentation,
     * if said segmentation is an instance of either CustomSegmentationPane or UnmodifiableSelectableSegmentationPane
     * or an empty Optional otherwise
     * @return see above
     */
    public Optional<ObservableSuperSet> getSelectedSegmentationTopicSet(){
        if(innerSegmentation.get() instanceof CustomSegmentationPane){
            return Optional.of(((CustomSegmentationPane) innerSegmentation.get()).getObservableSuperSet());
        }
        else if(innerSegmentation.get() instanceof UnmodifiableSelectableSegmentationPane){
            return Optional.of(((UnmodifiableSelectableSegmentationPane) innerSegmentation.get()).getObservableSuperSet());
        }
        return Optional.empty();
    }

    /**
     * Compares the reference of the SelectableSegmentationPane with the one that is currently
     * stored in the property
     * @param selectableSegmentationPane SelectableSegmentationPane that is to be compared
     * @return true if they're the same reference or false otherwise
     */
    public boolean equals(SelectableSegmentationPane selectableSegmentationPane){
        return innerSegmentation.get() == selectableSegmentationPane;
    }

    /**
     * Returns a list of topics new topics that are copies of topics which are missing from the given topic set
     * @param targetTopicSet the set which will receive thew missing sets
     * @return list of missing topic copies
     */
    public List<Topic> getMissingTopics(ObservableSuperSet targetTopicSet){
        Optional<ObservableSuperSet> optional = getSelectedSegmentationTopicSet();
        if(optional.isPresent()){
            //If the selected segmentation has a topic set (ie. it is not the stroke timeline), then
            //return a list of all topics that are missing from the target set (topics, whose >name< does
            //not appear in the target topicSet.
            return getSelectedSegmentationTopicSet().get().getTopicsObservableList().stream()
                    .filter(originTopic -> targetTopicSet.getTopicsObservableList().stream().noneMatch(targetTopic -> targetTopic.getTopicName().equals(originTopic.getTopicName())))
                    .map(originTopic -> new Topic(originTopic.getTopicName(), targetTopicSet.generateTopicId(originTopic.getTopicName())))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Generates the missing segments and fills them with annotations. If the target segmentation already contains topics
     * with the same name, the annotations are migrated over.
     * @param targetTopics topics available in the target segmentation
     * @return set of missing segments
     */
    public Set<Segment> generateMissingSegments(List<Topic> targetTopics){
        Set<SegmentRectangle> selectedAnnotations = getSelectedSegmentRectangles();
        Set<Segment> res = new TreeSet<>();
        Iterator<SegmentRectangle> it = selectedAnnotations.iterator();
        while(it.hasNext()){
            SegmentRectangle a = it.next();
            Segment newSegment = new Segment(a.getTimeStart(), a.getTimeStop());
            if(a instanceof MutableSegmentRectangle){
                for(Topic t : ((MutableSegmentRectangle)a).getObservableSuperSet().getTopicsObservableList()){
                    Optional<Topic> optionalTopic = targetTopics.stream().filter(top -> top.getTopicName().equals(t.getTopicName())).findFirst();
                    if(optionalTopic.isPresent()){
                        newSegment.putAnnotation(optionalTopic.get().getTopicID(), ((MutableSegmentRectangle)a).getObservableSegment().getAnnotation(t.getTopicID()));
                    }
                }
            }
            res.add(newSegment);
        }
        return res;
    }

    /**
     * Checks if the stored reference is an instance of CustomSegmentationPane
     * @return true if the stored reference is an instance of CustomSegmentationPane, false otherwise.
     */
    public boolean selectedSegmentationIsCustom(){
        return innerSegmentation.get() instanceof CustomSegmentationPane;
    }

    /**
     * Deletes all selected segments in the actve segmentation
     */
    public void deleteSelectedSegments(){
        if(selectedSegmentationIsCustom()){
            for(SegmentRectangle sr : getSelectedSegmentRectangles()){
                MutableSegmentRectangle msr = (MutableSegmentRectangle)sr;
                ((CustomSegmentationPane) innerSegmentation.get()).deleteSegment(msr, msr.getObservableSegment());
            }

        }
    }
}
