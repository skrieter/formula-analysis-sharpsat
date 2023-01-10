/*
 * Copyright (C) 2023 Sebastian Krieter
 *
 * This file is part of FeatJAR-formula-analysis-sharpsat.
 *
 * formula-analysis-sharpsat is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 *
 * formula-analysis-sharpsat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with formula-analysis-sharpsat. If not, see <https://www.gnu.org/licenses/>.
 *
 * See <https://github.com/FeatureIDE/FeatJAR-formula-analysis-sharpsat> for further information.
 */
package de.featjar.formula.configuration.list;

import de.featjar.base.computation.IComputation;
import de.featjar.formula.analysis.VariableMap;
import de.featjar.formula.analysis.bool.ABooleanAssignmentList;
import de.featjar.formula.analysis.bool.BooleanSolutionList;
import de.featjar.formula.analysis.metrics.AAggregatableMetrics;
import de.featjar.formula.analysis.metrics.ISampleMetric;
import de.featjar.formula.analysis.sharpsat.CountSolutionsAnalysis;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.DoubleSupplier;

public class DistributionMetrics extends AAggregatableMetrics {

    public static class RatioDiffFunction {

        private IComputation<CNF> rep;
        private final VariableMap termMap = null; // TODO
        private final BigDecimal totalCount;

        public RatioDiffFunction(IComputation<CNF> rep) {
            this.rep = rep;
            // termMap = rep.getFormula().getVariableMap().orElseGet(TermMap::new);
            totalCount = rep.map(CountSolutionsAnalysis::new)
                    .getResult()
                    .map(BigDecimal::new)
                    .orElseThrow();
        }

        public double compute(BooleanSolutionList sample, ABooleanAssignmentList expression) {
            //            final double sampleSize = sample.getSolutions().size();
            //            if (sampleSize == 0) {
            //                return 0;
            //            }
            //            int positiveCount = 0;
            //            for (final LiteralList solution : sample.getSolutions()) {
            //                for (final LiteralList clause : expression) {
            //                    if (solution.containsAll(clause)) {
            //                        positiveCount++;
            //                        break;
            //                    }
            //                }
            //            }
            //            final double sampleRatio = positiveCount / sampleSize;
            //            final CountSolutionsAnalysis analysis = new CountSolutionsAnalysis();
            //            final List<Expression> assumedConstraints = analysis.getAssumedConstraints();
            //            for (final LiteralList clause : expression) {
            //                assumedConstraints.add(Clauses.toOrClause(clause.negate(), termMap));
            //            }
            //            final BigDecimal negativeCount =
            //                    rep.getResult(analysis).map(BigDecimal::new).orElseThrow();
            //            final double actualRatio =
            //                    1 - negativeCount.divide(totalCount, MathContext.DECIMAL128).doubleValue();
            //            return Math.abs(actualRatio - sampleRatio);
            return 0; // TODO
        }
    }

    private final RatioDiffFunction function;
    private final List<ABooleanAssignmentList> expressionList;
    private final String functionName;

    public DistributionMetrics(
            RatioDiffFunction function, List<ABooleanAssignmentList> expressionList, String functionName) {
        this.function = function;
        this.expressionList = expressionList;
        this.functionName = functionName;
    }

    //    public DistributionMetrics(ModelRepresentation rep, List<ClauseList> expressionList, String functionName) {
    //        this.expressionList = expressionList;
    //        this.functionName = functionName;
    //        function = rep != null ? new RatioDiffFunction(rep) : null;
    //    }
    //
    //    public static List<SampleMetric> getAllAggregates(
    //            ModelRepresentation rep, List<ClauseList> expressionList, String functionName) {
    //        return new DistributionMetrics(rep, expressionList, functionName).getAllAggregates();
    //    }

    @Override
    public ISampleMetric getAggregate(String name, DoubleSupplier aggregate) {
        return new DoubleMetric(functionName + "_distribution_" + name, aggregate);
    }

    @Override
    public double[] computeValues() {
        final double[] values = new double[expressionList.size()];
        int index = 0;
        for (final ABooleanAssignmentList expression : expressionList) {
            values[index++] = function.compute(sample, expression);
        }
        return values;
    }
}
