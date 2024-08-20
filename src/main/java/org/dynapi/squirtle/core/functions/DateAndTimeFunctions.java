package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.interfaces.FunctionSqlAble;
import org.dynapi.squirtle.core.interfaces.SpecialParamsSqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.criterion.Field;
import org.dynapi.squirtle.core.terms.functions.Function;
import org.dynapi.squirtle.core.terms.values.LiteralValue;

public class DateAndTimeFunctions {
    public static class Now extends Function {
        public Now(Now original) {
            super(original);
        }

        public Now(String alias) {
            super(alias, "NOW");
        }
    }

    public static class UtcTimestamp extends Function {
        public UtcTimestamp(UtcTimestamp original) {
            super(original);
        }

        public UtcTimestamp(String alias) {
            super(alias, "UTC_TIMESTAMP");
        }
    }

    public static class CurrentTimestamp extends Function implements FunctionSqlAble {
        public CurrentTimestamp(CurrentTimestamp original) {
            super(original);
        }

        public CurrentTimestamp(String alias) {
            super(alias, "CURRENT_TIMESTAMP");
        }

        @Override
        public String getFunctionSql(SqlAbleConfig ignoredConfig) {
            return "CURRENT_TIMESTAMP";
        }
    }

    public static class CurrentDate extends Function {
        public CurrentDate(CurrentDate original) {
            super(original);
        }

        public CurrentDate(String alias) {
            super(alias, "CURRENT_DATE");
        }
    }

    public static class CurrentTime extends Function {
        public CurrentTime(CurrentTime original) {
            super(original);
        }

        public CurrentTime(String alias) {
            super(alias, "CURRENT_TIME");
        }
    }

    public static class Extract extends Function implements SpecialParamsSqlAble {
        private final Field field;

        public Extract(Extract original) {
            super(original);
            this.field = CloneUtils.copyConstructorClone(original.field);
        }

        public Extract(String alias, String datePart, Field field) {
            super(alias, "EXTRACT", new LiteralValue(null, datePart));
            this.field = field;
        }

        @Override
        public String getSpecialParamsSql(SqlAbleConfig config) {
            return String.format("FROM %s", field.getSql(config));
        }
    }
}
