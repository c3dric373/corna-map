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
public class RungeKuttaSolver implements DifferentialSolver {
    protected int order;
    protected int nbIterations;
    protected List<List<Double>> a;
    protected List<Double> b;
    protected List<Double> c;

    public List<Double> next(final CauchyProblem cauchyProblem, final int nbIterations) {
        Validate.notNull(cauchyProblem);
        TY ty = new TY(cauchyProblem.getT0(), cauchyProblem.getY0());
        final List<Function<TY, Double>> f = cauchyProblem.getF();
        this.nbIterations = nbIterations;

        for (int i = 0; i < nbIterations; ++i) {
            ty = updateResult(f, ty);
        }
        return ty.getY();
    }

    private TY updateResult(final List<Function<TY, Double>> f, final TY ty) {
        Validate.notNull(f);
        Validate.notNull(ty);
        final double h = 1. / nbIterations;
        TY tnyn = new TY();
        tnyn.setT(ty.getT() + h);
        int i = 0;
        for (final Function<TY, Double> f_i : f) {
            Double nextValue = makeNextValue(ty, f_i, i++);
            tnyn.add(nextValue);
        }
        return tnyn;
    }

    private Double makeNextValue(final TY ty, final Function<TY, Double> f_i, final int i) {
        final double h = 1. / nbIterations;
        List<Double> k = makeK(ty, f_i);
        double sum = 0;
        for (int j = 0; j < order; ++j) {
            sum += b.get(j) * k.get(j);
        }
        return ty.getYi(i) + h * sum;
    }

    private List<Double> makeK(final TY ty, final Function<TY, Double> f_i) {
        final double h = 1. / nbIterations;
        List<Double> k = new ArrayList<>(order);
        TY tmp = new TY();
        for (int i = 0; i < order; ++i) {
            tmp.setT(ty.getT() + c.get(i) * h);
            tmp.setY(auxMakeK(k, i, ty));
            k.add(f_i.apply(tmp));
        }
        return k;
    }

    private List<Double> auxMakeK(final List<Double> k, final int i, final TY tnyn) {
        final double h = 1. / nbIterations;
        double sum = 0;
        for (int j = 0; j < i; ++j) {
            sum += a.get(i).get(j) * k.get(j);
        }

        double finalSum = sum;
        return tnyn.getY().stream()
                .map(yn -> yn + h * finalSum)
                .collect(Collectors.toList());
    }
}
