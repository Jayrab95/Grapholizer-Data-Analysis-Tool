package New.Observables;

import New.Interfaces.Observer.PageObserver;
import New.Interfaces.Selector;

import java.util.List;
import java.util.stream.Collectors;

public class DotSelector implements Selector, PageObserver {

    private List<ObservableStroke> strokes;

    public DotSelector(ObservablePage p){
        p.addObserver(this);
        strokes = p.getObservableStrokes();
    }

    @Override
    public void select(double timeStart, double timeEnd) {
        strokes.stream()
                .filter(observableStroke -> observableStroke.getTimeStart() >= timeStart && observableStroke.getTimeEnd() <= timeEnd)
                .flatMap(observableStroke -> observableStroke.getObservableDots().stream())
                .filter(observableDot -> observableDot.getTimeStamp() >= timeStart && observableDot.getTimeStamp() <= timeEnd)
                .forEach(observableDot -> observableDot.setSelected(true));

    }

    @Override
    public void selectOnlyTimeFrame(double timeStart, double timeEnd) {

    }

    @Override
    public void selectRect(double x, double y, double width, double height) {

    }

    @Override
    public void selectRectUnscaled(double x, double y, double width, double height, double scale) {

    }

    @Override
    public void deselect(double timeStart, double timeEnd) {
        strokes.stream()
                .filter(observableStroke -> observableStroke.getTimeStart() >= timeStart && observableStroke.getTimeEnd() <= timeEnd)
                .forEach(observableStroke -> observableStroke.setSelected(false));
    }

    @Override
    public void deselectAll() {

    }

    @Override
    public void deselectRect(double x, double y, double width, double height) {

    }

    @Override
    public void deselectRectUnscaled(double x, double y, double width, double height, double scale) {

    }

    public List<List<ObservableDot>> getSelectedDots(){
        return strokes.stream()
                .map(observableStroke -> observableStroke.getObservableDots())
                .map(dots -> dots.stream().filter(dot -> dot.isSelected()).collect(Collectors.toList()))
                .filter(dots -> dots.size() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public void update(ObservablePage sender) {
        strokes = sender.getObservableStrokes();
    }
}
