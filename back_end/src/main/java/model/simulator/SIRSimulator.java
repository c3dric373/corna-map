package model.simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SIRSimulator implements Simulator {
    private List<Double> susceptible;
    private List<Double> infectious;
    private List<Double> recovered;
    private List<Double> dead;

    private Double beta = 0.3;
    private Double lambda = 0.04;
    private Double mu = 0.07;

    private CauchyProblem model;
    private DifferentialSolver solver = new RK4Solver();
    private int nbIterations = 500;

    public SIRSimulator(double iSuceptible, double iInfectious, double iRecovered, double iDead){
        susceptible = new ArrayList<>();
        infectious = new ArrayList<>();
        recovered = new ArrayList<>();
        dead = new ArrayList<>();
        susceptible.add(iSuceptible);
        infectious.add(iInfectious);
        recovered.add(iRecovered);
        dead.add(iDead);

        model = CauchyProblem.builder()
                .addParameter(iSuceptible, T -> - beta * T.getYi(0) * T.getYi(1))
                .addParameter(iInfectious, T -> beta * T.getYi(0) * T.getYi(1) - lambda * T.getYi(1) - mu * T.getYi(1))
                .addParameter(iRecovered, T -> lambda * T.getYi(1))
                .addParameter(iDead, T -> mu * T.getYi(1))
                .build();
    }

    public void step(){
        List<Double> nextValues = solver.next(model, nbIterations);
        susceptible.add(nextValues.get(0));
        infectious.add(nextValues.get(1));
        recovered.add(nextValues.get(2));
        dead.add(nextValues.get(3));

        model = CauchyProblem.builder()
                .addParameter(nextValues.get(0), T -> - beta * T.getYi(0) * T.getYi(1))
                .addParameter(nextValues.get(1), T -> beta * T.getYi(0) * T.getYi(1) - lambda * T.getYi(1) - mu * T.getYi(1))
                .addParameter(nextValues.get(2), T -> lambda * T.getYi(1))
                .addParameter(nextValues.get(3), T -> mu * T.getYi(1))
                .build();
    }
}
