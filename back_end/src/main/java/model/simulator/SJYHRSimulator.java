package model.simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulator.
 */
public class SJYHRSimulator implements Simulator {

  /**
   * People will be divided in five age category, each with its own
   * characteristics.
   */
  @Getter
  @Setter
  private class AgeCategory {
    /**
     * Susceptible.
     */
    List<Double> si;
    /**
     * Light infected.
     */
    List<List<Double>> ji;
    /**
     * Heavy infected.
     */
    List<List<Double>> yi;
    /**
     * Hospitalized.
     */
    List<List<Double>> hi;
    /**
     * Recovered.
     */
    List<Double> ri;
    /**
     * Dead.
     */
    List<Double> di;

    /**
     * Heavy infection rate.
     */
    double thetai;
    /**
     * Hospitalized death rate.
     */
    double mui;
    /**
     * The coefficient c in normal time.
     */
    double ci;
    /**
     * The coefficient c in confinement.
     */
    double ciConfined;

    /**
     * Constructor of the i-th age category.
     *
     * @param initialState initial rates of every person category.
     * @param i            index of the age category.
     * @param theta        heavy infection rate.
     * @param mu           hospitalized death rate.
     * @param cNew        cNew in normal time.
     * @param cConfined    cNew in confinement.
     */
    AgeCategory(final List<Double> initialState, final int i,
                final double theta, final double mu,
                final double cNew, final double cConfined) {
      si = new ArrayList<>();
      si.add(initialState.get(i * nbParam));

      ji = new ArrayList<>(g);
      for (int j = 0; j < g; ++j) {
        ji.add(new ArrayList<>());
        ji.get(j).add(initialState.get(i * nbParam + 1 + j));
      }

      yi = new ArrayList<>(h);
      for (int j = 0; j < h; ++j) {
        yi.add(new ArrayList<>());
        yi.get(j).add(initialState.get(i * nbParam + 1 + g + j));
      }

      hi = new ArrayList<>(u);
      for (int j = 0; j < u; ++j) {
        hi.add(new ArrayList<>());
        hi.get(j).add(initialState.get(i * nbParam + 1 + g + h + j));
      }

      ri = new ArrayList<>();
      ri.add(initialState.get(i * nbParam + 1 + g + h + u));

      di = new ArrayList<>();
      di.add(initialState.get(i * nbParam + 1 + g + h + u + 1));

      thetai = theta;
      mui = mu;
      ci = cNew;
      ciConfined = cConfined;
    }
  }

  // parameters common to all age categories
  /**
   * Number of age categories.
   */
  private final int n;
  /**
   * Time of a light infection.
   */
  private final int g;
  /**
   * Max time before going to hospital.
   */
  private final int h;
  /**
   * Max time before leaving hospital.
   */
  private final int u;
  /**
   * Number of parameters.
   */
  private final int nbParam;
  /**
   * Hospitalization rate for the heavily infected, eta is an array of size
   * h.
   */
  List<Double> eta;
  /**
   * Hospitalized recovery rate, nu is an array of size u.
   */
  List<Double> nu;
  /**
   * Contagion rate while being infected, zeta is an array of size max(g, h)
   * zeta[k] is the contagion rate after k days of infection.
   */
  List<Double> zeta;
  /**
   * Basic reproductive rate.
   */
  double r0;
  /**
   * Size of the population.
   */
  double s0;
  /**
   * Distance rate of every age category, c is an array of size n.
   */
  List<Double> c;

  /**
   * The n age categories.
   */
  private List<AgeCategory> ageCategories;
  /**
   * The differential equations that describes the model.
   */
  private CauchyProblem model;
  /**
   * The solver method.
   */
  private final DifferentialSolver solver;
  /**
   * The precision for the solver.
   */
  private final int nbIterations;

