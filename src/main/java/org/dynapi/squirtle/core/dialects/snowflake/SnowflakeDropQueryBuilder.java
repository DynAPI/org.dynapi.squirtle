package org.dynapi.squirtle.core.dialects.snowflake;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.queries.DropQueryBuilder;
import org.dynapi.squirtle.core.queries.Query;

public class SnowflakeDropQueryBuilder extends DropQueryBuilder {
    public String sqlAbleQuoteChar() { return null; }
    public Class<? extends Query> sqlAbleQueryClass() { return SnowflakeQuery.class; }

    public SnowflakeDropQueryBuilder(SnowflakeDropQueryBuilder original) {
        super(original);
    }

    public SnowflakeDropQueryBuilder() {
        super(Dialects.SNOWFLAKE);
    }
}
