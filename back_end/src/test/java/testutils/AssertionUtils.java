package testutils;

public class AssertionUtils {

  public static void superficialEnumCodeCoverage(Class<? extends Enum<?>> enumClass) {
    try {
      for (Object o : (Object[]) enumClass.getMethod("values").invoke(null)) {
        enumClass.getMethod("valueOf", String.class).invoke(null, o.toString());
      }
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
