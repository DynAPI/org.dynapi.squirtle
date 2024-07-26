package org.dynapi.squirtle.core.terms;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Interval implements Node, SqlAble {
    private static final Map<Dialects, String> templates;
    private static final String templateDefault = "INTERVAL '%s %s'";

    static {
        templates = new HashMap<>();
        // PostgreSQL, Redshift and Vertica require quotes around the expr and unit e.g. INTERVAL '1 week'
        templates.put(Dialects.POSTGRESQL, "INTERVAL '%s %s'");
        templates.put(Dialects.REDSHIFT, "INTERVAL '%s %s'");
        templates.put(Dialects.VERTICA, "INTERVAL '%s %s'");
        // Oracle and MySQL requires just single quotes around the expr
        templates.put(Dialects.ORACLE, "INTERVAL '%s' %s");
        templates.put(Dialects.MYSQL, "INTERVAL '%s' %s");
    }

    private static final String[] units = new String[]{"years", "months", "days", "hours", "minutes", "seconds", "microseconds"};
    private static final String[] labels = new String[]{"YEAR", "MONTH", "DAY", "HOUR", "MINUTE", "SECOND", "MICROSECOND"};

    private static final Pattern trimPatterns = Pattern.compile("(^0+\\.)|(\\.0+$)|(^[0\\-.: ]+[\\-: ])|([\\-:. ][0\\-.: ]+$)");

    private final Dialects dialect;
    private String largestLabel;
    private String smallestLabel;
    private boolean isNegative;

    private final Integer years;
    private final Integer months;
    private final Integer days;
    private final Integer hours;
    private final Integer minutes;
    private final Integer seconds;
    private final Integer microseconds;

    public Interval(Integer years, Integer months, Integer days, Integer hours, Integer minutes, Integer seconds, Integer microseconds, Dialects dialect) {
        this.dialect = dialect;
        this.largestLabel = null;
        this.smallestLabel = null;
        this.isNegative = false;

        this.years = years == null ? 0 : Math.abs(years);
        this.months = months == null ? 0 : Math.abs(months);
        this.days = days == null ? 0 : Math.abs(days);
        this.hours = hours == null ? 0 : Math.abs(hours);
        this.minutes = minutes == null ? 0 : Math.abs(minutes);
        this.seconds = seconds == null ? 0 : Math.abs(seconds);
        this.microseconds = microseconds == null ? 0 : Math.abs(microseconds);

        final Integer[] values = {years, months, days, hours, minutes, seconds, microseconds};

        for (int i = 0; i < units.length; i++) {
            String label = labels[i];
            Integer value = values[i];
            if (value == null) continue;
            if (largestLabel == null) {
                largestLabel = label;
                isNegative = value < 0;
            }
            smallestLabel = label;
        }
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        Dialects dialect = this.dialect != null ? this.dialect : config.getDialect();

        String expr;
        String unit;

        if (Objects.equals(largestLabel, "MICROSECOND")) {
            expr = microseconds.toString();
            unit = "MICROSECOND";
        } else {
            expr = String.format(
                    "%d-%d-%d %d:%d:%d.%d",
                    years, months, days, hours, minutes, seconds, microseconds
            );
            expr = expr.replaceAll(trimPatterns.pattern(), "");
            if (isNegative)
                expr = "-" + expr;

            unit = Objects.equals(largestLabel, smallestLabel) ? largestLabel : String.format("%s_%s", largestLabel, smallestLabel);

            if (unit == null) {
                unit = "DAY";
            }
        }

        String template = templates.getOrDefault(dialect, templateDefault);

        return String.format(template, expr, unit);
    }
}
