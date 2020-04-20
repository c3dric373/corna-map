package model.simulator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

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
//        thrown.expectMessage("derivative null");

        // Act
        CauchyProblem CP = CauchyProblem.builder()
                .addParameter(0.9, null)
                .addParameter(0.1, T -> beta * T.get(0) * T.get(1) - lambda * T.get(1) - mu * T.get(1))
                .addParameter(0., T -> lambda * T.get(1))
                .addParameter(0., T -> mu * T.get(1))
                .build();


        // Assert via annotation
    }

}
