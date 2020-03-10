package New.Interfaces;

public interface Selector {
    void select(double timeStart, double timeEnd);
    void selectOnlyTimeFrame(double timeStart, double timeEnd);

    void deselect(double timeStart, double timeEnd);

    void deselectAll();

    //TODO: Consider moving these into a different interface (perhaps "Canvas selector"?)
    void selectRect(double x, double y, double width, double height);
    void selectOnlyRect(double x, double y, double width, double height);
    void selectRectUnscaled(double x, double y, double width, double height, double scale);
    void selectOnlyRectUnscaled(double x, double y, double width, double height, double scale);

    void deselectRect(double x, double y, double width, double height);
    void deselectRectUnscaled(double x, double y, double width, double height, double scale);
}
