package org.dynapi.squirtle.core.dialects.postgresql;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.dialects.redshift.RedshiftQueryBuilder;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class PostgreSQLQuery extends Query {
    protected QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(PostgreSQLQueryBuilder.class, args);
    }
}
