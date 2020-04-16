package model.io;

import java.io.IOException;

public interface DataScrapper {
  /**
   * Read error @throws IOException
   */
  void getCurrentDataFromWeb() throws IOException;

}
