package org.dynapi.squirtle.core.dialects.oracle;

import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class OracleQuery extends Query {
    @Override
    protected Class<? extends QueryBuilder> getQueryBuilderClass() { return OracleQueryBuilder.class; }
}
