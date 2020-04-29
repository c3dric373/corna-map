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
 * <p>
 * y'(t) = (f_1(t, y(t)), f_2(t, y(t)), ... , f_n(t, y(t)))
 * <p>
 * where y(t) is a vector of R^n, y'(t) its derivative and the f_i are maps from R^n+1 to R
 * <p>
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
}
