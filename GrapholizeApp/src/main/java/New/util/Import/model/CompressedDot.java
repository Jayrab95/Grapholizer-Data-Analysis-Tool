package New.util.Import.model;

import New.Model.Entities.Dot;

public class CompressedDot {
    public int X ;

    public int Fx;

    public int Y;

    public int Fy;

    public int TiltX;

    public int TiltY;

    public int Twist;

    public float Force;

    public int DotType;
    /*
     * time difference between two dots in miliseconds
     */
    public byte TimeDiff;

    public CompressedDot(Dot dot, byte timeDifference){
        X = (int) dot.getX();
        Y = (int) dot.getY();
        Fx = (int) ((dot.getX() - X) * 100);
        Fy = (int) ((dot.getY() - Y) * 100);
        TiltX = 0;
        TiltY = 0;
        Twist = 0;
        dot.getForce();
        switch(dot.getDotType()) {
            case PEN_DOWN:
                this.DotType = 0;
                break;
            case PEN_MOVE:
                this.DotType = 1;
                break;
            case PEN_UP:
                this.DotType = 2;
                break;
            case PEN_HOVER:
                this.DotType = 3;
                break;
            case PEN_ERROR:
                this.DotType = 4;
                break;
            default:
                this.DotType = 4;
        }
        TimeDiff = timeDifference;
    }
}
