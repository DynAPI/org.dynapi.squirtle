package org.dynapi.squirtle.core.dialects.snowflake;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class SnowflakeQueryBuilder extends QueryBuilder {
    public static String QUOTE_CHAR = null;
    public static String ALIAS_QUOTE_CHAR = "\"";
    public static String QUERY_ALIAS_QUOTE_CHAR = "";
    public static Class<? extends Query> QUERY_CLASS = SnowflakeQuery.class;

    public SnowflakeQueryBuilder(Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(Dialects.SNOWFLAKE, wrapSetOperationQueries, wrapperClass, immutable, asKeyword);
    }
}
