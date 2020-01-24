package New.Model.Entities;

import java.util.Random;

public class SimpleColor {

    private double r;
    private double g;
    private double b;
    private double o;

    public SimpleColor(){
        Random rand = new Random();
        r = rand.nextFloat();
        g = rand.nextFloat();
        b = rand.nextFloat();
        o = 1;
    }

    public SimpleColor(double r, double g, double b, double o){
        this.r = r;
        this.g = g;
        this.b = b;
        this.o = o;
    }

    public double getR() {
        return r;
    }

    public SimpleColor setR(float r) {
        this.r = r;
        return this;
    }

    public double getG() {
        return g;
    }

    public SimpleColor setG(float g) {
        this.g = g;
        return this;
    }

    public double getB() {
        return b;
    }

    public SimpleColor setB(float b) {
        this.b = b;
        return this;
    }

    public double getO() {
        return o;
    }

    public SimpleColor setO(float o) {
        this.o = o;
        return this;
    }
}
