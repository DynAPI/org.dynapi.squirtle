package org.dynapi.squirtle.core.dialects.snowflake;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.queries.CreateQueryBuilder;
import org.dynapi.squirtle.core.queries.Query;

public class SnowflakeCreateQueryBuilder extends CreateQueryBuilder {
    public String sqlAbleQuoteChar() { return null; }
    public Class<? extends Query> sqlAbleQueryClass() { return SnowflakeQuery.class; }

    public SnowflakeCreateQueryBuilder() {
        super(Dialects.SNOWFLAKE);
    }
}
