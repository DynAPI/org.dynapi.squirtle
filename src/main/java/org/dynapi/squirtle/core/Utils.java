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

    public static String formatQuotes(String value, String quote_char) {
        if (quote_char == null) return value;

        return String.format("%s%s%s", quote_char, value, quote_char);
    }

    public static String formatAliasSql(
            String sql,
            String alias,
            SqlAbleConfig config
    ) {
        if (alias == null) return sql;

        String aliasQuoteChar = config.getAliasQuoteChar();
        return String.format(
                "%s%s%s",
                sql,
                config.isAsKeyword() ? " AS " : " ",
                formatQuotes(alias, aliasQuoteChar != null ? aliasQuoteChar : config.getQuoteChar())
                );
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
    public static<T> T newInstance(Class<T> type, Object... initArgs) {
        try {
            Class<?>[] argTypes = (Class<?>[]) Arrays.stream(initArgs).map(Object::getClass).toList().toArray(new Class[0]);
            return type.getConstructor(argTypes).newInstance(initArgs);
        } catch (Exception e) {
            String argsString = String.join(", ", Arrays.stream(initArgs).map(Object::toString).toList());
            String errorMessage = String.format("Can't instantiate %s(%s) because %s (%s)", type.getCanonicalName(), argsString, e.getClass().getSimpleName(), e.getMessage());
            throw new RuntimeException(errorMessage);
        }
    }
}
