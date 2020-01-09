package New.Interfaces;

public interface Selector {
    void select(double timeStart, double timeEnd);
    void selectOnlyTimeFrame(double timeStart, double timeEnd);
    void selectRect(double x, double y, double width, double height);
    void selectRectUnscaled(double x, double y, double width, double height, double scale);
    void deselect(double timeStart, double timeEnd);
    void deselectAll();
    void deselectRect(double x, double y, double width, double height);
    void deselectRectUnscaled(double x, double y, double width, double height, double scale);
}
