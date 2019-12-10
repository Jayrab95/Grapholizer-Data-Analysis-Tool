package New.Model.Entities;

public class TimeLineTag {
    private String tag;
    private SimpleColor simpleColor;

    public TimeLineTag(String tag, SimpleColor simpleColor) {
        this.tag = tag;
        this.simpleColor = simpleColor;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public SimpleColor getSimpleColor() {
        return simpleColor;
    }

    public void setSimpleColor(SimpleColor simpleColor) {
        this.simpleColor = simpleColor;
    }
}
