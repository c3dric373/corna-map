package model.service;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulatorService {

  /**
   * Map ageCategories as String to it's index in the simulator.
   */
  Map<String, Integer> ageCatToIndex = new HashMap<>();
  /**
   * Name ageCat 0-15.
   */
  private static final String M_0_15 = "m0_15";

  /**
   * Name ageCat 15-44.
   */
  private static final String M_15_44 = "m15_44";

  /**
   * Name ageCat 44-64.
   */
  private static final String M_44_64 = "m44_64";
  /**
   * Name ageCat 64-75.
   */
  private static final String M_64_75 = "m64_75";

  /**
   * Name ageCat 75+.
   */
  private static final String M_75_INF = "m75";

  /**
   * Key of the degree that people respect the confinement.
   */
  private static final String RESPECT_CONF = "respectConfinement";

  /**
   * Empty Constructor. Initialize Map.
   */
  public SimulatorService() {
    ageCatToIndex.put(M_0_15, 0);
    ageCatToIndex.put(M_15_44, 1);
    ageCatToIndex.put(M_44_64, 2);
    ageCatToIndex.put(M_64_75, 3);
    ageCatToIndex.put(M_75_INF, 4);
  }

  /**
   * Convertes map of measures that needs to be apply to a
   * {@link model.simulator.SJYHRSimulator} to a list of a list of integers.
   *
   * @param map the map to be converted
   * @return the value for each measure.
   */
  public List<List<Integer>> getMeasures(final Map map) {
    List<Integer> masks = new ArrayList<>();
    List<Integer> confinedCategories = new ArrayList<>();
    List<Integer> respectConfAsList = new ArrayList<>();
    List<List<Integer>> result = new ArrayList<>();

    LinkedTreeMap<String, Boolean> mask =
      (LinkedTreeMap) map.get(
        "mask");
    LinkedTreeMap<String, Boolean> conf =
      (LinkedTreeMap<String, Boolean>) map.get(
        "conf");
    double respectConfValue = (double) map.get(RESPECT_CONF);
    int respectConfValueAsInt = (int) respectConfValue;
    respectConfAsList.add(respectConfValueAsInt);

    for (Map.Entry<String, Boolean> entry : mask.entrySet()) {
      if (entry.getValue()) {
        masks.add(ageCatToIndex.get(entry.getKey()));
      }
    }
    for (Map.Entry<String, Boolean> entry : conf.entrySet()) {
      if (entry.getValue()) {
        confinedCategories.add(ageCatToIndex.get(entry.getKey()));
      }
    }
    result.add(confinedCategories);
    result.add(masks);
    result.add(respectConfAsList);
    return result;
  }
}
