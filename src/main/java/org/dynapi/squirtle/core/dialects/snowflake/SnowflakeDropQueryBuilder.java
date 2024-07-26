package org.dynapi.squirtle.core.dialects.snowflake;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.queries.DropQueryBuilder;
import org.dynapi.squirtle.core.queries.Query;

public class SnowflakeDropQueryBuilder extends DropQueryBuilder {
    public static String QUOTE_CHAR = null;
    public static Class<? extends Query> QUERY_CLASS = SnowflakeQuery.class;

    public SnowflakeDropQueryBuilder() {
        super(Dialects.SNOWFLAKE);
    }
}