  /**
   * @param initS list of size n, initial rates of susceptible.
   * @param initJ list of size n, initial rante of light infected.
   * @param initY list of size n, initial rante of heavy infected.
   */
  public SJYHRSimulator(final List<Double> initS,
                        final List<Double> initJ,
                        final List<Double> initY) {
    n = 5;
    h = 7;
    u = 30;
    g = 15;
    nbParam = 1 + g + h + u + 2;

    eta = new ArrayList<>(h);
    eta.add(0.019800996674983);
    eta.add(0.038431577566093);
    eta.add(0.054835871116274);
    eta.add(0.068171503117297);
    eta.add(0.077880078307141);
    eta.add(0.083721159128524);
    eta.add(1.);

    nu = new ArrayList<>(u);
    nu.add(0.279529548717468);
    nu.add(0.232378190716258);
    nu.add(0.209836368913548);
    nu.add(0.190466877227672);
    nu.add(0.169403073518076);
    nu.add(0.146313694656761);
    nu.add(0.122408679009779);
    nu.add(0.099222391436451);
    nu.add(0.07804904312467);
    nu.add(0.05972102303667);
    nu.add(0.044588146023765);
    nu.add(0.03260490588277);
    nu.add(0.023459478889861);
    nu.add(0.016700896161408);
    nu.add(0.011840776117942);
    nu.add(0.008421561632826);
    nu.add(0.006053358910595);
    nu.add(0.004426574270714);
    nu.add(0.003308830028639);
    nu.add(0.002533619814707);
    nu.add(0.001986151513213);
    nu.add(0.001589727084837);
    nu.add(0.001294302511991);
    nu.add(0.001067710486963);
    nu.add(0.000889368688435);
    nu.add(0.00074600811335);
    nu.add(0.000628898963787);
    nu.add(0.000532112830217);
    nu.add(0.000451463616165);
    nu.add(1.);

    zeta.add(0.049686146);
    zeta.add(0.107848814);
    zeta.add(0.152150834);
    zeta.add(0.170897764);
    zeta.add(0.162294381);
    zeta.add(0.13354738);
    zeta.add(0.096324484);
    zeta.add(0.061246889);
    zeta.add(0.034425138);
    zeta.add(0.017124111);
    zeta.add(0.007539966);
    zeta.add(0.002937658);
    zeta.add(0.001012027);
    zeta.add(0.000307988);
    zeta.add(0.0000827103);

    r0 = 2.4;
    s0 = 67000000.; // 67 millions

    c = new ArrayList<>(n);
    c.add(0.8);
    c.add(0.9);
    c.add(0.6);
    c.add(0.3);
    c.add(0.1);

    List<Double> initialState = new ArrayList<>(5 * nbParam);
    for (int k = 0; k < 5 * nbParam; ++k) {
      initialState.add(0.);
    }
    for (int i = 0; i < 5; ++i) {
      initialState.set(i * nbParam, initS.get(i));
      initialState.set(i * nbParam + 1, initJ.get(i));
      initialState.set(i * nbParam + 1 + g, initY.get(i));
    }

    ageCategories = new ArrayList<>(n);
    ageCategories.add(new AgeCategory(initialState,
      0, 0.0072, 0.003, 0.8, 0.05));
    ageCategories.add(new AgeCategory(initialState,
      1, 0.144, 0.01, 0.9, 0.2));
    ageCategories.add(new AgeCategory(initialState,
      2, 1.77, 0.08, 0.6, 0.15));
    ageCategories.add(new AgeCategory(initialState,
      3, 6.57, 0.22, 0.3, 0.05));
    ageCategories.add(new AgeCategory(initialState,
      4, 12.96, 0.44, 0.1, 0.01));

    model = makeModel(initialState);
    solver = new RK4Solver();
    nbIterations = 50;
  }

  /**
   * Update the values for the next day.
   */
  @Override
  public void step() {
    List<Double> nextValues = solver.next(model, nbIterations);

    for (int i = 0; i < n; ++i) {
      ageCategories.get(i).getSi()
        .add(nextValues.get(i * nbParam));
      for (int j = 0; j < g; ++j) {
        ageCategories.get(i).getJi().get(j)
          .add(nextValues.get(i * nbParam + 1 + j));
      }
      for (int j = 0; j < h; ++j) {
        ageCategories.get(i).getYi().get(j)
          .add(nextValues.get(i * nbParam + 1 + g + j));
      }
      for (int j = 0; j < u; ++j) {
        ageCategories.get(i).getHi().get(j)
          .add(nextValues.get(i * nbParam + 1 + g + h + j));
      }
      ageCategories.get(i).getRi()
        .add(nextValues.get(i * nbParam + 1 + g + h + u));
      ageCategories.get(i).getDi()
        .add(nextValues.get(i * nbParam + 1 + g + h + u + 1));
    }

    model = makeModel(nextValues);
  }

