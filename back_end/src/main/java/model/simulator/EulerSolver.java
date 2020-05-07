package model.simulator;

import org.apache.commons.lang.Validate;

import java.util.List;
import java.util.function.Function;

public class EulerSolver implements DifferentialSolver {
  /**
   * Given a Cauchy problem with an initial time t_0, this function calculates
   * y(t_0 + 1) where y is the solution of the Cauchy problem, using the Euler
   * method.
   *
   * @param cauchyProblem the Cauchy problem we want to solve
   * @param nbIterations  the precision we want to have, we will compute all
   *                      the y(t_0 + k h) for all k between 1 and nbIteration
   *                      and h = 1 / nbIteration
   * @return the result y(t_0 + 1), approximated by the Euler method
   */
  @Override
  public List<Double> next(final CauchyProblem cauchyProblem,
                           final int nbIterations) {
    Validate.notNull(cauchyProblem);
    TY ty = new TY(cauchyProblem.getT0(), cauchyProblem.getY0());
    final List<Function<TY, Double>> f = cauchyProblem.getF();
    double h = 1. / nbIterations;

    for (int i = 0; i < nbIterations; ++i) {
      ty = updateResult(f, ty, h);
    }
    return ty.getY();
  }

  /**
   * Updates t and y(t) making one iteration of the Euler method t becomes
   * t + h and y(t) becomes y(t + h).
   *
   * @param f  the derivatives of the differential system
   * @param ty t and y(t)
   * @param h  the step size
   * @return t + h and y(t + h), approximated by the Euler method
   */
  private TY updateResult(final List<Function<TY, Double>> f,
                          final TY ty,
                          final double h) {
    Validate.notNull(f);
    Validate.notNull(ty);
    TY newty = new TY();
    newty.setT(ty.getT() + h);
    int i = 0;
    for (final Function<TY, Double> function : f) {
      Double nextValue = ty.getYi(i++) + h * function.apply(ty);
      newty.add(nextValue);
    }
    return newty;
  }

  /**
   * Empty Constructor.
   */
  public EulerSolver() {

  }
}
