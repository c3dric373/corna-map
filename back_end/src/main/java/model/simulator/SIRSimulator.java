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

  private Double beta = 0.01;
  private Double lambda = 0.1;
  private Double mu = 0.07;

  private CauchyProblem model;
  private DifferentialSolver solver = new OldRK4Solver();
  private int nbIterations = 500;

  public SIRSimulator() {
  }

  public SIRSimulator(double iSuceptible, double iInfectious,
                      double iRecovered, double iDead) {
    susceptible = new ArrayList<>();
    infectious = new ArrayList<>();
    recovered = new ArrayList<>();
    dead = new ArrayList<>();
    susceptible.add(iSuceptible);
    infectious.add(iInfectious);
    recovered.add(iRecovered);
    dead.add(iDead);

    model = CauchyProblem.builder()
      .addParameter(iSuceptible, T -> -beta * T.getY_i(0) * T.getY_i(1))
      .addParameter(iInfectious,
        T -> beta * T.getY_i(0) * T.getY_i(1) - lambda * T.getY_i(1) - mu * T.getY_i(1))
      .addParameter(iRecovered, T -> lambda * T.getY_i(1))
      .addParameter(iDead, T -> mu * T.getY_i(1))
      .build();
  }

  public void step() {
    List<Double> nextValues = solver.next(model, nbIterations);
    susceptible.add(nextValues.get(0));
    infectious.add(nextValues.get(1));
    recovered.add(nextValues.get(2));
    dead.add(nextValues.get(3));

    model = CauchyProblem.builder()
      .addParameter(nextValues.get(0), T -> -beta * T.getY_i(0) * T.getY_i(1))
      .addParameter(nextValues.get(1),
        T -> beta * T.getY_i(0) * T.getY_i(1) - lambda * T.getY_i(1) - mu * T.getY_i(1))
      .addParameter(nextValues.get(2), T -> lambda * T.getY_i(1))
      .addParameter(nextValues.get(3), T -> mu * T.getY_i(1))
      .build();
  }

  public static void main(String[] args) {
    SIRSimulator SIRS = new SIRSimulator(0.9, 0.1, 0., 0.);
    long start = System.nanoTime();
    for (int i = 0; i < 28; ++i) {
      SIRS.step();
    }
    long end = System.nanoTime();

    System.out.println((end - start) * 0.000001);

    for (double param : SIRS.getSusceptible()) {
      System.out.print(param);
      System.out.print(", ");
    }
    System.out.println();
    for (double param : SIRS.getInfectious()) {
      System.out.print(param);
      System.out.print(" ");
    }
    System.out.println();
    for (double param : SIRS.getRecovered()) {
      System.out.print(param);
      System.out.print(" ");
    }
    System.out.println();
    for (double param : SIRS.getDead()) {
      System.out.print(param);
      System.out.print(" ");
    }
    System.out.println();
  }
}
