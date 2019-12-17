package New.Filters;

import javafx.scene.paint.Color;

public class StrokeDifferentiationFilter extends Filter {

    private Color[] colors;
    private int currentColor = 0;

    public StrokeDifferentiationFilter(String filterName, Color... colors) {
        super(filterName);
        this.colors = colors;
    }

    @Override
    public Color applyFilter(Color c) {
        currentColor = (currentColor + 1) % colors.length;
        return colors[currentColor];
    }
}
