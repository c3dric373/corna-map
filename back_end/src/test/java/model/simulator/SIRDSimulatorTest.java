package model.simulator;

import com.google.common.collect.Iterables;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class SIRDSimulatorTest {

  private final static double MU = 0.05;
  private final static double LAMBDA = 0.2;
  private final static double BETA = 0.5;
  private final static double SUSCEPTIBLE = 0.9;
  private final static double DEAD = 0;
  private final static double RECOVERED = 0;
  private final static int NB_AGE_CATEGORIES = 1;
  private final static double INFECTIOUS = 1 - SUSCEPTIBLE;
  private static SIRDSimulator subject;

  @BeforeClass
  public static void setUp() {

    subject = new SIRDSimulator(Collections.singletonList(SUSCEPTIBLE),
      Collections.singletonList(INFECTIOUS),
      Collections.singletonList(RECOVERED),
      Collections.singletonList(DEAD), NB_AGE_CATEGORIES);
    subject.setMu(Collections.singletonList(MU));
    subject.setBeta(Collections.singletonList(BETA));
    subject.setGamma(LAMBDA);
    subject.setNbIterations(1000);
  }

  @Test
  public void testStep_twoStepsValidCall_resultsInDelta() {
    // Arrange
    // Calculate approximate values with
    // https://interstices.info/modeliser-la-propagation-dune-epidemie/
    final double expectedSusceptibleStep1 = 0.85206;
    final double expectedSusceptibleStep2 = 0.7982;
    final double expectedInfectiousStep1 = 0.12065;
    final double expectedInfectiousStep2 = 0.142;
    final double expectedRecoveredStep1 = 0.0218;
    final double expectedRecoveredStep2 = 0.0479;
    final double expectedDeadStep1 = 1 - 0.995;
    final double expectedDeadStep2 = 1 - 0.9887;
    final int step1Index = 1;
    final double delta = 0.001;

    // Act
    subject.step();
    subject.step();

    // Assert
    final List<Double> dead = subject.getDead().get(0);
    final List<Double> infectious = subject.getInfectious().get(0);
    final List<Double> recovered = subject.getRecovered().get(0);
    final List<Double> susceptible = subject.getSusceptible().get(0);
    final double deadStep1 = dead.get(step1Index);
    final double deadStep2 = Iterables.getLast(dead);
    final double recoveredStep1 = recovered.get(step1Index);
    final double recoveredStep2 = Iterables.getLast(recovered);
    final double susceptibleStep1 = susceptible.get(step1Index);
    final double susceptibleStep2 = Iterables.getLast(susceptible);
    final double infectiousStep1 = infectious.get(step1Index);
    final double infectiousStep2 = Iterables.getLast(infectious);

    Assert.assertEquals("wrong susceptible", expectedSusceptibleStep1,
      susceptibleStep1, delta);
    Assert.assertEquals("wrong susceptible2", expectedSusceptibleStep2,
      susceptibleStep2, delta);
    Assert.assertEquals("wrong recovered", expectedRecoveredStep1,
      recoveredStep1, delta);
    Assert.assertEquals("wrong recovered", expectedRecoveredStep2,
      recoveredStep2, delta);
    Assert.assertEquals("wrong dead", expectedDeadStep1, deadStep1,
      delta);
    Assert.assertEquals("wrong dead2", expectedDeadStep2, deadStep2,
      delta);
    Assert.assertEquals("wrong infectious", expectedInfectiousStep1,
      infectiousStep1,
      delta);
    Assert.assertEquals("wrong infectious2", expectedInfectiousStep2,
      infectiousStep2,
      delta);
  }

  @Test
  public void testSetModel_validCall_setCorrectModel() {
    // Arrange
    final double param = 2.3;
    final CauchyProblem expectedModel = CauchyProblem.builder()
      .addParameter(param,
        T -> -BETA * T.getYi(0) * T.getYi(1))
      .addParameter(param, T -> BETA * T.getYi(0) * T.getYi(1)
        - LAMBDA * T.getYi(1)
        - MU * T.getYi(1))
      .addParameter(param,
        T -> LAMBDA * T.getYi(1))
      .addParameter(DEAD,
        T -> MU * T.getYi(1))
      .build();

    // Act
    subject.setModel(expectedModel);

    // Assert
    final CauchyProblem actualModel = subject.getModel();
    Assert.assertEquals("wrong CauchyProblem", actualModel, expectedModel);

  }

}
