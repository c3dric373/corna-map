package model.simulator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CauchyProblemTest {

  private static CauchyProblem subject;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @BeforeClass
  public static void stUp() {
    final Double beta = 0.1;
    final Double lambda = 0.1;
    final Double mu = 0.001;
    subject = CauchyProblem.builder()
      .addParameter(0.9, T -> 2.)
      .addParameter(0.1,
        T -> beta * T.getYi(0) * T.getYi(1) - lambda * T.getYi(1) - mu * T.getYi(1))
      .addParameter(0., T -> lambda * T.getYi(1))
      .addParameter(0., T -> mu * T.getYi(1))
      .build();
  }

  @Test
  public void testCTOR_derivateNull_throwsIllegalArgumentException() {

    // Arrange
    final Double beta = 0.1;
    final Double lambda = 0.1;
    final Double mu = 0.001;
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("derivative null");

    // Act
    final CauchyProblem CP = CauchyProblem.builder()
      .addParameter(0.9, null)
      .addParameter(0.1,
        T -> beta * T.getYi(0) * T.getYi(1) - lambda * T.getYi(1) - mu * T.getYi(1))
      .addParameter(0., T -> lambda * T.getYi(1))
      .addParameter(0., T -> mu * T.getYi(1))
      .build();

    // Assert via annotation
  }

  @Test
  public void testSetF_validCall_correctSet() {
    // Arrange
    final Double lambda = 0.1;
    List<Function<TY, Double>> f = new ArrayList<>();
    f.add(T -> lambda * T.getYi(1));
    // Act
    System.out.println(f);
    subject.setF(f);

    // Assert
    List<Function<TY, Double>> actualF = subject.getF();
    Assert.assertEquals("wrong f", actualF, f);
  }

  @Test
  public void testSetT0_validCall_correctSet() {

    // Arrange
    final double lambda = 0.1;
    final double delta = 0.;

    // Act
    subject.setT0(lambda);

    // Assert
    final double actualT0 = subject.getT0();
    Assert.assertEquals("wrong f", actualT0, lambda, delta);
  }

  @Test
  public void testSetY0_validCall_correctSet() {

    // Arrange
    final double lambda = 0.1;
    final double delta = 0.;
    final List<Double> expected = new ArrayList<>(2);
    expected.add(lambda);
    expected.add(delta);

    // Act
    subject.setY0(expected);

    // Assert
    final List<Double> actualY0 = subject.getY0();
    Assert.assertEquals("wrong f", actualY0, expected);
  }
}
