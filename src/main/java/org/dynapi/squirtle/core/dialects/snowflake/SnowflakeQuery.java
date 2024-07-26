package org.dynapi.squirtle.core.dialects.snowflake;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.*;

public class SnowflakeQuery extends Query {
    protected static QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(SnowflakeQueryBuilder.class, args);
    }

    public static CreateQueryBuilder createTable(Table table) {
        return new SnowflakeCreateQueryBuilder().createTable(table);
    }

    public static DropQueryBuilder dropTable(Table table) {
        return new SnowflakeDropQueryBuilder().dropTable(table);
    }
}
