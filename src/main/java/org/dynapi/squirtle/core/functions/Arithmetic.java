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

        public Sum(String alias, Object term) {
            super(alias, "SUM", term);
        }
    }

    public static class Pow extends Function {
        public Pow(Pow original) {
            super(original);
        }

        public Pow(String alias, Term term, float exponent) {
            super(alias, "POW", term, exponent);
        }
    }

    public static class Mod extends Function {
        public Mod(Mod original) {
            super(original);
        }

        public Mod(String alias, Term term, float modulus) {
            super(alias, "MOD", term, modulus);
        }
    }

    public static class Avg extends AggregateFunction {
        public Avg(Avg original) {
            super(original);
        }

        public Avg(String alias, Object term) {
            super(alias, "AVG", term);
        }
    }

    public static class Min extends AggregateFunction {
        public Min(Min original) {
            super(original);
        }

        public Min(String alias, Object term) {
            super(alias, "MIN", term);
        }
    }

    public static class Max extends AggregateFunction {
        public Max(Max original) {
            super(original);
        }

        public Max(String alias, Object term) {
            super(alias, "MAX", term);
        }
    }

    public static class Std extends AggregateFunction {
        public Std(Std original) {
            super(original);
        }

        public Std(String alias, Object term) {
            super(alias, "STD", term);
        }
    }

    public static class StdDev extends AggregateFunction {
        public StdDev(StdDev original) {
            super(original);
        }

        public StdDev(String alias, Object term) {
            super(alias, "STDDEV", term);
        }
    }

    public static class Abs extends AggregateFunction {
        public Abs(Abs original) {
            super(original);
        }

        public Abs(String alias, Object term) {
            super(alias, "ABS", term);
        }
    }

    public static class First extends AggregateFunction {
        public First(First original) {
            super(original);
        }

        public First(String alias, Object term) {
            super(alias, "FIRST", term);
        }
    }

    public static class Last extends AggregateFunction {
        public Last(Last original) {
            super(original);
        }

        public Last(String alias, Object term) {
            super(alias, "LAST", term);
        }
    }

    public static class Sqrt extends Function {
        public Sqrt(Sqrt original) {
            super(original);
        }

        public Sqrt(String alias, Object term) {
            super(alias, "SQRT", term);
        }
    }

    public static class Floor extends Function {
        public Floor(Floor original) {
            super(original);
        }

        public Floor(String alias, Object term) {
            super(alias, "FLOOR", term);
        }
    }

    public static class ApproximatePercentile extends AggregateFunction {
        private final float percentile;

        public ApproximatePercentile(ApproximatePercentile original) {
            super(original);
            this.percentile = original.percentile;
        }

        public ApproximatePercentile(String alias, Term term, float percentile) {
            super(alias, "APPROXIMATE_PERCENTILE", term);
            this.percentile = percentile;
        }

        @Override
        public String getSpecialParamsSql(SqlAbleConfig config) {
            return String.format("USING PARAMETERS percentile=%f", percentile);
        }
    }
}
