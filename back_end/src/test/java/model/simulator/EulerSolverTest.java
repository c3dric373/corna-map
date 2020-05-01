package model.simulator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class EulerSolverTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testStep_constantFunction_correctResult() {

        // Arrange
        CauchyProblem cauchyProblem = CauchyProblem.builder()
                .addParameter(4., T -> 0.)
                .build();
        EulerSolver subject = new EulerSolver();
        int nbIterations = 10;
        List<Double> expected = new ArrayList<>(1);
        expected.add(4.);

        // Act
        List<Double> candidate = subject.next(cauchyProblem, nbIterations);

        // Assert
        assertEquals("wrong result", expected, candidate);
    }

    @Test
    public void testStep_threeConstantFunction_correctResult() {

        // Arrange
        CauchyProblem cauchyProblem = CauchyProblem.builder()
                .addParameter(4., T -> 0.)
                .addParameter(5., T -> 0.)
                .addParameter(6., T -> 0.)
                .build();
        EulerSolver subject = new EulerSolver();
        int nbIterations = 10;
        List<Double> expected = new ArrayList<>(1);
        expected.add(4.);
        expected.add(5.);
        expected.add(6.);

        // Act
        List<Double> candidate = subject.next(cauchyProblem, nbIterations);

        // Assert
        assertEquals("mismatch sizes", expected.size(), candidate.size());
        assertEquals("wrong result", expected, candidate);
    }

    @Test
    public void testStep_linearFunction_correctResult() {

        // Arrange
        CauchyProblem cauchyProblem = CauchyProblem.builder()
                .addParameter(4., T -> 2.)
                .build();
        EulerSolver subject = new EulerSolver();
        int nbIterations = 10;
        List<Double> expected = new ArrayList<>(1);
        expected.add(6.);

        // Act
        List<Double> candidate = subject.next(cauchyProblem, nbIterations);

        // Assert
        assertEquals("mismatch sizes", expected.size(), candidate.size());
        for(int i = 0; i < expected.size(); ++i){
            assertEquals("wrong " + i + "-th result", expected.get(i), candidate.get(i), 0.000001);
        }
    }

    @Test
    public void testStep_multipleLinearFunction_correctResult() {

        // Arrange
        CauchyProblem cauchyProblem = CauchyProblem.builder()
                .addParameter(-6., T -> 2.)
                .addParameter(17., T -> -1.)
                .addParameter(-40., T -> 3.)
                .addParameter(7., T -> -15.)
                .addParameter(10., T -> 4.)
                .addParameter(-20., T -> -21.)
                .addParameter(24., T -> 5.)
                .build();
        EulerSolver subject = new EulerSolver();
        int nbIterations = 10;
        List<Double> expected = new ArrayList<>(1);
        expected.add(-4.);
        expected.add(16.);
        expected.add(-37.);
        expected.add(-8.);
        expected.add(14.);
        expected.add(-41.);
        expected.add(29.);

        // Act
        List<Double> candidate = subject.next(cauchyProblem, nbIterations);

        // Assert
        assertEquals("mismatch sizes", expected.size(), candidate.size());
        for(int i = 0; i < expected.size(); ++i){
            assertEquals("wrong " + i + "-th result", expected.get(i), candidate.get(i), 0.000001);
        }
    }

    @Test
    public void testStep_exponentialOneIteration_correctResult() {

        // Arrange
        CauchyProblem cauchyProblem = CauchyProblem.builder()
                .addParameter(1., T -> T.getYi(0))
                .build();
        EulerSolver subject = new EulerSolver();
        int nbIterations = 1;
        List<Double> expected = new ArrayList<>(1);
        expected.add(2.);

        // Act
        List<Double> candidate = subject.next(cauchyProblem, nbIterations);

        // Assert
        assertEquals("mismatch sizes", expected.size(), candidate.size());
        for(int i = 0; i < expected.size(); ++i){
            assertEquals("wrong " + i + "-th result", expected.get(i), candidate.get(i), 0.000001);
        }
    }

    @Test
    public void testStep_exponentialTwoIterations_correctResult() {

        // Arrange
        CauchyProblem cauchyProblem = CauchyProblem.builder()
                .addParameter(1., T -> T.getYi(0))
                .build();
        EulerSolver subject = new EulerSolver();
        int nbIterations = 2;
        List<Double> expected = new ArrayList<>(1);
        expected.add(2.25);

        // Act
        List<Double> candidate = subject.next(cauchyProblem, nbIterations);

        // Assert
        assertEquals("mismatch sizes", expected.size(), candidate.size());
        for(int i = 0; i < expected.size(); ++i){
            assertEquals("wrong " + i + "-th result", expected.get(i), candidate.get(i), 0.000001);
        }
    }

    @Test
    public void testStep_exponentialHundredIterations_correctResult() {

        // Arrange
        CauchyProblem cauchyProblem = CauchyProblem.builder()
                .addParameter(1., T -> T.getYi(0))
                .build();
        EulerSolver subject = new EulerSolver();
        int nbIterations = 100;
        List<Double> expected = new ArrayList<>(1);
        /*
        y0 = 1
        for i in range(100):
            y0 += 0.01 * y0
         */
        expected.add(2.704813829421526);

        // Act
        List<Double> candidate = subject.next(cauchyProblem, nbIterations);

        // Assert
        assertEquals("mismatch sizes", expected.size(), candidate.size());
        for(int i = 0; i < expected.size(); ++i){
            assertEquals("wrong " + i + "-th result", expected.get(i), candidate.get(i), 0.000001);
        }
    }
}
