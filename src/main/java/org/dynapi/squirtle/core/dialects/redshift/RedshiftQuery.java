package org.dynapi.squirtle.core.dialects.redshift;

import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class RedshiftQuery extends Query {
    @Override
    protected Class<? extends QueryBuilder> getQueryBuilderClass() { return RedshiftQueryBuilder.class; }
}
