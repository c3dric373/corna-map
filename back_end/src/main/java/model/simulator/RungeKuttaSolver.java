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
        T_Y t_y = new T_Y(cauchyProblem.getT_0(), cauchyProblem.getY_0());
        final List<Function<T_Y, Double>> f = cauchyProblem.getF();
        double h = 1. / nbIterations;

        for(int i = 0; i < nbIterations; ++i){
            t_y = updateResult(f, t_y, h);
        }
        return t_y.getY();
    }
    private T_Y updateResult(final List<Function<T_Y, Double>> f, final T_Y t_y, final double h){
        Validate.notNull(f);
        Validate.notNull(t_y);
        T_Y t_y_n = new T_Y();
        t_y_n.setT(t_y.getT() + h);
        int i = 0;
        for(final Function<T_Y, Double> f_i : f){
            Double nextValue = makeNextValue(t_y, f_i, h, i++);
            t_y_n.add(nextValue);
        }
        return t_y_n;
    }

    private Double makeNextValue(final T_Y t_y, final Function<T_Y, Double> f_i, double h, int i){
        List<Double> k = makeK(t_y, f_i);
        double sum = 0;
        for(int j = 0; j < order; ++j){
            sum += b.get(j) * k.get(j);
        }
        return t_y.getY_i(i) + h * sum;
    }

    private List<Double> makeK(final T_Y t_y, final Function<T_Y, Double> f_i) {
        List<Double> k = new ArrayList<>(order);
        double h = 1. / nbIterations;
        T_Y tmp = new T_Y();
        for (int i = 0; i < order; ++i) {
            tmp.setT(t_y.getT() + c.get(i) * h);
            tmp.setY(auxMakeK(k, i, t_y));
            k.add(f_i.apply(tmp));
        }
        return k;
    }

    private List<Double> auxMakeK(final List<Double> k, final int i, final T_Y t_y_n) {
        double h = 1. / nbIterations;
        double sum = 0;
        for (int j = 0; j < i; ++j) {
            sum += a.get(i).get(j) * k.get(j);
        }

        double finalSum = sum;
        return t_y_n.getY().stream()
                .map(yn -> yn + h * finalSum)
                .collect(Collectors.toList());
    }
}
