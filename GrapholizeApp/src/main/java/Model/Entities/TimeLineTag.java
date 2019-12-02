package Model.Entities;

public class TimeLineTag {
    private String tag;
    private Color color;

    public TimeLineTag(String tag, Color color) {
        this.tag = tag;
        this.color = color;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
