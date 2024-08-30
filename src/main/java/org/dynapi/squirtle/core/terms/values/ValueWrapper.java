package org.dynapi.squirtle.core.terms.values;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public class ValueWrapper extends Term implements SqlAble {
    public Boolean isAggregate() { return null; }

    private final Object value;

    public ValueWrapper(ValueWrapper original) {
        super(original);
        this.value = CloneUtils.copyConstructorCloneNoFail(original.value);
    }

    public ValueWrapper(Object value) {
        this.value = value;
    }

    public ValueWrapper as(String alias) {
        this.alias = alias;
        return this;
    }

    public String getValueSql(Object value, SqlAbleConfig config) {
        return getFormattedValue(value, config);
    }

    public static String getFormattedValue(Object value, SqlAbleConfig config) {
        String quoteChar = config.getSecondaryQuoteChar();

        if (value == null)
            return "null";
        if (value instanceof Term term)
            return term.getSql(config);
        // date and time
        if (value instanceof LocalDateTime localDateTime)
            return getFormattedValue(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime), config);
        if (value instanceof Date date)
            return getFormattedValue(DateTimeFormatter.ISO_INSTANT.format(date.toInstant()), config);
        if (value instanceof Instant instant)
            return getFormattedValue(DateTimeFormatter.ISO_INSTANT.format(instant), config);
        // basic types
        if (value instanceof CharSequence charSequence)
            return Utils.formatQuotes(charSequence.toString(), quoteChar);
        if (value instanceof Character character)
            return Utils.formatQuotes(character.toString(), quoteChar);
        if (value instanceof Boolean booleanValue)
            return booleanValue ? "true" : "false";
        if (value instanceof UUID uuid)
            return getFormattedValue(uuid.toString(), config);
        if (value instanceof Enum<?> enumValue)
            return getFormattedValue(enumValue.toString(), config);

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