  /**
   * Makes the model according to the initial condition.
   *
   * @param state the initial conditions.
   * @return the differential equation system, together with the initial
   * condition
   */
  private CauchyProblem makeModel(final List<Double> state) {
    CauchyProblem.Builder builder = new CauchyProblem.Builder();
    for (int i = 0; i < n; ++i) {
      final int i1 = i;
      double thetai = ageCategories.get(i).getThetai();

      // S_i
      builder.addParameter(state.get(i * nbParam),
        // S_i -> J_i1 and Y_i1
        ty -> -makeLambdai(ty.getY(), i1)
          * ty.getYi(i1 * nbParam));

      // J_i1
      builder.addParameter(state.get(i * nbParam + 1),
        // S_i -> J_i1
        ty -> makeLambdai(ty.getY(), i1) * (1. - thetai)
          * ty.getYi(i1 * nbParam)
          // J_i1 -> J_i2
          - ty.getYi(i1 * nbParam + 1));

      // J_i2 to J_ig
      for (int j = 1; j < g; ++j) {
        final int j1 = j;
        builder.addParameter(state.get(i * nbParam + 1 + j),
          // Y_ij-1 -> Y_ij
          ty -> ty.getYi(i1 * nbParam + 1 + j1 - 1)
            // Y_ij -> Y_ij+1
            - ty.getYi(i1 * nbParam + 1 + j1));
      }

      // Y_i1
      builder.addParameter(state.get(i * nbParam + 1 + g),
        // S_i -> Y_i1
        ty -> makeLambdai(ty.getY(), i1) * thetai
          * ty.getYi(i1 * nbParam)
          // Y_i1 -> Y_i2 and H_i1
          - ty.getYi(i1 * nbParam + 1 + g));

      // Y_i2 to Y_ih
      for (int j = 1; j < h; ++j) {
        final int j1 = j;
        builder.addParameter(state.get(i * nbParam + 1 + g + j),
          // Y_ij-1 -> Y_ij
          ty -> (1. - eta.get(j1 - 1))
            * ty.getYi(i1 * nbParam + 1 + g + j1 - 1)
            // Y_ij -> Y_ij+1 and H_i1
            - ty.getYi(i1 * nbParam + 1 + g + j1));
      }

      // H_i1
      builder.addParameter(state.get(i * nbParam + 1 + g + h),
        // Y_i1 to Y_ih -> H_i1
        ty -> heavyInfectedToHospitalized(ty.getY(), i1)
          // H_i1 -> H_i2, R_i and D_i
          - ty.getYi(i1 * nbParam + 1 + g + h));

      // H_i2 to H_iu
      for (int j = 1; j < u; ++j) {
        int j1 = j;
        builder.addParameter(state.get(i * nbParam + 1 + g + h + j),
          // H_ij-1 -> H_ij
          ty -> (1. - nu.get(j1 - 1))
            * ty.getYi(i1 * nbParam + 1 + g + h + j1 - 1)
            // H_ij -> H_ij+1, R_i and D_i
            - ty.getYi(i1 * nbParam + 1 + g + h + j1));
      }

      // R_i
      builder.addParameter(state.get(i * nbParam + 1 + g + h + u),
        // H_i1 to H_iu -> R_i
        ty -> hospitalizedToRecovered(ty.getY(), i1)
          // J_ig to R_i
          + ty.getYi(i1 * nbParam + 1 + g - 1));

      // D_i
      builder.addParameter(state.get(i * nbParam + 1 + g + h + u + 1),
        // H_i1 to H_iu -> D_i
        ty -> hospitalizedToDead(ty.getY(), i1));
    }

    return builder.build();
  }

  /**
   * Computes Lambda_i.
   *
   * @param state the state of the system.
   * @param i     the ageCategory we are studying.
   * @return Lambda_i.
   */
  private double makeLambdai(final List<Double> state,
                             final int i) {
    double iBar = makeIBar(state);
    return iBar / (s0 / (c.get(i) * r0) + iBar);
  }

