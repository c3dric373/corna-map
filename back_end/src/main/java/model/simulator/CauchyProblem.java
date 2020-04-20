package model.simulator;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class CauchyProblem {
    private List<Double> initialConditions; // [f1(t0), f2(t0), ..., fn(t0), t0]
    private List<Function<List<Double>, Double>> system; // the time is at the end

    public static class Builder {
        private Double time;
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
            Validate.notNull(derivative, "derivative null");
            system.add(derivative);
            initialConditions.add(initialCondition);
            return this;
        }

        public CauchyProblem build() {
            CauchyProblem cauchyProblem = new CauchyProblem();
            cauchyProblem.system = system;
            cauchyProblem.initialConditions = initialConditions;
            return cauchyProblem;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}


/**
 Exemple de construction pour le modèle SIR

 final Double beta = 0.1;
 final Double lambda = 0.1;
 final Double mu = 0.001;

 CauchyProblem CP = CauchyProblem.builder()
 .addParameter(0.9, T -> - beta * T.get(0) * T.get(1))
 .addParameter(0.1, T -> beta * T.get(0) * T.get(1) - lambda * T.get(1) - mu * T.get(1))
 .addParameter(0., T -> lambda * T.get(1))
 .addParameter(0., T -> mu * T.get(1))
 .build();
 **/