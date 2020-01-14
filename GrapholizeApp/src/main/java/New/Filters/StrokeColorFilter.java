package New.Filters;

import New.Observables.ObservableDot;
import New.Observables.ObservableStroke;
import javafx.scene.paint.Color;

public class StrokeColorFilter extends StrokeFilter {
    private Color[] colors;
    private int currentColor = 0;

    public StrokeColorFilter(String filterName, Color... colors) {
        super(filterName);
        this.colors = colors;
    }

    @Override
    public void applyFilter(ObservableStroke s) {
        currentColor = (currentColor + 1) % colors.length;
        for(ObservableDot o : s.getObservableDots()){
            o.setColor(colors[currentColor]);
        }
        //return colors[currentColor];
    }

    @Override
    public Color applyFilter(Color c) {
        return null;
    }
}
