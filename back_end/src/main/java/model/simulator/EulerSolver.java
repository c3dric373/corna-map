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
        List<Double> result = cauchyProblem.getInitialConditions();
        final List<Function<List<Double>, Double>> system = cauchyProblem.getSystem();
        double delta = 1. / nbIterations;
        for(int i = 0; i < nbIterations; ++i){
            result = updateResult(system, result, delta);
        }
        return result;
    }

    /**
     *
     * @param system
     * @param result
     * @param delta
     * @return
     */
    private List<Double> updateResult(final List<Function<List<Double>, Double>> system, final List<Double> result, final double delta){
        Validate.notNull(system);
        Validate.notNull(result);
        List<Double> nextResult = new ArrayList<Double>(result.size());
        int i = 0;
        for(final Function<List<Double>, Double> function : system){
            Double nextValue = result.get(i++) + delta * function.apply(result);
            nextResult.add(nextValue);
        }
        return nextResult;
    }
}
