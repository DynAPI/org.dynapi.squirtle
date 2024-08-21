package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.functions.AggregateFunction;
import org.dynapi.squirtle.core.terms.functions.Function;

public class Arithmetic {
    public static class Sum extends DistinctOptionFunction {
        public Sum(Sum original) {
            super(original);
        }

        public Sum(Object term) {
            super("SUM", term);
        }
    }

    public static class Pow extends Function {
        public Pow(Pow original) {
            super(original);
        }

        public Pow(Term term, float exponent) {
            super("POW", term, exponent);
        }
    }

    public static class Mod extends Function {
        public Mod(Mod original) {
            super(original);
        }

        public Mod(Term term, float modulus) {
            super("MOD", term, modulus);
        }
    }

    public static class Avg extends AggregateFunction {
        public Avg(Avg original) {
            super(original);
        }

        public Avg(Object term) {
            super("AVG", term);
        }
    }

    public static class Min extends AggregateFunction {
        public Min(Min original) {
            super(original);
        }

        public Min(Object term) {
            super("MIN", term);
        }
    }

    public static class Max extends AggregateFunction {
        public Max(Max original) {
            super(original);
        }

        public Max(Object term) {
            super("MAX", term);
        }
    }

    public static class Std extends AggregateFunction {
        public Std(Std original) {
            super(original);
        }

        public Std(Object term) {
            super("STD", term);
        }
    }

    public static class StdDev extends AggregateFunction {
        public StdDev(StdDev original) {
            super(original);
        }

        public StdDev(Object term) {
            super("STDDEV", term);
        }
    }

    public static class Abs extends AggregateFunction {
        public Abs(Abs original) {
            super(original);
        }

        public Abs(Object term) {
            super("ABS", term);
        }
    }

    public static class First extends AggregateFunction {
        public First(First original) {
            super(original);
        }

        public First(Object term) {
            super("FIRST", term);
        }
    }

    public static class Last extends AggregateFunction {
        public Last(Last original) {
            super(original);
        }

        public Last(Object term) {
            super("LAST", term);
        }
    }

    public static class Sqrt extends Function {
        public Sqrt(Sqrt original) {
            super(original);
        }

        public Sqrt(Object term) {
            super("SQRT", term);
        }
    }

    public static class Floor extends Function {
        public Floor(Floor original) {
            super(original);
        }

        public Floor(Object term) {
            super("FLOOR", term);
        }
    }

    public static class ApproximatePercentile extends AggregateFunction {
        private final float percentile;

        public ApproximatePercentile(ApproximatePercentile original) {
            super(original);
            this.percentile = original.percentile;
        }

        public ApproximatePercentile(Term term, float percentile) {
            super("APPROXIMATE_PERCENTILE", term);
            this.percentile = percentile;
        }

        @Override
        public String getSpecialParamsSql(SqlAbleConfig config) {
            return String.format("USING PARAMETERS percentile=%f", percentile);
        }
    }
}
