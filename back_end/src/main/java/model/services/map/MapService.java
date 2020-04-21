package model.services.map;

import model.data.DayData;
import model.project.ProjectData;
import model.project.ProjectDataWrapper;

import java.time.LocalDate;
import java.util.NoSuchElementException;

public class MapService {

  static DayData infosFrance(final ProjectDataWrapper projectDataWrapper,
                             final String date) {
    final LocalDate requestDate = LocalDate.parse(date);
    final ProjectData projectData = projectDataWrapper.getProjectData();
    for (DayData dayData : projectData.getFrance()) {
      if (dayData.getDate().equals(requestDate)) {
        return dayData;
      }
    }
    throw new NoSuchElementException("No Data for this specific date: " + date);
  }

}
