package model.data;

/**
 * Enum representing different types of lacalisations in france. Allows us to
 * have different granularities in the map.
 */
public enum TypeLocalisation {
  /**
   * County.
   */
  DEPARTEMENT,
  /**
   * Country.
   */
  PAYS,
  /**
   * Region.
   */
  REGION,
  /**
   * World.
   */
  MONDE;
}
