package model.io;


import java.io.IOException;

/**
 * Class which holds methods to get information about the coronavirus from
 * the web.
 */
public interface DataScrapper {

  /**
   * This procedure download the csv file directly from the french
   * government official dataset.
   * @throws IOException Read error.
   */
  void getCurrentDataFromWeb() throws IOException;

  Object extract() throws IOException;

}
