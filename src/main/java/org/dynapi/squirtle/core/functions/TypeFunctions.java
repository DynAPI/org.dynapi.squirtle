package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.enums.SqlTypes;
import org.dynapi.squirtle.core.interfaces.SpecialParamsSqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Interval;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.functions.Function;
import org.dynapi.squirtle.core.terms.values.LiteralValue;

public class TypeFunctions {
    public static class Cast extends Function implements SpecialParamsSqlAble {
        private final SqlTypes asType;

        public Cast(String alias, Term term, SqlTypes asType) {
            super(alias, "CAST", term);
            this.asType = asType;
        }

        @Override
        public String getSpecialParamsSql(SqlAbleConfig config) {
            String typeSql = (asType instanceof SqlAble)
                ? ((SqlAble) asType).getSql(config)
                : asType.toString().toUpperCase();
            return String.format("AS %s", typeSql);
        }
    }

    public static class Convert extends Function implements SpecialParamsSqlAble {
        private final String encoding;

        public Convert(String alias, Term term, String encoding) {
            super(alias, "CONVERT", term);
            this.encoding = encoding;
        }

        @Override
        public String getSpecialParamsSql(SqlAbleConfig config) {
            return String.format("USING %s", encoding);
        }
    }

    public static class ToChar extends Function {
        public ToChar(String alias, Term term, Object asType) {
            super(alias, "TO_CHAR", term, asType);
        }
    }

    public static class Signed extends Cast {
        public Signed(String alias, Term term) {
            super(alias, term, SqlTypes.SIGNED);
        }
    }

    public static class UnSigned extends Cast {
        public UnSigned(String alias, Term term) {
            super(alias, term, SqlTypes.UNSIGNED);
        }
    }

    public static class Date extends Function {
        public Date(String alias, Term term) {
            super(alias, "DATE", term);
        }
    }

    public static class DateDiff extends Function {
        public DateDiff(String alias, Interval interval, String startDate, String endDate) {
            super(alias, "DATEDIFF", interval, startDate, endDate);
        }
    }

    public static class TimeDiff extends Function {
        public TimeDiff(String alias, Interval interval, String startTime, String endTime) {
            super(alias, "DATEDIFF", interval, startTime, endTime);
        }
    }

    public static class DateAdd extends Function {
        public DateAdd(String alias, String datePart, Interval interval, Term term) {
            super(alias, "DATE_ADD", new LiteralValue(null, datePart), interval, term);
        }
    }

    public static class ToDate extends Function {
        public ToDate(String alias, Object value, String formatMask) {
            super(alias, "TO_DATE", value, formatMask);
        }
    }

    public static class Timestamp extends Function {
        public Timestamp(String alias, Term term) {
            super(alias, "TIMESTAMP", term);
        }
    }

    public static class TimestampAdd extends Function {
        public TimestampAdd(String alias, String datePart, Interval interval, String term) {
            super(alias, "TIMESTAMPADD", new LiteralValue(null, datePart), interval, term);
        }
    }
}
