package org.dynapi.squirtle.core.dialects.redshift;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class RedshiftQueryBuilder extends QueryBuilder {
    public Class<? extends Query> sqlAbleQueryClass() { return RedshiftQuery.class; }

    public RedshiftQueryBuilder(RedshiftQueryBuilder original) {
        super(original);
    }

    public RedshiftQueryBuilder(Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(Dialects.REDSHIFT, wrapSetOperationQueries, wrapperClass, immutable, asKeyword);
    }
}
