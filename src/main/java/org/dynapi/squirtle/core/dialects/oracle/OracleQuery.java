package org.dynapi.squirtle.core.dialects.oracle;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class OracleQuery extends Query {
    @Override
    protected QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(OracleQueryBuilder.class, args);
    }
}
