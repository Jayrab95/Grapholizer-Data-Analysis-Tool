package New.util.math;

import New.Model.Entities.Dot;

import java.util.List;

public final class VelocityParameters {
    public final List<Dot> dots;
    public final List<Double> velocities;
    public final List<Double> accelerations;
    public final List<Double> jerks;
    public final List<Double> normalizedJerks;

    public VelocityParameters(List<Dot> dots
            , List<Double> velocities
            , List<Double> accelerations
            , List<Double> jerks
            , List<Double> normalizedJerks) {
        this.dots = dots;
        this.velocities = velocities;
        this.accelerations = accelerations;
        this.jerks = jerks;
        this.normalizedJerks = normalizedJerks;
    }
}
