package org.dynapi.squirtle.core.dialects.redshift;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class RedshiftQueryBuilder extends QueryBuilder {
    public static Class<? extends Query> QUERY_CLASS = RedshiftQuery.class;

    public RedshiftQueryBuilder(Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(Dialects.REDSHIFT, wrapSetOperationQueries, wrapperClass, immutable, asKeyword);
    }
}
