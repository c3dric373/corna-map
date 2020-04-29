package model.simulator;

import java.util.List;

public interface DifferentialSolver {
//    List<List<Double>> solve(final CauchyProblem cauchyProblem);

    List<Double> next(final CauchyProblem cauchyProblem, final int nbIterations);
}
