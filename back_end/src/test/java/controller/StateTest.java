package controller;

import org.junit.Test;
import testutils.AssertionUtils;

public class StateTest {
  @Test
  public void testAllGeneratedMethods_noErrors() {

    // Arrange

    // Act
    AssertionUtils.superficialEnumCodeCoverage(State.class);

    // Assert
  }
}