  /**
   * Intermediary function for computing Lambda_i.
   *
   * @param state the state of the system.
   * @return iBar.
   */
  private double makeIBar(final List<Double> state) {
    double iBar = 0.;
    for (int i = 0; i < n; ++i) {
      double sumJ = 0.;
      double sumY = 0.;
      for (int j = 0; j < g; ++j) {
        sumJ += zeta.get(j) * state.get(i * nbParam + 1 + j);
      }
      for (int j = 0; j < g; ++j) {
        sumJ += zeta.get(j) * state.get(i * nbParam + 1 + g + j);
      }
      iBar += c.get(i) * (sumJ + sumY);
    }
    return iBar;
  }

  /**
   * @param state the state of the system.
   * @param i     the age category index.
   * @return the part of the derivative related with the heavily infected
   * going to hospital.
   */
  private double heavyInfectedToHospitalized(final List<Double> state,
                                             final int i) {
    double res = 0.;
    for (int j = 0; j < h; ++j) {
      res += eta.get(j) * state.get(i * nbParam + 1 + g + j);
    }
    return res;
  }

  /**
   * @param state the state of the system.
   * @param i     the age category index.
   * @return the part of the derivative related with people leaving the
   * hospital alive.
   */
  private double hospitalizedToRecovered(final List<Double> state,
                                         final int i) {
    double res = 0.;
    double mu = ageCategories.get(i).getMui();
    for (int j = 0; j < u; ++j) {
      res += nu.get(j)
        * state.get(i * nbParam + 1 + g + h + j)
        * (1. - mu);
    }
    return res;
  }

  /**
   * @param state the state of the system.
   * @param i     the age category index.
   * @return the part of the derivative related with people leaving the
   * hospital dead.
   */
  private double hospitalizedToDead(final List<Double> state,
                                    final int i) {
    double res = 0.;
    double mu = ageCategories.get(i).getMui();
    for (int j = 0; j < u; ++j) {
      res += nu.get(j) * state.get(i * nbParam + 1 + g + h + j) * mu;
    }
    return res;
  }

  /**
   * Applies the government measures.
   *
   * @param confinedCategories Age categories to confine.
   * @param maskedCategories   Age categories who will have to wear a mask.
   * @param confinementRespect indicator of the respect of the confinement.
   */
  public void applyMeasures(final List<Integer> confinedCategories,
                            final List<Integer> maskedCategories,
                            final double confinementRespect) {
    applyConfinement(confinedCategories, confinementRespect);
    setMaskUsage(maskedCategories);
  }

  /**
   * Sets the confinement for the age categories represented by indexes and
   * stops it for the other ones.
   *
   * @param confinedCategories age categories that needs to be confined.
   * @param confinementRespect indicator of the respect of the confinement.
   */
  private void applyConfinement(final List<Integer> confinedCategories,
                                final double confinementRespect) {
    for (int i = 0; i < n; ++i) {
      if (confinedCategories.contains(i)) {
        applyConfinement(i, confinementRespect);
      } else {
        stopConfinement(i);
      }
    }
  }

  /**
   * Sets the confinement for an age category.
   *
   * @param i                  age category index.
   * @param confinementRespect indicator of the respect of the confinement.
   */
  private void applyConfinement(final int i,
                                final double confinementRespect) {
    double a = ageCategories.get(i).getCi();
    double b = ageCategories.get(i).getCiConfined();
    c.set(i, (b - a) * confinementRespect + a);
  }

  /**
   * Stops the confinement for an age category.
   *
   * @param i age category index.
   */
  private void stopConfinement(final int i) {
    c.set(i, ageCategories.get(i).getCi());
  }

  /**
   * Sets the mask usage for the age categories represented by indexes and
   * stops it for the other ones.
   *
   * @param maskedCategories age categories that needs to ware a mask.
   */
  private void setMaskUsage(final List<Integer> maskedCategories) {
    for (int i = 0; i < n; ++i) {
      if (maskedCategories.contains(i)) {
        c.set(i, c.get(i) * 0.32);
      }
    }
  }
}
