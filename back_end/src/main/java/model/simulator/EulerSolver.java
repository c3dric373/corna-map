package model.simulator;

import org.apache.commons.lang.Validate;

import java.util.List;
import java.util.function.Function;

public class EulerSolver implements DifferentialSolver {
    /**
     * Given a Cauchy problem and an initial time t_0, this function calculates y(t_0 + 1)
     * where y is the solution of the Cauchy problem, using the Euler method
     * @param cauchyProblem, the Cauchy problem we want to solve
     * @param nbIterations, the precision we want to have, we will compute all the y(t_0 + k h)
     *                      for all k between 1 and nbIteration and h = 1 / nbIteration
     * @return the result y(t_0 + 1), approximated by the Euler method
     */
    @Override
    public List<Double> next(final CauchyProblem cauchyProblem, final int nbIterations) {
        Validate.notNull(cauchyProblem);
        T_Y t_y = new T_Y(cauchyProblem.getT_0(), cauchyProblem.getY_0());
        final List<Function<T_Y, Double>> f = cauchyProblem.getF();
        double h = 1. / nbIterations;

        for(int i = 0; i < nbIterations; ++i){
            t_y = updateResult(f, t_y, h);
        }
        return t_y.getY();
    }

    /**
     * updates t and y(t) making one iteration of the Euler method
     * t becomes t + h and y(t) becomes y(t + h)
     * @param f, the derivatives of the differential system
     * @param t_y, t and y(t)
     * @param h, the step size
     * @return t + h and y(t + h), approximated by the Euler method
     */
    private T_Y updateResult(final List<Function<T_Y, Double>> f, final T_Y t_y, final double h){
        Validate.notNull(f);
        Validate.notNull(t_y);
        T_Y newt_y = new T_Y();
        newt_y.setT(t_y.getT() + h);
        int i = 0;
        for(final Function<T_Y, Double> function : f){
            Double nextValue = t_y.getY_i(i++) + h * function.apply(t_y);
            newt_y.add(nextValue);
        }
        return newt_y;
    }
}
