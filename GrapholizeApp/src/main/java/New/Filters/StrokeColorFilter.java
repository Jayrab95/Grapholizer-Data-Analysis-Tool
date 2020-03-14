package New.Filters;

import New.Observables.ObservableDot;
import New.Observables.ObservablePage;
import New.Observables.ObservableStroke;
import javafx.scene.paint.Color;

public class StrokeColorFilter extends Filter {
    private Color[] colors;

    public StrokeColorFilter(ObservablePage p, Color... colors) {
        super("Stroke differentiation filter", p);
        this.colors = colors;
    }

    @Override
    public void applyFilter() {
        int currentColor = 0;
        for(ObservableStroke s : p.getObservableStrokes()){
            for(ObservableDot d : s.getObservableDots()){
                d.setColor(colors[currentColor]);
            }
            currentColor = (currentColor + 1) % colors.length;
        }
    }


    @Override
    public void removeFilter() {
        for(ObservableStroke s : p.getObservableStrokes()){
            for(ObservableDot d : s.getObservableDots()){
                d.setColor(Color.BLACK);
            }
        }
    }

    @Override
    public void calculateMetrics(ObservablePage p) {
        //Unneeded for this filter.
    }
}
