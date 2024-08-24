package org.dynapi.squirtle.core.dialects.sqlite;

import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class SQLiteQuery extends Query {
    @Override
    public Class<? extends QueryBuilder> getQueryBuilderClass() { return SQLiteQueryBuilder.class; }
}
