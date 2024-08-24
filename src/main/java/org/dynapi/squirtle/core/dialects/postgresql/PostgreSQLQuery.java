package org.dynapi.squirtle.core.dialects.postgresql;

import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class PostgreSQLQuery extends Query {
    @Override
    protected Class<? extends QueryBuilder> getQueryBuilderClass() { return PostgreSQLQueryBuilder.class; }
}
