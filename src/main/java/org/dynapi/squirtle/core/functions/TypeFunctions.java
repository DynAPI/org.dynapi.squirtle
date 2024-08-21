package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.enums.SqlType;
import org.dynapi.squirtle.core.interfaces.SpecialParamsSqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Interval;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.functions.Function;
import org.dynapi.squirtle.core.terms.values.LiteralValue;

public class TypeFunctions {
    public static class Cast extends Function implements SpecialParamsSqlAble {
        private final SqlType asType;

        public Cast(Cast original) {
            super(original);
            this.asType = original.asType;
        }

        public Cast(Term term, SqlType asType) {
            super("CAST", term);
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

        public Convert(Convert original) {
            super(original);
            this.encoding = original.encoding;
        }

        public Convert(Term term, String encoding) {
            super("CONVERT", term);
            this.encoding = encoding;
        }

        @Override
        public String getSpecialParamsSql(SqlAbleConfig config) {
            return String.format("USING %s", encoding);
        }
    }

    public static class ToChar extends Function {
        public ToChar(ToChar original) {
            super(original);
        }

        public ToChar(Term term, Object asType) {
            super("TO_CHAR", term, asType);
        }
    }

    public static class Signed extends Cast {
        public Signed(Signed original) {
            super(original);
        }

        public Signed(Term term) {
            super(term, SqlType.SIGNED);
        }
    }

    public static class UnSigned extends Cast {
        public UnSigned(UnSigned original) {
            super(original);
        }

        public UnSigned(Term term) {
            super(term, SqlType.UNSIGNED);
        }
    }

    public static class Date extends Function {
        public Date(Date original) {
            super(original);
        }

        public Date(Term term) {
            super("DATE", term);
        }
    }

    public static class DateDiff extends Function {
        public DateDiff(DateDiff original) {
            super(original);
        }

        public DateDiff(Interval interval, String startDate, String endDate) {
            super("DATEDIFF", interval, startDate, endDate);
        }
    }

    public static class TimeDiff extends Function {
        public TimeDiff(TimeDiff original) {
            super(original);
        }

        public TimeDiff(Interval interval, String startTime, String endTime) {
            super("DATEDIFF", interval, startTime, endTime);
        }
    }

    public static class DateAdd extends Function {
        public DateAdd(DateAdd original) {
            super(original);
        }

        public DateAdd(String datePart, Interval interval, Term term) {
            super("DATE_ADD", new LiteralValue(datePart), interval, term);
        }
    }

    public static class ToDate extends Function {
        public ToDate(ToDate original) {
            super(original);
        }

        public ToDate(Object value, String formatMask) {
            super("TO_DATE", value, formatMask);
        }
    }

    public static class Timestamp extends Function {
        public Timestamp(Timestamp original) {
            super(original);
        }

        public Timestamp(Term term) {
            super("TIMESTAMP", term);
        }
    }

    public static class TimestampAdd extends Function {
        public TimestampAdd(TimestampAdd original) {
            super(original);
        }

        public TimestampAdd(String datePart, Interval interval, String term) {
            super("TIMESTAMPADD", new LiteralValue(datePart), interval, term);
        }
    }
}
