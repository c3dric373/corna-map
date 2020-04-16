package controller;

/**
 * ntm.
 */
public class MessageBuilder {
  /**
   * ntm.
   * @param name ntm.
   * @return ntm
   * */
  public String getMessage(final String name) {

    StringBuilder result = new StringBuilder();

    if (name == null || name.trim().length() == 0) {

      result.append("Please provide a name!");

    } else {

      result.append("Hello " + name);

    }
    return result.toString();
  }

}
