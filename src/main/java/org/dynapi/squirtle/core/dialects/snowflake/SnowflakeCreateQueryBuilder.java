package org.dynapi.squirtle.core.dialects.snowflake;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.queries.CreateQueryBuilder;
import org.dynapi.squirtle.core.queries.Query;

public class SnowflakeCreateQueryBuilder extends CreateQueryBuilder {
    public static String QUOTE_CHAR = null;
    public static Class<? extends Query> QUERY_CLASS = SnowflakeQuery.class;

    public SnowflakeCreateQueryBuilder() {
        super(Dialects.SNOWFLAKE);
    }
}
