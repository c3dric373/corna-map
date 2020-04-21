package model.project;

import lombok.Getter;
import model.io.DataScrapperImpl;

import java.io.IOException;

/**
 * This class acts as a wrapper for the {@link ProjectData} object. It offers
 * convenient methods to add, modify and remove project to all
 * project types that are stored inside the ProjectData object.
 * Most methods making changes to the
 * project return a value which will be passed on to  the View about those
 * changes.
 */
@Getter
public class ProjectDataWrapperImpl implements ProjectDataWrapper {

  ProjectData projectData;

  @Override
  public ProjectDataImpl getCurrentAllDataFrance() throws IOException {
    DataScrapperImpl dataScrapper = new DataScrapperImpl();
    dataScrapper.getCurrentDataFromWeb();
    return dataScrapper.extract();
  }
}

