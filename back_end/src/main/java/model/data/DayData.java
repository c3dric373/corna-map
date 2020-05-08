package model.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.Validate;

import java.time.LocalDate;

/**
 * This class is a data container class which holds all the necessary
 * information about the COVID-19 cases for a given day and region, county in
 * france or france itself.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DayData {

  /**
   * Id of location of data.
   */
  private String id;
  /**
   * Name of location.
   */
  private String name;
  /**
   * Number of critical cases.
   */
  private int criticalCases;
  /**
   * Number of hospitalized cases.
   */
  private int hospitalized;
  /**
   * Number of total cases.
   */
  private int totalCases;
  /**
   * Number of ephad cases.
   */
  private int ephadCases;
  /**
   * Number of ephad confirmed cases.
   */
  private int ephadConfirmedCases;
  /**
   * Number of ephad Possible cases.
   */
  private int ephadPossibleCases;
  /**
   * Number of total detaths.
   */
  private int totalDeaths;
  /**
   * Number of total ephad deaths.
   */
  private int totalEphadDeaths;
  /**
   * Number of recovered cases.
   */
  private int recoveredCases;
  /**
   * Number of total Tests.
   */
  private int totalTests;
  /**
   * Date of the data..
   */
  private LocalDate date;

  /**
   * Empty Constructor.
   */
  public DayData() {
  }

  /**
   * Constructor. All the number arguments are the numbers of people in a
   * given category related to the covid-19.
   *
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
  public DayData(final LocalDate dateNew,
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
      "ephadConfirmedCasesNew negative");
    Validate.isTrue(totalDeathsNew >= 0,
      "totalDeathsNew negative");
    Validate.isTrue(ephadPossibleCasesNew >= 0,
      "ephadPossibleCasesNew negative");
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
    this.name = nomNew;
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
  }

}
