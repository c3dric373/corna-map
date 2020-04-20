package model.simulator;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RK4Solver implements DifferentialSolver {
    /**
     *
     * @param cauchyProblem
     * @param nbIterations
     * @return
     */
    @Override
    public List<Double> step(final CauchyProblem cauchyProblem, final int nbIterations) {
        Validate.notNull(cauchyProblem);
        DerivativeParameters result = cauchyProblem.getInitialConditions();
        final List<Function<DerivativeParameters, Double>> system = cauchyProblem.getSystem();
        double delta = 1. / nbIterations;
        for(int i = 0; i < nbIterations; ++i){
            result = updateResult(system, result, delta);
        }
        return result.getState();
    }

    /**
     *
     * @param system
     * @param result
     * @param delta
     * @return
     */
    private DerivativeParameters updateResult(final List<Function<DerivativeParameters, Double>> system, final DerivativeParameters result, final double delta){
        Validate.notNull(system);
        Validate.notNull(result);
        DerivativeParameters nextResult = new DerivativeParameters();
        nextResult.setTime(result.getTime() + delta);
        int i = 0;
        for(final Function<DerivativeParameters, Double> function : system){
            Double nextValue = rk4equation(result, function, delta, i++);
            nextResult.add(nextValue);
        }
        return nextResult;
    }

    private Double rk4equation(final DerivativeParameters result, final Function<DerivativeParameters, Double> function, final double delta, int i){
        List<Double> kis = result.rungeKuttaKi(function, delta);
        return result.getIthState(i) + delta / 6. * (kis.get(0) + 2. * kis.get(1) + 2. * kis.get(2) + kis.get(3));
    }
}
