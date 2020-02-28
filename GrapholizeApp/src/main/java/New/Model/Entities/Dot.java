package New.Model.Entities;

import New.util.Import.CompressedDot;

public class Dot {

    public enum DotType
    {
        PEN_DOWN, PEN_MOVE, PEN_UP, PEN_HOVER, PEN_ERROR
    }

    private final int tiltX;
    private final int tiltY;
    private final int twist;
    private final float force;
    private final long timeStamp;
    private final DotType dotType;
    private final float x;
    private final float y;


    /**
     * Constructor, which is used when creating Dot object from a .data file.
     * These Dot objects only contain the X/Y coordinates, the force and a timestamp.
     * All other numeric values are set to 0. The DotType is set to PEN_DOWN per default.
     * @param x The x coordinate of the dot
     * @param y The y coordinate of the dot
     * @param force the force applied to the dot.
     * @param timeStamp the timestamp of the dot
     */
    public Dot(float x, float y, float force, long timeStamp){
        this.x = x;
        this.y = y;
        this.force = force;
        this.timeStamp = timeStamp;
        //Set the DotType of each dot to PEN_DOWN per default.
        this.dotType = DotType.PEN_DOWN;
        //Set all other attributes to 0.
        this.tiltX = 0;
        this.tiltY = 0;
        this.twist = 0;
    }

    /**
     * The constructor which is used when reading dots from a JSON file. All properties are set.
     * @param tiltX
     * @param tiltY
     * @param twist
     * @param force
     * @param timeStamp
     * @param dotType
     * @param x
     * @param y
     */
    public Dot(int tiltX, int tiltY, int twist, float force, long timeStamp, DotType dotType, float x, float y) {
        this.tiltX = tiltX;
        this.tiltY = tiltY;
        this.twist = twist;
        this.force = force;
        this.timeStamp = timeStamp;
        this.dotType = dotType;
        this.x = x;
        this.y = y;
    }

    public Dot (CompressedDot cd, long timeStamp) {
        this.tiltX = cd.TiltX;
        this.tiltY = cd.TiltY;
        this.twist = cd.Twist;
        this.force = cd.Force;
        this.timeStamp = timeStamp;
        switch(cd.DotType) {
            case 0:
                this.dotType = DotType.PEN_DOWN;
                break;
            case 1:
                this.dotType = DotType.PEN_MOVE;
                break;
            case 2:
                this.dotType = DotType.PEN_UP;
                break;
            case 3:
                this.dotType = DotType.PEN_HOVER;
                break;
            case 4:
                this.dotType = DotType.PEN_ERROR;
                break;
            default:
                this.dotType = DotType.PEN_ERROR; //Maybe another default is ne
        }
        this.x = cd.X + cd.Fx * 0.01f;
        this.y = cd.Y + cd.Fy * 0.01f;
    }
    public int getTiltX() {
        return tiltX;
    }


    public int getTiltY() {
        return tiltY;
    }


    public int getTwist() {
        return twist;
    }


    public float getForce() {
        return force;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public long getTimeStamp(){
        return timeStamp;
    }


    public DotType getDotType() {
        return dotType;
    }

}
