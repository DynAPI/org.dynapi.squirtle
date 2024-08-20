package org.dynapi.squirtle.core.terms.values;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class ValueWrapper extends Term implements SqlAble {
    public Boolean isAggregate() { return null; }

    private final Object value;

    public ValueWrapper(ValueWrapper original) {
        super(original);
        this.value = CloneUtils.copyConstructorCloneNoFail(original.value);
    }

    public ValueWrapper(Object value) {
        this(null, value);
    }

    public ValueWrapper(String alias, Object value) {
        super(alias);
        this.value = value;
    }

    public String getValueSql(Object value, SqlAbleConfig config) {
        return getFormattedValue(value, config);
    }

    public static String getFormattedValue(Object value, SqlAbleConfig config) {
        String quoteChar = config.getSecondaryQuoteChar();

        if (value instanceof Term)
            return ((Term) value).getSql(config);
        if (value instanceof Enum<?>)
            return getFormattedValue(value.toString(), config);
        if (value instanceof Date) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset)
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            return getFormattedValue(df.format(value), config);
        }
        if (value instanceof String)
            return Utils.formatQuotes(((String) value).replace(quoteChar, quoteChar+quoteChar), quoteChar);
        if (value instanceof Boolean)
            return (boolean) value ? "true" : "false";
        if (value instanceof UUID)
            return getFormattedValue(value.toString(), config);
        if (value == null)
            return "null";

        return value.toString();
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        if (config.getSecondaryQuoteChar() == null)
            config = config.withSecondaryQuoteChar("'");

        String sql = getValueSql(value, config);
        return Utils.formatAliasSql(sql, alias, config);
    }
}
