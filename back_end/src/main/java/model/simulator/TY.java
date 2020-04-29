package model.simulator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * We will have to solve differential equations systems of order p
 * <p>
 * y'(t) = f(t, y(t)) = (f_1(t, y(t)), f_2(t, y(t)), ... , f_p(t, y(t)))
 * <p>
 * where y(t) is a vector of R^p, y'(t) its derivative and f = (f_1, ... , f_p)
 * is a map from R^p+1 to R^p.
 * <p>
 * To this end, we will use the java class Function that allows to create
 * functions with one parameter using lambdas. Hence we need a class to
 * implement this parameter (t, y(t)).
 * But in fact this class will also be used for other purpose in the
 * numerical differential equation solvers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TY {
    /**
     * t, the time (by default set to 0).
     */
    private double t = 0;
    /**
     * y, a vector of R^n (by default an empty array).
     */
    private List<Double> y = new ArrayList<>();

    /**
     * Syntaxic sugar for getY().add(value).
     *
     * @param value the value to add
     */
    void add(final Double value) {
        y.add(value);
    }

    /**
     * Syntaxic sugar for getY().get(i).
     *
     * @param i the index to get
     *
     * @return y.get(i)
     */
    Double getYi(final int i) {
        return y.get(i);
    }

    /**
     * Syntaxic sugar for getY().size().
     *
     * @return y.size()
     */
    public int size() {
        return y.size();
    }
}
