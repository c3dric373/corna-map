package model.simulator;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A Cauchy problem of order p is a differential equations systems of order p
 * <p>
 * y'(t) = f(t, y(t)) = (f_1(t, y(t)), f_2(t, y(t)), ... , f_p(t, y(t)))
 * <p>
 * where y(t) is a vector of R^p, y'(t) its derivative, f a map from R^p+1 to
 * R^p and the f_i are maps from R^p+1 to R together with an initial condition
 * <p>
 * y(t_0) = y_0 = (y_01, y_02, ... , y_0p)
 * <p>
 * The Cauchy-Lipschitz theorem (that you may know as Picard-Lindelöf theorem)
 * states that there is a unique y that fits to such a problem, and we will
 * compute it by using Euler method and Runge-Kutta method.
 */
@Getter
@Setter
public class CauchyProblem {
    /**
     * f, the map from R^n+1 to R^n such that  y'(t) = f(t, y(t)).
     */
    private List<Function<TY, Double>> f;
    /**
     * t_0, the initial time.
     */
    private double t0;
    /**
     * y_0 = y(t_0), the initial condition.
     */
    private List<Double> y0;

    /**
     * Syntaxic sugar for new TY(cauchyProblem.getT0(), cauchyProblem.getY0()).
     * @return the initial condition of the Cauchy problem.
     */
    public TY getInitialCondition(){
        return new TY(t0, y0);
    }

    /**
     * This is builder class that will be used to create a Cauchy problem.
     */
    public static class Builder {
        /**
         * f, the map from R^n+1 to R^n such that  y'(t) = f(t, y(t)).
         */
        private final List<Function<TY, Double>> f;
        /**
         * t_0, the initial time.
         */
        private double t0;
        /**
         * y_0 = y(t_0), the initial condition.
         */
        private List<Double> y0;

        /**
         * By default t0 is set to 0 and y0 and f are empty arrays.
         */
        public Builder() {
            t0 = 0;
            y0 = new ArrayList<>();
            f = new ArrayList<>();
        }

        /**
         * Sets the time t_0 for the initial condition of the Cauchy Problem (by
         * default, t_0 = 0).
         *
         * @param t the new t_0
         *
         * @return the builder, in order to chain the calls
         */
        public Builder setInitialTime(final double t) {
            t0 = t;
            return this;
        }

        /**
         * Adds an unknown y_i to the differential equation.
         *
         * @param y0i the initial condition of the unknown y_i(t_0) = y_0i
         * @param fi  the function that defines the derivative of the unknown
         *            y_i'(t) = f_i(t, y(t))
         *
         * @return the builder, in order to chain the calls
         */
        public Builder addParameter(final Double y0i,
                                    final Function<TY, Double> fi) {
            Validate.notNull(y0i, "initialCondition null");
            Validate.notNull(fi, "derivative null");
            f.add(fi);
            y0.add(y0i);
            return this;
        }

        /**
         * Builds a Cauchy problem according to the data that was set.
         *
         * @return the Cauchy problem y'(t) = f(t, y(t)) and y(t_0) = y_0
         */
        public CauchyProblem build() {
            CauchyProblem cauchyProblem = new CauchyProblem();
            cauchyProblem.f = f;
            cauchyProblem.t0 = t0;
            cauchyProblem.y0 = y0;
            return cauchyProblem;
        }
    }

    /**
     * Creates a builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }
}
