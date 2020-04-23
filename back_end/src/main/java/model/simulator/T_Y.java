package model.simulator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * We will have to solve differential equations systems of order n
 *
 * y'(t) = (f_1(t, y(t)), f_2(t, y(t)), ... , f_n(t, y(t)))
 *
 * where y(t) is a vector of R^n, y'(t) its derivative and the f_i are maps from R^n+1 to R
 *
 * To this end, we will use the java class Function that allows to create functions with one parameter using lambdas
 * Hence we need a class to implement this parameter (t, y(t))
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class T_Y {
    private double t = 0;
    private List<Double> y = new ArrayList<>();

    void add(final Double value) {
        y.add(value);
    }

    Double getY_i(int i) {
        return y.get(i);
    }

    public int size() {
        return y.size();
    }

    List<Double> rungeKuttaKi(final Function<T_Y, Double> function, final double delta) {
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

    private T_Y kiArgs(final double ki, final double delta, final double alpha) {
        List<Double> tmpState = y.stream()
                .map(yn -> yn + alpha * delta * ki)
                .collect(Collectors.toList());
        return new T_Y(t + alpha * delta, tmpState);
    }
}
