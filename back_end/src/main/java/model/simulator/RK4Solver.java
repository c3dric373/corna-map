package model.simulator;

import java.util.ArrayList;
import java.util.List;

public class RK4Solver extends RungeKuttaSolver {
    /**
     * Creates a differential solver for the RK4 method.
     * The RK4 method is a Runge-Kutta method with the following Butcher tableau
     *
     *  0  |  0   0   0   0
     * 1/2 | 1/2  0   0   0
     * 1/2 |  0  1/2  0   0
     *  1  |  0   0   1   0
     * -------------------------
     *     | 1/6 1/3 1/3 1/6
     *
     */
    public RK4Solver() {
        order = 4;
        a = new ArrayList<>(order);
        List<Double> a0 = new ArrayList<>(order);
        List<Double> a1 = new ArrayList<>(order);
        List<Double> a2 = new ArrayList<>(order);
        List<Double> a3 = new ArrayList<>(order);
        a0.add(0.);  a0.add(0.);  a0.add(0.);  a0.add(0.);
        a1.add(.5);  a1.add(0.);  a1.add(0.);  a1.add(0.);
        a2.add(0.);  a2.add(.5);  a2.add(0.);  a2.add(0.);
        a3.add(0.);  a3.add(0.);  a3.add(1.);  a3.add(0.);
        a.add(a0);
        a.add(a1);
        a.add(a2);
        a.add(a3);

        b = new ArrayList<>(order);
        b.add(1. / 6);
        b.add(1. / 3);
        b.add(1. / 3);
        b.add(1. / 6);

        c = new ArrayList<>(order);
        c.add(0.);
        c.add(0.5);
        c.add(0.5);
        c.add(1.);
    }
}
