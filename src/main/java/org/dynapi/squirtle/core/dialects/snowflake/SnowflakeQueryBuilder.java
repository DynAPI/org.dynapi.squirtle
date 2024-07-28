package org.dynapi.squirtle.core.dialects.snowflake;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class SnowflakeQueryBuilder extends QueryBuilder {
    public String sqlAbleQuoteChar() { return null; }
    public String sqlAbleAliasQuoteChar() { return "\""; }
    public String sqlAbleQueryAliasQuoteChar() { return ""; }
    public Class<? extends Query> sqlAbleQueryClass() { return SnowflakeQuery.class; }

    public SnowflakeQueryBuilder(Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(Dialects.SNOWFLAKE, wrapSetOperationQueries, wrapperClass, immutable, asKeyword);
    }
}
