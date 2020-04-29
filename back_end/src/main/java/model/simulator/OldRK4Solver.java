package model.simulator;

import org.apache.commons.lang.Validate;

import java.util.List;
import java.util.function.Function;

public class OldRK4Solver implements DifferentialSolver {
    /**
     *
     * @param cauchyProblem
     * @param nbIterations
     * @return
     */
    @Override
    public List<Double> next(final CauchyProblem cauchyProblem, final int nbIterations) {
        Validate.notNull(cauchyProblem);
        T_Y result = new T_Y(cauchyProblem.getT_0(), cauchyProblem.getY_0());
        final List<Function<T_Y, Double>> system = cauchyProblem.getF();
        double delta = 1. / nbIterations;
        for(int i = 0; i < nbIterations; ++i){
            result = updateResult(system, result, delta);
        }
        return result.getY();
    }

    /**
     *
     * @param system
     * @param result
     * @param delta
     * @return
     */
    private T_Y updateResult(final List<Function<T_Y, Double>> system, final T_Y result, final double delta){
        Validate.notNull(system);
        Validate.notNull(result);
        T_Y nextResult = new T_Y();
        nextResult.setT(result.getT() + delta);
        int i = 0;
        for(final Function<T_Y, Double> function : system){
            Double nextValue = rk4equation(result, function, delta, i++);
            nextResult.add(nextValue);
        }
        return nextResult;
    }

    private Double rk4equation(final T_Y result, final Function<T_Y, Double> function, final double delta, int i){
        List<Double> kis = result.rungeKuttaKi(function, delta);
        return result.getY_i(i) + delta / 6. * (kis.get(0) + 2. * kis.get(1) + 2. * kis.get(2) + kis.get(3));
    }
}
