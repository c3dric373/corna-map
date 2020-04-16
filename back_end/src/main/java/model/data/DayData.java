package model.data;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class DayData {
    /**
     * Default constructor of DayData class
     */
    public DayData() {
    }


    public DayData(TypeLocalisation type ,LocalDate date,String id,String nom, Integer totalCases, Integer ephadCases, Integer ephadConfirmedCases, Integer ephadPossibleCases, Integer totalDeaths,Integer totalEphadDeaths, Integer criticalCases, Integer hospitalized, Integer recoveredCases, Integer totalTests) {
        this.id = id;
        this.nom=nom;
        this.criticalCases = criticalCases;
        this.hospitalized = hospitalized;
        this.totalCases = totalCases;
        this.ephadCases = ephadCases;
        this.ephadConfirmedCases = ephadConfirmedCases;
        this.ephadPossibleCases = ephadPossibleCases;
        this.totalDeaths = totalDeaths;
        this.totalEphadDeaths=totalEphadDeaths;
        this.recoveredCases = recoveredCases;
        this.totalTests = totalTests;
        this.date = date;
        this.type = type;
    }

    private String id;
    private String nom;
    private Integer criticalCases;
    private Integer hospitalized;
    private Integer totalCases;
    private Integer ephadCases;
    private Integer ephadConfirmedCases;
    private Integer ephadPossibleCases;
    private Integer totalDeaths;
    private Integer totalEphadDeaths;
    private Integer recoveredCases;
    private Integer totalTests;
    private LocalDate date;
    private TypeLocalisation type;
}
