package controller;

/**
 * State of the project. The view is either showing current or old data, this
 * corresponds to the Map state, or is in a simulation, i.e. the simulation
 * state.
 */
public enum State {
  /**
   * Map state.
   */
  MAP,
  /**
   * Simulation state.
   */
  SIMULATION;
}
