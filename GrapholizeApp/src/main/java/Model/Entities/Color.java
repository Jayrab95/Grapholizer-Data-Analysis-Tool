package Model.Entities;

import java.util.Random;

public class Color {

    private double r;
    private double g;
    private double b;
    private double o;

    public Color(){
        Random rand = new Random();
        r = rand.nextFloat();
        g = rand.nextFloat();
        b = rand.nextFloat();
        o = 1;
    }
    public Color(double r, double g, double b, double o){
        this.r = r;
        this.g = g;
        this.b = b;
        this.o = o;
    }

    public double getR() {
        return r;
    }

    public Color setR(float r) {
        this.r = r;
        return this;
    }

    public double getG() {
        return g;
    }

    public Color setG(float g) {
        this.g = g;
        return this;
    }

    public double getB() {
        return b;
    }

    public Color setB(float b) {
        this.b = b;
        return this;
    }

    public double getO() {
        return o;
    }

    public Color setO(float o) {
        this.o = o;
        return this;
    }
}
