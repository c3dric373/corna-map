package model.simulator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DerivativeParameters {
    private double time = 0;
    private List<Double> state = new ArrayList<>();

    void add(final Double value) {
        state.add(value);
    }

    Double getIthState(int i) {
        return state.get(i);
    }

    public int size() {
        return state.size();
    }

    List<Double> rungeKuttaKi(final Function<DerivativeParameters, Double> function, final double delta) {
        final double alphak2k3 = 0.5;
        final double alphak4 = 1.;

        final double k1 = function.apply(this);
        final double k2 = function.apply(kiArgs(k1, delta, alphak2k3));
        final double k3 = function.apply(kiArgs(k2, delta, alphak2k3));
        final double k4 = function.apply(kiArgs(k3, delta, alphak4));

        List<Double> result = new ArrayList<Double>(4);
        result.add(k1);
        result.add(k2);
        result.add(k3);
        result.add(k4);
        return result;
    }

    private DerivativeParameters kiArgs(final double ki, final double delta, final double alpha) {
        List<Double> tmpState = state.stream()
                .map(yn -> yn + alpha * delta * ki)
                .collect(Collectors.toList());
        return new DerivativeParameters(time + alpha * delta, tmpState);
    }
}
