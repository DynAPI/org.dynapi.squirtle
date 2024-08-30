package org.dynapi.squirtle.core;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Utils {

    /**
     * Resolves the is_aggregate flag for an expression that contains multiple terms.  This works like a voter system,
     * each term votes True or False or abstains with None.
     *
     * @param values A list of booleans (or null) for each term in the expression
     * @return If all values are True or None, True is returned.  If all values are None, None is returned. Otherwise, False is returned.
     */
    public static Boolean resolveIsAggregate(Boolean... values) {
        return resolveIsAggregate(Arrays.asList(values));
    }

    /**
     * Resolves the is_aggregate flag for an expression that contains multiple terms.  This works like a voter system,
     * each term votes True or False or abstains with None.
     *
     * @param values A list of booleans (or null) for each term in the expression
     * @return If all values are True or None, True is returned.  If all values are None, None is returned. Otherwise, False is returned.
     */
    public static Boolean resolveIsAggregate(List<Boolean> values) {
        List<Boolean> result = values.stream().filter(Objects::nonNull).toList();
        if (!result.isEmpty())
            return result.stream().allMatch(val -> val);
        return null;
    }

    /**
     * adds {@code quote_char} around {@code value} while also escaping {@code value} by doubling {@code quote_char} occurrences
     * @param value string-value to quote
     * @param quote_char quote-char to surround value with
     * @return quoted value
     */
    public static String formatQuotes(String value, String quote_char) {
        if (quote_char == null) return value;
        final String escapedValue = value.replace(quote_char, quote_char+quote_char);
        return quote_char + escapedValue + quote_char;
    }

    /**
     *
     * @param sql sql-string
     * @param alias (optional) alias to add
     * @param config {@link SqlAbleConfig} to know how to format it
     * @return alias-slq
     */
    public static String formatAliasSql(String sql, String alias, SqlAbleConfig config) {
        if (alias == null) return sql;
        final String asWord = config.isAsKeyword() ? " AS " : " ";
        final String aliasWord = formatQuotes(alias, Objects.requireNonNullElse(config.getAliasQuoteChar(), config.getQuoteChar()));
        return sql + asWord + aliasWord;
    }

    /**
     *
     * @param args values to test if isInstance of type
     * @param exc exception to throw (null for RuntimeException)
     * @param type class each arg should be instanced of (null for no check)
     * @throws Throwable see {@code exc}
     */
    public static void validate(Object[] args, Throwable exc, Class<?> type) throws Throwable {
        if (type != null) {
            for (Object arg : args) {
                if (!type.isInstance(arg)) {
                    throw exc == null ? new RuntimeException() : exc;
                }
            }
        }
    }

    /**
     *
     * @param type class to instantiate
     * @param initArgs arguments to pass to the class
     * @return instance of {@code type}
     */
    public static<T> T newInstance(Class<T> type, Object[] initArgs) {
        Class<?>[] signature = (Class<?>[]) Arrays.stream(initArgs).map(Object::getClass).toList().toArray(new Class[0]);
        return newInstance(type, initArgs, signature);
    }

    /**
     *
     * @param type class to instantiate
     * @param initArgs arguments to pass to the class
     * @param signature signature of the constructor if initArgs classes don't match
     * @return instance of {@code type}
     */
    public static<T> T newInstance(Class<T> type, Object[] initArgs, Class<?>[] signature) {
        try {
            return type.getConstructor(signature).newInstance(initArgs);
        } catch (Exception e) {
            String argsString = String.join(", ", Arrays.stream(initArgs).map(Object::toString).toList());
            String errorMessage = String.format("Can't instantiate %s(%s) because %s (%s)", type.getCanonicalName(), argsString, e.getClass().getSimpleName(), e.getMessage());
            throw new RuntimeException(errorMessage);
        }
    }
}
