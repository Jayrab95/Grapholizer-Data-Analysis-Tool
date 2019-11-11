package Model;

import java.util.Random;

public class Color {

    private float r;
    private float g;
    private float b;
    private float o;

    public Color(){
        Random rand = new Random();
        r = rand.nextFloat();
        g = rand.nextFloat();
        b = rand.nextFloat();
        o = 1;
    }
    public Color(float r, float g, float b, float o){
        this.r = r;
        this.g = g;
        this.b = b;
        this.o = o;
    }

    public float getR() {
        return r;
    }

    public Color setR(float r) {
        this.r = r;
        return this;
    }

    public float getG() {
        return g;
    }

    public Color setG(float g) {
        this.g = g;
        return this;
    }

    public float getB() {
        return b;
    }

    public Color setB(float b) {
        this.b = b;
        return this;
    }

    public float getO() {
        return o;
    }

    public Color setO(float o) {
        this.o = o;
        return this;
    }
}
