package model.io;

import model.project.ProjectDataWrapper;

import java.io.IOException;

/**
 * Class which holds methods to get information about the coronavirus from
 * the web.
 */
public interface DataScrapper {

  /**
   * This procedure download the csv file directly from the french
   * government official dataset.
   *
   * @throws IOException Read error.
   */
  void getCurrentDataFromWeb() throws IOException;

  /**
   * Extracts all the necessary data from a resource file which has to been
   * downloaded previously.
   *
   * @param projectDataWrapper the wrapper to which the data should be added.
  * @throws IOException Read error.
   */
  void extract(ProjectDataWrapper projectDataWrapper) throws IOException;

}
