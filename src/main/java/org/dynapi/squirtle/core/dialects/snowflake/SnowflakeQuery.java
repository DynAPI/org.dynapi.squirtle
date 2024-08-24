package org.dynapi.squirtle.core.dialects.snowflake;

import org.dynapi.squirtle.core.queries.*;

public class SnowflakeQuery extends Query {
    @Override
    public Class<? extends QueryBuilder> getQueryBuilderClass() { return SnowflakeQueryBuilder.class; }

    @Override
    public CreateQueryBuilder createTable(Table table) {
        return new SnowflakeCreateQueryBuilder().createTable(table);
    }

    @Override
    public DropQueryBuilder dropTable(Table table) {
        return new SnowflakeDropQueryBuilder().dropTable(table);
    }
}
