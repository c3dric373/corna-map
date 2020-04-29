package model.simulator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CauchyProblemTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCTOR_derivateNull_throwsIllegalArgumentException() {

        // Arrange
        final Double beta = 0.1;
        final Double lambda = 0.1;
        final Double mu = 0.001;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("derivative null");

        // Act
        CauchyProblem CP = CauchyProblem.builder()
                .addParameter(0.9, null)
                .addParameter(0.1, T -> beta * T.getYi(0) * T.getYi(1) - lambda * T.getYi(1) - mu * T.getYi(1))
                .addParameter(0., T -> lambda * T.getYi(1))
                .addParameter(0., T -> mu * T.getYi(1))
                .build();


        // Assert via annotation
    }

}
