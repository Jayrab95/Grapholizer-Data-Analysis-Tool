package New.Model.Entities;

import java.util.Random;

/**
 * The SimpleColor class represents the color of a topic set.
 * It consists of a red, green, blue and opacity value (doubles).
 * Note that these values should be between 0 and 1.
 * These SimpleColor objects can later be converted into JavaFX Color objects using
 * the ColorConverter class.
 */
public class SimpleColor {

    private double r;
    private double g;
    private double b;
    private double o;

    /**
     * Constructor which creates a new color with random r, g and b values and an opacity value of 1
     */
    public SimpleColor(){
        Random rand = new Random();
        r = rand.nextFloat();
        g = rand.nextFloat();
        b = rand.nextFloat();
        o = 1;
    }

    /**
     * Constructor which takes a red, green, blue and opacity value. Note that these values
     * should be between 0 and 1.
     * @param r red value between 0 and 1
     * @param g green value between 0 and 1
     * @param b blue value between 0 and 1
     * @param o opacity value between 0 and 1
     */
    public SimpleColor(double r, double g, double b, double o){
        this.r = r;
        this.g = g;
        this.b = b;
        this.o = o;
    }

    /**
     * @return red value for this color (between 0 and 1)
     */
    public double getR() {
        return r;
    }

    /**
     * Sets the red value for this color (between 0 and 1)
     * @param r red value
     * @return this
     */
    public SimpleColor setR(float r) {
        this.r = r;
        return this;
    }

    /**
     * @return green value for this color (between 0 and 1)
     */
    public double getG() {
        return g;
    }

    /**
     * Sets the green value for this color (between 0 and 1)
     * @param g green value
     * @return this
     */
    public SimpleColor setG(float g) {
        this.g = g;
        return this;
    }

    /**
     * @return blue value for this color (between 0 and 1)
     */
    public double getB() {
        return b;
    }

    /**
     * Sets the blue value for this color (between 0 and 1)
     * @param b blue value
     * @return this
     */
    public SimpleColor setB(float b) {
        this.b = b;
        return this;
    }

    /**
     * @return opacity value for this color (between 0 and 1)
     */
    public double getO() {
        return o;
    }

    /**
     * Sets the opacity value for this color (between 0 and 1)
     * @param o opacity value
     * @return this
     */
    public SimpleColor setO(float o) {
        this.o = o;
        return this;
    }
}
