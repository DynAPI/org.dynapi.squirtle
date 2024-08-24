package org.dynapi.squirtle.core.dialects.redshift;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class RedshiftQuery extends Query {
    @Override
    protected QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(RedshiftQueryBuilder.class, args);
    }
}
