package model.simulator;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
/**
 * The Runge-Kutta method is an improvement of the Euler method to solve
 * differential equations.
 *
 * Each RK method of order s is defined by its Butcher tableau
 *
 * c_1 | a_11 a_12 ... a_1s
 * c_2 | a_21 a_22 ... a_2s
 * ... | ...  ...  ... ...
 * c_s | a_s1 a_s2 ... a_ss
 * -------------------------
 *     | b_1  b_2  ... b_s
 *
 * Now given a Cauchy problem y'(t) = f(t, y(t)) and y(t_0) = y_0, the
 * Runge-Kutta methods works as follows :
 *
 * We choose a step size h and define for n in N t_n = t_0 + n h.
 * Then we want to compute y_n, an approximation of y(t_n).
 * To do this, we start from y_0 (which is known) and compute y_n+1 as follows
 * For i in [|1, p|], the i-th component of y_n+1
 *
 * y_n+1i = y_ni + h (b_1 k_i1 + ... + b_s k_is)
 *
 * where the k_i vector is defined for j in [|1, s|] by
 *
 * k_ij = f_i(t_n + c_j h, y_n + h (a_j1 ki1 + ... + a_j(j-1) k_ij-1))
 */
public class RungeKuttaSolver implements DifferentialSolver {
    /**
     * The order of the RK method, named s in the description above.
     */
    protected int order;
    /**
     * The number of iterations we want to make.
     */
    protected int n;
    /**
     * the a_ij of the Butcher tableau.
     */
    protected List<List<Double>> a;
    /**
     * the b_i of the Butcher tableau.
     */
    protected List<Double> b;
    /**
     * the c_i of the Butcher tableau.
     */
    protected List<Double> c;

    /**
     * Computes y(t_0 + 1) where y is the solution of the Cauchy problem y'(t) =
     * f(t, y(t)) and y(t_0) = y_0.
     *
     * @param cauchyProblem the Cauchy problem we want to solve.
     * @param nbIterations  the number of intermediate values we will compute.
     *                      Hence, h = 1 / nbIterations.
     *
     * @return an array of p elements that represents y(t_0 + 1).
     */
    public List<Double> next(final CauchyProblem cauchyProblem,
                             final int nbIterations) {
        Validate.notNull(cauchyProblem);
        TY ty = cauchyProblem.getInitialCondition();
        final List<Function<TY, Double>> f = cauchyProblem.getF();
        n = nbIterations;

        for (int i = 0; i < nbIterations; ++i) {
            ty = computeNextStep(f, ty);
        }
        return ty.getY();
    }

    /**
     * Computes t_n+1 and y_n+1 given t_n, y_n and a differential equation y'(t)
     * = f(t, y(t)).
     *
     * @param f  the function in the Cauchy problem.
     * @param ty the representation of t_n and y_n.
     *
     * @return the represention of t_n+1 and y_n+1.
     */
    private TY computeNextStep(final List<Function<TY, Double>> f,
                               final TY ty) {
        Validate.notNull(f);
        Validate.notNull(ty);
        final double h = 1. / n;
        TY nextTY = new TY();
        nextTY.setT(ty.getT() + h);
        int i = 0; // will go from 0 to p - 1
        for (final Function<TY, Double> fi : f) {
            double nextValue = computeNextIthStep(fi, ty, i++);
            nextTY.add(nextValue);
        }
        return nextTY;
    }

    /**
     * Computes the i-th component of y_n+1 given t_n, y_n and a differential
     * equation y'(t) = f(t, y(t)).
     *
     * @param fi the i-th component of f.
     * @param ty the representation of t_n and y_n.
     * @param i  the index component we are interested in.
     *
     * @return the i-th component of y_n+1.
     */
    private Double computeNextIthStep(final Function<TY, Double> fi,
                                      final TY ty,
                                      final int i) {
        Validate.notNull(fi);
        Validate.notNull(ty);
        final double h = 1. / n;
        List<Double> ki = computeKi(fi, ty);
        double sum = 0;
        for (int j = 0; j < order; ++j) {
            sum += b.get(j) * ki.get(j);
        }
        return ty.getYi(i) + h * sum;
    }

    /**
     * Computes the k_i as defined above.
     *
     * @param fi the i-th component of f.
     * @param ty the representation of t_n and y_n.
     *
     * @return the representation k_i.
     */
    private List<Double> computeKi(final Function<TY, Double> fi,
                                   final TY ty) {
        Validate.notNull(fi);
        Validate.notNull(ty);
        final double h = 1. / n;
        List<Double> ki = new ArrayList<>(order);
        TY tmp = new TY();
        for (int j = 0; j < order; ++j) {
            tmp.setT(ty.getT() + c.get(j) * h);
            tmp.setY(auxComputeKi(ki, j, ty));
            ki.add(fi.apply(tmp));
        }
        return ki;
    }

    /**
     * Computes y_n + h (a_j1 ki1 + ... + a_j(j-1) k_ij-1) in order to make
     * f_i(t_n + c_j h, y_n + h (a_j1 k1 + ... + a_j(j-1) k_ij-1)) next.
     *
     * @param ki the values we already computed (from 0 to j - 1).
     * @param j the component we are interested in.
     * @param ty the representation of tn and yn.
     *
     * @return the representation of y_n + h (a_j1 ki1 + ... + a_j(j-1) k_ij-1)
     */
    private List<Double> auxComputeKi(final List<Double> ki,
                                      final int j,
                                      final TY ty) {
        final double h = 1. / n;
        double sum = 0;
        for (int l = 0; l < j; ++l) {
            sum += a.get(j).get(l) * ki.get(l);
        }

        double finalSum = sum;
        return ty.getY().stream()
                .map(y -> y + h * finalSum)
                .collect(Collectors.toList());
    }
}
