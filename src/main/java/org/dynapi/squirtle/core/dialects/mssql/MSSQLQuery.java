package org.dynapi.squirtle.core.dialects.mssql;

import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class MSSQLQuery extends Query {
    @Override
    protected Class<? extends QueryBuilder> getQueryBuilderClass() { return MSSQLQueryBuilder.class; }
}
