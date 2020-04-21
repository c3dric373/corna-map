package model.data;

import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.time.LocalDate;

/**
 * This class is a data container class which holds all the necessary
 * information about the COVID-19 cases for a given day and region, county in
 * france or france itself.
 */
@Getter
public class DayData {
  /**
   * Default constructor of DayData class.
   */
  public DayData() {
  }

  /**
   * wip.
   */
  private String id;
  /**
   * wip.
   */
  private String nom;
  /**
   * wip.
   */
  private Integer criticalCases;
  /**
   * wip.
   */
  private Integer hospitalized;
  /**
   * wip.
   */
  private Integer totalCases;
  /**
   * wip.
   */
  private Integer ephadCases;
  /**
   * wip.
   */
  private Integer ephadConfirmedCases;
  /**
   * wip.
   */
  private Integer ephadPossibleCases;
  /**
   * wip.
   */
  private Integer totalDeaths;
  /**
   * wip.
   */
  private Integer totalEphadDeaths;
  /**
   * wip.
   */
  private Integer recoveredCases;
  /**
   * wip.
   */
  private Integer totalTests;
  /**
   * wip.
   */
  private LocalDate date;
  /**
   * wip.
   */
  private TypeLocalisation type;

  /**
   * Constructor. All the number arguments are the numbers of people in a
   * given category related to the covid-19.
   *
   * @param typeNew                Type of the data it represents, i.e region
   *                               county or country.
   * @param dateNew                Date of data.
   * @param idNew                  Id of the type of data.
   * @param nomNew                 Name.
   * @param totalCasesNew          Total infected cases.
   * @param ephadCasesNew          Cases in ephad.
   * @param ephadConfirmedCasesNew Confirmed cases in ephad.
   * @param ephadPossibleCasesNew  Possible cases in ephad.
   * @param totalDeathsNew         Total deaths.
   * @param totalEphadDeathsNew    Total deaths in ephad facilities.
   * @param criticalCasesNew       Number of critical cases.
   * @param hospitalizedNew        Number of hospitalized cases.
   * @param recoveredCasesNew      Number of recovered cases.
   * @param totalTestsNew          Number of tests done in this location.
   */
  public DayData(final TypeLocalisation typeNew, final LocalDate dateNew,
                 final String idNew,
                 final String nomNew, final int totalCasesNew,
                 final int ephadCasesNew,
                 final int ephadConfirmedCasesNew,
                 final int ephadPossibleCasesNew,
                 final int totalDeathsNew,
                 final int totalEphadDeathsNew,
                 final int criticalCasesNew,
                 final int hospitalizedNew,
                 final int recoveredCasesNew,
                 final int totalTestsNew) {
    Validate.notNull(typeNew, "typeNew null");
    Validate.notNull(dateNew, "dateNew null");
    Validate.notNull(idNew, "idNew null");
    Validate.notEmpty(idNew, "idNew empty");
    Validate.notNull(nomNew, "nomNew null");
    Validate.notEmpty(nomNew, "nomNew empty");
    Validate.isTrue(ephadCasesNew >= 0,
      "ephadCasesNew negative");
    Validate.isTrue(totalCasesNew >= 0,
      "totalCasesNew negative");
    Validate.isTrue(ephadConfirmedCasesNew >= 0,
      "ephadConfirmedCasesNew " +
        "negative");
    Validate.isTrue(totalDeathsNew >= 0,
      "totalDeathsNew negative");
    Validate.isTrue(ephadPossibleCasesNew >= 0,
      "ephadPossibleCasesNew " +
        "negative");
    Validate.isTrue(totalEphadDeathsNew >= 0,
      "totalEphadDeathsNew negative");
    Validate.isTrue(criticalCasesNew >= 0,
      "criticalCasesNew negative");
    Validate.isTrue(hospitalizedNew >= 0,
      "hospitalizedNew negative");
    Validate.isTrue(recoveredCasesNew >= 0,
      "recoveredCasesNew negative");
    Validate.isTrue(totalTestsNew >= 0,
      "totalTestsNew negative");
    this.id = idNew;
    this.nom = nomNew;
    this.criticalCases = criticalCasesNew;
    this.hospitalized = hospitalizedNew;
    this.totalCases = totalCasesNew;
    this.ephadCases = ephadCasesNew;
    this.ephadConfirmedCases = ephadConfirmedCasesNew;
    this.ephadPossibleCases = ephadPossibleCasesNew;
    this.totalDeaths = totalDeathsNew;
    this.totalEphadDeaths = totalEphadDeathsNew;
    this.recoveredCases = recoveredCasesNew;
    this.totalTests = totalTestsNew;
    this.date = dateNew;
    this.type = typeNew;
  }
}
