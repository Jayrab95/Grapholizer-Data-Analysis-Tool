package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicAveragePressure extends Characteristic<Double> {

    public CharacteristicAveragePressure(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Double calculateImplementation(List<List<Dot>> listDots) {
        int numberOfDots = 0;
        double forceSum = 0.0d;
        for (List<Dot> listDot : listDots) {
            numberOfDots += listDot.size();
            for (Dot dot : listDot) {
                forceSum += dot.getForce();
            }
        }
        return (forceSum / (double)(numberOfDots));
    }

}
