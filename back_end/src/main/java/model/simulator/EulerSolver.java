package model.simulator;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EulerSolver implements DifferentialSolver {
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
            Double nextValue = result.getIthState(i++) + delta * function.apply(result);
            nextResult.add(nextValue);
        }
        return nextResult;
    }
}
