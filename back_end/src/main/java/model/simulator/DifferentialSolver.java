package model.simulator;

import java.util.List;

/**
 * DifferentialSolver is an interface for all the numerical methods to solver
 * differential equations.
 */
public interface DifferentialSolver {
    /**
     * Given a Cauchy problem of order p y'(t) = f(t, y(t)) and y(t_0) = y_0,
     * computes y(t_0 + 1) where y is the solution to the Cauchy problem.
     *
     * @param cauchyProblem the Cauchy problem we want to solve.
     * @param nbIterations  the number of intermediary values we compute to make
     *                      the solution more precise.
     *
     * @return an array of p element that represents y(t_0 + 1)
     */
    List<Double> next(CauchyProblem cauchyProblem, int nbIterations);
}
