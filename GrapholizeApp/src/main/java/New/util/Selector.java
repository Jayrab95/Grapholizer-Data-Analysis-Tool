package New.util;

public class Selector {

    Point start = new Point();
    Point end = new Point();

    public void setStart(double x, double y){
        start.setValues(x,y);
    }
    public void setEnd(double x, double y){
        end.setValues(x,y);
    }

    public Point getStart(){return start;}
    public Point end(){return end;}

    private class Point{
        double x;
        double y;
        void setValues(double x, double y){
            this.x = x;
            this.y = y;
        }
    }
}
