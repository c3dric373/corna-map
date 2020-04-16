package model.simulator;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class CauchyProblem {
    private List<Double> initialConditions;
    private List<Function<List<Double>, Double>> system;

    public static class Builder {
        private final List<Double> initialConditions;
        private final List<Function<List<Double>, Double>> system;

        /**
         * TODO
         */
        public Builder() {
            system = new ArrayList<>();
            initialConditions = new ArrayList<>();
        }

        /**
         * TODO
         * @param initialCondition
         * @param derivative
         * @return
         */
        public Builder addParameter(final Double initialCondition, final Function<List<Double>, Double> derivative) {
            Validate.notNull(initialCondition, "initialCondition null");
            Validate.notNull(derivative, "derivate null");
            system.add(derivative);
            initialConditions.add(initialCondition);
            return this;
        }

        public CauchyProblem build() {
            CauchyProblem CP = new CauchyProblem();
            CP.system = system;
            CP.initialConditions = initialConditions;
            return CP;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
