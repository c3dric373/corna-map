package model.simulator;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A Cauchy problem of order n is a differential equations systems of order n
 * <p>
 * y'(t) = f(t, y(t)) = (f_1(t, y(t)), f_2(t, y(t)), ... , f_n(t, y(t)))
 * <p>
 * where y(t) is a vector of R^n, y'(t) its derivative, f a map from R^n+1 to
 * R^n and the f_i are maps from R^n+1 to R together with an initial condition
 * <p>
 * y(t_0) = y_0 = (y_01, y_02, ... , y_0n)
 * <p>
 * The Cauchy-Lipschitz theorem (that you may know as Picard-Lindelöf theorem)
 * states that there is a unique y that fits to such a problem, and we will
 * compute it by using Euler method and Runge-Kutta method
 */
@Getter
@Setter
public class CauchyProblem {
    private double t_0;
    private List<Double> y_0;
    private List<Function<TY, Double>> f;

    public static class Builder {
        private double t_0;
        private List<Double> y_0;
        private final List<Function<TY, Double>> f;

        /**
         * This is builder class that will be used to create a Cauchy problem
         */
        public Builder() {
            t_0 = 0;
            y_0 = new ArrayList<>();
            f = new ArrayList<>();
        }

        /**
         * Sets the time t_0 for the initial condition of the Cauchy Problem (by
         * default, t_0 = 0)
         *
         * @param t, the new t_0
         *
         * @return the builder, in order to chain the calls
         */
        public Builder setInitialTime(final Double t) {
            t_0 = t;
            return this;
        }

        /**
         * Adds an unknown y_i to the differential equation
         *
         * @param y_0i, the initial condition of the unknown y_i(t_0) = y_0i
         * @param f_i,  the function that defines the derivative of the unknown
         *              y_i'(t) = f_i(t, y(t))
         *
         * @return the builder, in order to chain the calls
         */
        public Builder addParameter(final Double y_0i, final Function<TY, Double> f_i) {
            Validate.notNull(y_0i, "initialCondition null");
            Validate.notNull(f_i, "derivative null");
            f.add(f_i);
            y_0.add(y_0i);
            return this;
        }

        /**
         * Builds a Cauchy problem according to the data that was set
         *
         * @return the Cauchy problem y'(t) = f(t, y(t)) and y(t_0) = y_0
         */
        public CauchyProblem build() {
            CauchyProblem cauchyProblem = new CauchyProblem();
            cauchyProblem.f = f;
            cauchyProblem.t_0 = t_0;
            cauchyProblem.y_0 = y_0;
            return cauchyProblem;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
