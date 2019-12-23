package New.Interfaces.structure;

public interface ISelectOperationObservable {
    void Select(long timeStart, long timeEnd);
    void Deselect(long timeStart, long timeEnd);
    void notifyListeners();
}
