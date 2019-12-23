package New.Interfaces.structure;

public interface ITimelineOperationsObservable {
    void createTimeline(String timelineTag);
    void removeTimeline(String timelineTag);
    void notifyListeners();
}
