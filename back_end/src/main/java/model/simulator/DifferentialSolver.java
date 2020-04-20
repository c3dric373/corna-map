package model.simulator;

import java.util.List;

public interface DifferentialSolver {
//    List<List<Double>> solve(final CauchyProblem cauchyProblem);

    List<Double> step(final CauchyProblem cauchyProblem, final int nbIterations);

}
