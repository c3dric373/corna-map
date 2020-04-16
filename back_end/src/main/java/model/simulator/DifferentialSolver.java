package model.simulator;

import java.util.List;

@FunctionalInterface
public interface DifferentialSolver {
    List<List<Double>> solve(final CauchyProblem CP);
}
